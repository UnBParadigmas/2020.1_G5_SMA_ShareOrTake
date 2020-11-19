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
import jade.core.behaviours.CyclicBehaviour;
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

	public final static String KILL = "KILL";
    public final static String HELLO = "HELLO";
    public final static String SHARE = "SHARE";
    public final static String GOBACK = "GOBACK";
    public final static String DEAD = "DEAD";
    public final static String MOVE = "MOVE";
    
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
				EnvironmentAgent envAgent = (EnvironmentAgent) this.myAgent;
				
				if (msg != null) {
					switch (msg.getPerformative()) {
						case ACLMessage.INFORM:
							
							switch (msg.getContent()) {
								case EnvironmentAgent.HELLO:
									// Hello
									System.out.println("Amigo estou aqui");
									if(envAgent.randomFood == null || envAgent.randomFood.isEmpty()) {
										envAgent.randomFood = randomElementOneRepeat(envAgent.foodResources);
									}
									Food newCoords = envAgent.randomFood.get(0);
									envAgent.randomFood.remove(0);
									ACLMessage coords = new ACLMessage(ACLMessage.PROPOSE);
									coords.setSender(envAgent.getAID());
									coords.addReceiver(msg.getSender());
									try {
									     Object[] oMsg = new Object[3];
									     oMsg[0] = newCoords.getFoodAmount();
									     oMsg[1] = newCoords.getXPos();
									     oMsg[2] = newCoords.getYPos();
									     
									     coords.setContentObject(oMsg);
									 } catch (IOException ex) {
									     System.err.println("N�o consegui reconhecer mensagem. Mandando mensagem vazia.");
									     ex.printStackTrace(System.err);
									 }	
									send(coords);
								default:
									System.out.println("Mensagem inesperada.");
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
	
	public void startSimulation(int boardSize, List<SpecyState> species, int creaturesPerSpecy, int foodAmount) {
		this.boardSize = boardSize;
		
		resetState();
		setUpFood(foodAmount);
		setUpCreaturesAgents(species, creaturesPerSpecy);
	}
	
	private void resetState() {		
		mainWindow.clearBoard();
		
		for (SpecyState specy : speciesState) {
			for (CreatureState creature : specy.getCreaturesState()) {
				ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
				
				msg.setContent( KILL );
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
//		this.speciesState.add(new SpecyState("Dove", "/specy_1.png"));
//		this.speciesState.add(new SpecyState("Evo", "/specy_5.png"));

		for (int i = 0; i < this.speciesState.size(); i++) {
			createCreatureAgents(this.speciesState, i, creaturesPerSpecy, 0, boardSize - 1, CreatureState.FRIENDLY);
			creaturesGroup = new BoardItemGroup(this.speciesState.get(i).getCreaturesState(),
					this.speciesState.get(i).getImagePath());
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
	 	Boolean repeat [] = new Boolean[numberOfElements];
	 	
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
