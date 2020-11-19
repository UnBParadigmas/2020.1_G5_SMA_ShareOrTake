package simulation.environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import generic.board.item.BoardItemGroup;
import graphics.MainWindow;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import simulation.creatures.CreatureState;
import simulation.creatures.SpecyState;
import simulation.resources.Food;

/**
 * Agente que controla o meio ambiente em que as criaturas estao inseridas
 */
public class EnvironmentAgent extends Agent {
	// Constantes
	private static final long serialVersionUID = -6481631683157763680L;

	public final static String SHARE = "SHARE";
	public final static String GOBACK = "GOBACK";
	public final static String DEAD = "DEAD";
	public final static String MOVE = "MOVE";
	public final static String DAY_ARAISE = "DAY_ARAISE";
	public final static String NIGHT_FALL = "NIGHT_FALL";
	public final static String FOOD_SEEK = "FOOD_SEEK";

	private MainWindow mainWindow = null;

	int boardSize = 0;

	private List<Food> foodResources = new ArrayList<>();
	private List<Food> randomFood = new ArrayList<>();
	private List<SpecyState> speciesState = new ArrayList<>();

	@Override
	protected void setup() {
		System.out.println("Iniciando " + getLocalName());

		this.registerInDFD();
		this.setUpUI();

		addBehaviour(new CyclicBehaviour(this) {
			static final long serialVersionUID = 1L;

			public void action() {
				ACLMessage msg = receive();

				if (msg != null) {
					switch (msg.getPerformative()) {
					case ACLMessage.REQUEST:
						
						switch (msg.getContent()) {
						case EnvironmentAgent.FOOD_SEEK:
							// Requisicao de comida pelas criaturas
							fulfillFoodSeekRequest(msg);
						}
						break;
					case ACLMessage.INFORM:

						switch (msg.getContent()) {
						default:
							System.out.println("Mensagem inesperada (ambiente).");
						}

						break;
					case ACLMessage.ACCEPT_PROPOSAL:
						break;
					default:
						break;
					}
				} else {
					// Bloquear caso mensagem for nula.
					block();
				}
			}
		});
	}

	@Override
	protected void takeDown() {
	}
	
	//Comportamento de Controle Temporal (Passagem dos dias)
	class IncrementDaysBehaviour extends Behaviour{
		
		private static final long serialVersionUID = 1L;
		
		// Se altera em dia e noite
		private boolean isDay = false;
		private int daysCount = 0;
		
		long delay;

		public IncrementDaysBehaviour(Agent a, long delay) {
			super(a);
			this.delay = delay;
		}

		public void action() {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			isDay = !isDay;
			if (isDay) {
				daysCount++;
				dayAlert();
			} else {
				nightAlert();
			}
		}

		public boolean done () {
			System.out.println ((isDay ?  "DIA " : "NOITE ") + daysCount);
			return daysCount > 10;
		}

	}
	
	// Alerta para todas as criaturas informando que esta de dia
	// As criaturas devem procurar comida pelo ambiente
	private void dayAlert() {
		sendBroadCast(ACLMessage.INFORM, DAY_ARAISE);
	}
	
	// Alerta para todas as criaturas informando que esta de noite
	// Todas as criaturas devem voltar a sua posicao inicial
	private void nightAlert() {
		sendBroadCast(ACLMessage.INFORM, NIGHT_FALL);
	}

	public void startSimulation(int boardSize, List<SpecyState> species, int creaturesPerSpecy, int foodAmount) {
		this.boardSize = boardSize;

		System.out.println("---------- INICIANDO SIMULACAO ----------");
		setUpFood(foodAmount);
		setUpCreaturesAgents(species, creaturesPerSpecy);
		
		System.out.println("Inicia rotina de controle temporal (passagem dos dias)");
		ThreadedBehaviourFactory incDayThread = new ThreadedBehaviourFactory();
		Behaviour incDayBehaviour = new IncrementDaysBehaviour(this, 1000);
		addBehaviour(incDayThread.wrap(incDayBehaviour));
	}
	
