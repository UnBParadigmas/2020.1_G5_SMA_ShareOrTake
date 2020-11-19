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
import jade.lang.acl.UnreadableException;
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

	public final static String HELLO = "HELLO";
	public final static String SHARE = "SHARE";
	public final static String GOBACK = "GOBACK";
	public final static String DEAD = "DEAD";
	public final static String MOVE = "MOVE";
	public final static String REPRODUCE = "REPRODUCE";

	private MainWindow mainWindow = null;

	int boardSize = 0;

	private List<Food> foodResources = new ArrayList<>();
	private List<Food> randomFood = new ArrayList<>();
	private List<SpecyState> speciesState = new ArrayList<>();
	private List<CreatureState> creaturePool = new ArrayList<>();
	private int totalIterations = 0;
	private int currentIteration = 0;

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
							if (envAgent.randomFood == null || envAgent.randomFood.isEmpty()) {
								envAgent.randomFood = randomElementOneRepeat(envAgent.foodResources);
							}
							Food newCoords = envAgent.randomFood.get(0);
							envAgent.randomFood.remove(0);
							ACLMessage coords = new ACLMessage(ACLMessage.PROPOSE);
							coords.setSender(envAgent.getAID());
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
						default:
							System.out.println("Mensagem inesperada (ambiente).");
						}

						break;
					case ACLMessage.ACCEPT_PROPOSAL:
						try {
							Object[] oMsg = (Object []) msg.getContentObject();
							CreatureState creature = new CreatureState((AID) oMsg[3],
																	   (int) oMsg[0],
																	   (int) oMsg[1],
																	   (String) oMsg[2]);
							envAgent.creaturePool.add(creature);
							envAgent.currentIteration-= 1;
							if(envAgent.currentIteration == 0) {
								agentFoodTime(envAgent, creaturePool);
							}
						} catch (UnreadableException e) {
							// Nao reconheci a mensagem.
							System.out.println("Não consegui ler a posição nova!");
							e.printStackTrace();
						}
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

		System.out.println("---------- INICIANDO SIMULACAO ----------");
		setUpFood(foodAmount);
		setUpCreaturesAgents(species, creaturesPerSpecy);
	}
	
	public void stopSimulation() {		
		resetStates();
		System.out.println("---------- PARANDO SIMULACAO ----------");
	}

	private void resetStates() {
		mainWindow.clearBoard();

		for (SpecyState specy : speciesState) {
			for (CreatureState creature : specy.getCreaturesState()) {
				ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
				
				msg.setContent(DEAD);
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
//		this.speciesState.add(new SpecyState("Dove", CreatureState.FRIENDLY, "/specy_1.png"));
//		this.speciesState.add(new SpecyState("Evo", CreatureState.AGGRESSIVE, "/specy_5.png"));

		for (int i = 0; i < this.speciesState.size(); i++) {
			createCreatureAgents(this.speciesState, i, creaturesPerSpecy, 0, boardSize - 1);
			creaturesGroup = new BoardItemGroup(this.speciesState.get(i).getCreaturesState(),
					this.speciesState.get(i).getImagePath());
			mainWindow.insertElementsGroup(creaturesGroup);
			this.totalIterations+= 1;
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
						new Object[] { pos[0], 
									   pos[1], 
									   species.get(specyIndex).getShareStrategy(),});
				creatureCtl.start();

				species.get(specyIndex).addCreatureState(new CreatureState(new AID(creatureName, AID.ISLOCALNAME),
						pos[0], pos[1], species.get(specyIndex).getShareStrategy()));
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
		if(creature1.getXPos() == creature2.getXPos()) {
			if(creature1.getYPos() == creature2.getYPos()) {
				return true;
			}
		}
		return false;
	}
	
	private void compareStrategies(EnvironmentAgent envAgent, CreatureState creature1, CreatureState creature2) {
		String strat1 = creature1.getShareStrategy();
		String strat2 = creature2.getShareStrategy();
		
		// Strat1 and strat2 are equal
		if(strat1.equals(strat2)) {
			switch (strat1) {
				case CreatureState.FRIENDLY:
					agentGoBack(envAgent, creature1);
					agentGoBack(envAgent, creature2);
					break;
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
		int total = envAgent.totalIterations;
		for(int i = 0; i < total; i++) {
			for(int j = i+1; j < total; i++) {
				if(checkDuplicates(creaturePool.get(i), creaturePool.get(j))) {
					compareStrategies(envAgent, creaturePool.get(i), creaturePool.get(j));
				}else if(j == (total-1)) {
					agentGoBack(envAgent, creaturePool.get(i));
				}
			}
		}
		creaturePool.clear();
		envAgent.currentIteration = envAgent.totalIterations;
	}
	
	private void agentGoBack(EnvironmentAgent envAgent, CreatureState creature) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(creature.getId());
		msg.setSender(envAgent.getAID());
		msg.setContent(EnvironmentAgent.GOBACK);
		send(msg);
	}
	
	private void agentKill(EnvironmentAgent envAgent, CreatureState creature) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(creature.getId());
		msg.setSender(envAgent.getAID());
		msg.setContent(EnvironmentAgent.DEAD);
		envAgent.totalIterations-=1;
		send(msg);
	}
	
	private void agentChanceReproduce(EnvironmentAgent envAgent, CreatureState creature, double chance) {
		if(Math.random() < chance) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(creature.getId());
			msg.setSender(envAgent.getAID());
			msg.setContent(EnvironmentAgent.REPRODUCE);
			envAgent.totalIterations+=1;
			send(msg);
		} else {
			agentGoBack(envAgent, creature);
		}
	}
	
	private void agentChanceSurvive(EnvironmentAgent envAgent, CreatureState creature, double chance) {
		if(Math.random() < chance) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(creature.getId());
			msg.setSender(envAgent.getAID());
			msg.setContent(EnvironmentAgent.DEAD);
			envAgent.totalIterations-=1;
			send(msg);
		} else {
			agentGoBack(envAgent, creature);
		}
	}
}
