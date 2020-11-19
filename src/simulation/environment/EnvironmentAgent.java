package simulation.environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import generic.board.item.BoardItemGroup;
import graphics.MainWindow;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import simulation.creatures.CreatureAgent;
import simulation.creatures.CreatureState;
import simulation.creatures.SpecyState;
import simulation.resources.Food;

/**
 * Agente que controla o meio ambiente em que as criaturas estao inseridas
 */
public class EnvironmentAgent extends Agent {
	// Constantes
	private static final long serialVersionUID = -6481631683157763680L;
	private static final int BOARD_SIZE = 10;
    public final static String HELLO = "HELLO";
    public final static String SHARE = "SHARE";
    public final static String GOBACK = "GOBACK";
    public final static String DEAD = "DEAD";
    public final static String MOVE = "MOVE";

    
	private MainWindow mainWindow = null;

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
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				ACLMessage rqst = receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
				EnvironmentAgent a = (EnvironmentAgent) myAgent;
				if(msg != null) {	
					switch (msg.getContent()) {
						case EnvironmentAgent.HELLO:
							// Hello
							System.out.println("Amigo estou aqui");
							ACLMessage move = new ACLMessage(ACLMessage.INFORM);
							move.setContent(EnvironmentAgent.MOVE);
							move.setSender(a.getAID());
							move.addReceiver(msg.getSender());
						case EnvironmentAgent.SHARE: 
							break;
						default:
							System.out.println("Mensagem inesperada.");
					}
				} else {
					if(rqst != null) {
						if(a.randomFood == null) {
							a.randomFood = randomElementOneRepeat(a.foodResources);
						}
						Food newCoords = a.randomFood.get(0);
						a.randomFood.remove(0);
						ACLMessage coords = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
						coords.setSender(a.getAID());
						coords.addReceiver(rqst.getSender());
						try {
					         Object[] oMsg = new Object[3];
					         oMsg[0] = newCoords.getFoodAmount();
					         oMsg[1] = newCoords.getXPos();
					         oMsg[2] = newCoords.getYPos();
					         
					         coords.setContentObject(oMsg);
					     } catch (IOException ex) {
					         System.err.println("Não consegui reconhecer mensagem. Mandando mensagem vazia.");
					         ex.printStackTrace(System.err);
					     }	
						send(coords);
					} else {
						// Se não houver mensagem nem request, bloquear behaviour.
						block();
					}
				}
			}
		});
	}

	@Override
	protected void takeDown() {
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
		mainWindow = new MainWindow(new EnvironmentAgent(), 800, 800, BOARD_SIZE, BOARD_SIZE);
		mainWindow.setVisible(true);
		
		setUpFood();
		setUpCreaturesAgents();
		this.randomFood = randomElementOneRepeat(this.foodResources); 
	}
	
	private void setUpFood() {
		BoardItemGroup foodGroup;

		Food.createFoodResources(this.foodResources, 5, 0, BOARD_SIZE - 1);
		foodGroup = new BoardItemGroup(this.foodResources, "/food.png", 0, BOARD_SIZE - 1);

		mainWindow.insertElementsGroup(foodGroup);
	}

	private void setUpCreaturesAgents() {
		BoardItemGroup creaturesGroup;

		// Especies Adicionadas
		this.speciesState.add(new SpecyState("Dove", "/specy_1.png"));
		this.speciesState.add(new SpecyState("Evo", "/specy_5.png"));

		createCreatureAgents(this.speciesState, 0, 7, 0, BOARD_SIZE - 1, CreatureState.FRIENDLY);
		createCreatureAgents(this.speciesState, 1, 2, 0, BOARD_SIZE - 1, CreatureState.AGGRESSIVE);

		for (int i = 0; i < this.speciesState.size(); i++) {
			creaturesGroup = new BoardItemGroup(this.speciesState.get(i).getCreaturesState(),
					this.speciesState.get(i).getImagePath(), 0, BOARD_SIZE - 1);
			mainWindow.insertElementsGroup(creaturesGroup);
		}
	}

	private void createCreatureAgents(List<SpecyState> speciesState, int specyIndex, int amount, int minPos, int maxPos, String shareStrategy) {
		PlatformController container = getContainerController();

		try {
			for (int i = 0; i < amount; i++) {
				String creatureName = speciesState.get(specyIndex).getName() + "_" + i;
				int pos[] = CreatureState.getRandomPos(speciesState, minPos, maxPos);
				
				AgentController creatureCtl = container.createNewAgent(creatureName,
						"simulation.creatures.CreatureAgent", new Object[] {pos[0], pos[1], shareStrategy});
				creatureCtl.start();


				speciesState.get(specyIndex)
						.addCreatureState(new CreatureState(new AID(creatureName, AID.ISLOCALNAME), pos[0], pos[1], shareStrategy));
			}
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	private List<Food> randomElementOneRepeat(List<Food> foods) {
	    List<Food> foodCopy = new ArrayList<>(foods);	    
	    Random rand = new Random();
	    List<Food> randomSequence = new ArrayList<>();
	 	int numberOfElements = foods.size();
	 	boolean repeat [] = null;
	 	
	    for (int i = 0; i < numberOfElements; i++) {
	        int randomIndex = rand.nextInt(foodCopy.size());
	        Food randomElement = foodCopy.get(randomIndex);
	        randomSequence.add(randomElement);
	        repeat[randomIndex] = true;
	        if(repeat[randomIndex] == true) {
	        	foodCopy.remove(randomIndex);
	        }
	    }
	    return randomSequence;
	}
}