	public void stopSimulation() {
		mainWindow.clearBoard();
		sendBroadCast(ACLMessage.INFORM, DEAD);
		System.out.println("---------- PARANDO SIMULACAO ----------");
	}

	// Envia a mesma mensagem a todas as criaturas
	private void sendBroadCast(int type, String content) {
		for (SpecyState specy : speciesState) {
			for (CreatureState creature : specy.getCreaturesState()) {
				ACLMessage msg = new ACLMessage(type);
				
				msg.setContent(content);
	            msg.addReceiver(creature.getId());
	            send(msg);
			}
		}
	}

	private void registerInDFD() {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	private void setUpUI() {
		mainWindow = new MainWindow(this);
		mainWindow.setVisible(true);
	}

	private void setUpFood(int foodAmount) {
		BoardItemGroup foodGroup;

		this.foodResources = new ArrayList<>();

		Food.createFoodResources(this.foodResources, foodAmount, 0, boardSize - 1);
		foodGroup = new BoardItemGroup(this.foodResources, "/food.png");

		mainWindow.insertElementsGroup(foodGroup);
	}

	private void setUpCreaturesAgents(List<SpecyState> species, int creaturesPerSpecy) {
		BoardItemGroup creaturesGroup;

		// Especies Adicionadas
		speciesState = species;

		for (int i = 0; i < this.speciesState.size(); i++) {
			createCreatureAgents(this.speciesState, i, creaturesPerSpecy, 0, boardSize - 1);
			creaturesGroup = new BoardItemGroup(this.speciesState.get(i).getCreaturesState(),
					this.speciesState.get(i).getImagePath());
			mainWindow.insertElementsGroup(creaturesGroup);
		}
	}

	private void createCreatureAgents(List<SpecyState> species, int specyIndex, int amount, int minPos, int maxPos) {
		PlatformController container = getContainerController();

		try {
			for (int i = 0; i < amount; i++) {
				String creatureName = species.get(specyIndex).getName() + "_" + i;
				int pos[] = CreatureState.getRandomPos(species, minPos, maxPos);

				AgentController creatureCtl = container.createNewAgent(creatureName,
						"simulation.creatures.CreatureAgent",
						new Object[] { pos[0], pos[1], species.get(specyIndex).getShareStrategy() });
				creatureCtl.start();

				species.get(specyIndex).addCreatureState(new CreatureState(new AID(creatureName, AID.ISLOCALNAME),
						pos[0], pos[1], species.get(specyIndex).getShareStrategy()));
			}
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	private void fulfillFoodSeekRequest(ACLMessage msg) {
		System.out.println(msg.getSender().getLocalName() + " requisita comida");
		if (this.randomFood == null || this.randomFood.isEmpty()) {
			this.randomFood = randomElementOneRepeat(this.foodResources);
		}
		Food newCoords = this.randomFood.get(0);
		this.randomFood.remove(0);
		ACLMessage coords = new ACLMessage(ACLMessage.PROPOSE);
		coords.setSender(this.getAID());
		coords.addReceiver(msg.getSender());
		try {
			Object[] oMsg = new Object[3];
			oMsg[0] = newCoords.getFoodAmount();
			oMsg[1] = newCoords.getXPos();
			oMsg[2] = newCoords.getYPos();

			coords.setContentObject(oMsg);
		} catch (IOException ex) {
			System.err.println("Mensagem de busca por comida incompleta.");
			ex.printStackTrace(System.err);
		}
		send(coords);
	}

	private List<Food> randomElementOneRepeat(List<Food> foods) {
		List<Food> foodCopy = new ArrayList<>(foods);
		Random rand = new Random();
		List<Food> randomSequence = new ArrayList<>();
		int numberOfElements = foods.size();
		Boolean repeat[] = new Boolean[numberOfElements];

		for (int i = 0; i < numberOfElements; i++) {
			int randomIndex = rand.nextInt(foodCopy.size());
			Food randomElement = foodCopy.get(randomIndex);
			randomSequence.add(randomElement);
			repeat[randomIndex] = true;
			if (repeat[randomIndex] == true) {
				foodCopy.remove(randomIndex);
			}
		}
		return randomSequence;
	}
}
