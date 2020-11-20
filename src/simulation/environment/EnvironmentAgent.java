package simulation.environment;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import graphics.MainWindow;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.UnreadableException;
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
	public final static String REPRODUCE = "REPRODUCE";

	private final static Map<String, String> SPECIES_IMAGE_PATH = new HashMap<String, String>();
	static {
		SPECIES_IMAGE_PATH.put("dove", "/specy_1.png");
		SPECIES_IMAGE_PATH.put("hawk", "/specy_2.png");
	}

	private MainWindow mainWindow = null;

	int boardSize = 0;

	private List<Food> foodResources = new ArrayList<>();
	private List<Food> randomFood = new ArrayList<>();
	private List<CreatureState> creaturesState = new ArrayList<>();
	private List<CreatureState> creaturePool = new ArrayList<>();
	private int totalIterations = 0;

	// Thread para avisar periodicamente para as criaturas irem buscar comida
	ThreadedBehaviourFactory incDayThread = new ThreadedBehaviourFactory();

	@Override
	protected void setup() {
		System.out.println("Iniciando " + getLocalName());

		this.registerInDFD();
		this.setUpUI();

		addBehaviour(new CyclicBehaviour(this) {
			static final long serialVersionUID = 1L;

			public void action() {
				ACLMessage msg = receive();
				EnvironmentAgent envAgent = (EnvironmentAgent) this.myAgent;

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

						if (msg.getContent().startsWith(EnvironmentAgent.GOBACK)) {

							String msgParts[] = msg.getContent().split(",");

							msg.getSender().getLocalName();

							moveCreature(envAgent, msg.getSender(), Integer.parseInt(msgParts[1]),
									Integer.parseInt(msgParts[2]));
						} else if (msg.getContent().startsWith(EnvironmentAgent.REPRODUCE)) {
//							doReproduce(msg);
						} else {
							System.out.println("Mensagem inesperada (ambiente).");
						}

						break;
					case ACLMessage.ACCEPT_PROPOSAL:
						try {
							Object[] oMsg = (Object[]) msg.getContentObject();
							CreatureState creature = new CreatureState((AID) oMsg[3], null, (int) oMsg[0], (int) oMsg[1],
									(String) oMsg[2], null);

							envAgent.creaturePool.add(creature);

							if (envAgent.creaturePool.size() == envAgent.creaturesState.size()) {
								agentFoodTime(envAgent, creaturePool);
							}

							moveCreature(envAgent, creature.getId(), creature.getXPos(), creature.getYPos());

						} catch (UnreadableException e) {
							// Nao reconheci a mensagem.
							System.out.println("Nao consegui ler a posicao nova! (ambiente)");
							e.printStackTrace();
						}
						break;
					default:
						System.out.println("Formato inesperado (ambiente).");
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

	// Comportamento de Controle Temporal (Passagem dos dias)
	class IncrementDaysBehaviour extends Behaviour {

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
				System.out.println("Remover Sessao");
			}

			isDay = !isDay;
			if (isDay) {
				daysCount++;
				dayAlert();
			} else {
				nightAlert();
			}
		}

		public boolean done() {
			System.out.println((isDay ? "DIA " : "NOITE ") + daysCount);
			return false;
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

	public void startSimulation(int boardSize, List<SpecyState> speciesList, int creaturesPerSpecy, int foodAmount) {
		this.boardSize = boardSize;

		System.out.println("---------- INICIANDO SIMULACAO ----------");
		setUpFood(foodAmount);
		setUpCreaturesAgents(speciesList, creaturesPerSpecy);

		System.out.println("Inicia rotina de controle temporal (passagem dos dias)");
		Behaviour incDayBehaviour = new IncrementDaysBehaviour(this, 1000);
		addBehaviour(incDayThread.wrap(incDayBehaviour));
	}

	public void stopSimulation() {
		System.out.println("---------- PARANDO SIMULACAO ----------");
		sendBroadCast(ACLMessage.INFORM, DEAD);
		incDayThread.interrupt();
		mainWindow.clearBoard();
	}

	// Envia a mesma mensagem a todas as criaturas
	private void sendBroadCast(int type, String content) {
		for (CreatureState creature : this.creaturesState) {
			ACLMessage msg = new ACLMessage(type);

			msg.setContent(content);
			msg.addReceiver(creature.getId());
			send(msg);
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
		this.foodResources = new ArrayList<>();

		Food.createFoodResources(this.foodResources, foodAmount, 0, boardSize - 1);

		mainWindow.insertFood(foodResources, getImage("/food.png"));
	}

	private Image getImage(String path) {
		try {
			return ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setUpCreaturesAgents(List<SpecyState> speciesList, int creaturesPerSpecy) {

		for (int i = 0; i < speciesList.size(); i++) {
			Image speciesImage = getImage("/specy_" + (i + 1) + ".png");
			this.creaturesState.addAll(createCreatureAgents(speciesList.get(i).getName(),
					speciesList.get(i).getShareStrategy(), speciesImage, creaturesPerSpecy, 0, boardSize - 1));
		}

		mainWindow.insertSpecies(this.creaturesState);
	}

	private List<CreatureState> createCreatureAgents(String speciesName, String shareStrategy, Image speciesImage,
			int amount, int minPos, int maxPos) {
		PlatformController container = getContainerController();
		List<CreatureState> creaturesList = new ArrayList<>();

		try {
			for (int i = 0; i < amount; i++) {
				String creatureName = speciesName + "_" + i;
				int pos[] = CreatureState.getRandomPos(this.creaturesState, minPos, maxPos);

				AgentController creatureCtl = container.createNewAgent(creatureName,
						"simulation.creatures.CreatureAgent",
						new Object[] { pos[0], pos[1], shareStrategy, speciesName });
				creatureCtl.start();

				creaturesList.add(new CreatureState(new AID(creatureName, AID.ISLOCALNAME), speciesName, pos[0], pos[1],
						shareStrategy, speciesImage));
			}
		} catch (ControllerException e) {
			e.printStackTrace();
		}

		return creaturesList;
	}

	private void fulfillFoodSeekRequest(ACLMessage msg) {
		System.out.println(msg.getSender().getLocalName() + " requisita comida");

		if (randomFood == null || randomFood.isEmpty()) {
			randomFood = randomElementOneRepeat(foodResources);
		}
		Food newCoords = randomFood.get(0);
		randomFood.remove(0);
		ACLMessage coords = new ACLMessage(ACLMessage.PROPOSE);
		coords.setSender(getAID());
		coords.addReceiver(msg.getSender());
		try {
			Object[] oMsg = new Object[2];
			oMsg[0] = newCoords.getXPos();
			oMsg[1] = newCoords.getYPos();

			coords.setContentObject(oMsg);
		} catch (IOException ex) {
			System.err.println("Nao consegui reconhecer mensagem. Mandando mensagem vazia.");
			ex.printStackTrace(System.err);
		}
		send(coords);
	}

	private void moveCreature(EnvironmentAgent envAgent, AID creatureId, int xPos, int yPos) {
		for (int j = 0; j < envAgent.creaturesState.size(); j++) {
			if (envAgent.creaturesState.get(j).getId().equals(creatureId)) {
				envAgent.creaturesState.get(j).setPos(xPos, yPos);
			}
		}

		System.out.println("Nova posicao X: " + xPos + " Nova posicao Y: " + yPos);
	}

	private List<Food> randomElementOneRepeat(List<Food> foods) {
		List<Food> foodCopy = new ArrayList<>(foods);
		Random rand = new Random();
		List<Food> randomSequence = new ArrayList<>();
		int numberOfElements = foods.size();
		int repeat[] = new int[numberOfElements];

		for (int i = 0; i < numberOfElements; i++) {
			int randomIndex = rand.nextInt(foodCopy.size());
			Food randomElement = foodCopy.get(randomIndex);
			randomSequence.add(randomElement);
			repeat[randomIndex] += 1;
			if (repeat[randomIndex] > 1) {
				foodCopy.remove(randomIndex);
			}
		}
		return randomSequence;
	}

	private boolean checkDuplicates(CreatureState creature1, CreatureState creature2) {
		if (creature1.getXPos() == creature2.getXPos()) {
			if (creature1.getYPos() == creature2.getYPos()) {
				return true;
			}
		}
		return false;
	}

	private void compareStrategies(EnvironmentAgent envAgent, CreatureState creature1, CreatureState creature2) {
		String strat1 = creature1.getShareStrategy();
		String strat2 = creature2.getShareStrategy();

		System.out.println("COMPARAR ESTRATEGIAS");

		// Strat1 and strat2 are equal
		if (strat1.equals(strat2)) {
			switch (strat1) {
			case CreatureState.AGGRESSIVE:
				agentKill(envAgent, creature1);
				agentKill(envAgent, creature2);
				break;
			default:
			}
		} else { // Strat 1 and strat2 are not equal
			switch (strat1) {
			case CreatureState.FRIENDLY:
				agentChanceSurvive(envAgent, creature1, 0.5);
				agentChanceReproduce(envAgent, creature2, 0.5);
				break;
			case CreatureState.AGGRESSIVE:
				agentChanceSurvive(envAgent, creature2, 0.5);
				agentChanceReproduce(envAgent, creature1, 0.5);
				break;
			default:

			}
		}
	}

	// Handles what happens after all creatures have moved to eat.
	private void agentFoodTime(EnvironmentAgent envAgent, List<CreatureState> creaturePool) {
		if (creaturePool.size() > 1) {
			for (int i = 0; i < creaturePool.size(); i++) {
				for (int j = i + 1; j < creaturePool.size(); j++) {
					if (checkDuplicates(creaturePool.get(i), creaturePool.get(j))) {
						compareStrategies(envAgent, creaturePool.get(i), creaturePool.get(j));
					}
				}
			}
		} else {
			agentChanceReproduce(envAgent, creaturePool.get(0), 1);
		}

		creaturePool.clear();
	}

	private void agentKill(EnvironmentAgent envAgent, CreatureState creature) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(creature.getId());
		msg.setSender(envAgent.getAID());
		msg.setContent(EnvironmentAgent.DEAD);
		send(msg);

		for (int i = 0; i < this.creaturesState.size(); i++) {
			if (this.creaturesState.get(i).getId().equals(creature.getId())) {
				this.creaturesState.remove(i);
			}
		}
	}

	private void agentChanceReproduce(EnvironmentAgent envAgent, CreatureState creature, double chance) {
		if (chance > Math.random()) {
//			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//			msg.addReceiver(creature.getId());
//			msg.setSender(envAgent.getAID());
//			msg.setContent(EnvironmentAgent.REPRODUCE);
//			send(msg);
//
//			for (int i = 0; i < this.creaturesState.size(); i++) {
//				if (this.creaturesState.get(i).getId().equals(creature.getId())) {
//					CreatureState creatureFound = this.creaturesState.get(i);
//
//					createCreatureAgents(creatureFound.getSpeciesName(), creatureFound.getShareStrategy(),
//							creatureFound.getImage(), 1, 0, boardSize - 1);
//				}
//			}

		}
	}

	private void agentChanceSurvive(EnvironmentAgent envAgent, CreatureState creature, double chance) {
		if (chance < Math.random()) {
			agentKill(envAgent, creature);
		}
	}
}
