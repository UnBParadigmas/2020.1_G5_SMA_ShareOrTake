package simulation.environment;

import java.util.ArrayList;
import java.util.List;

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
				
				if(msg != null) {
					EnvironmentAgent a = (EnvironmentAgent) myAgent;
					
					switch (msg.getContent()) {
						case EnvironmentAgent.HELLO:
							// Hello
							System.out.println("Amigo estou aqui");
							break;
						default:
							System.out.println("Mensagem inesperada.");
					
					}
				} else {
					// Se n�o houver mensagem, bloquear behaviour.
					block();
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

		createCreatureAgents(this.speciesState, 0, 7, 0, BOARD_SIZE - 1);
		createCreatureAgents(this.speciesState, 1, 2, 0, BOARD_SIZE - 1);

		for (int i = 0; i < this.speciesState.size(); i++) {
			creaturesGroup = new BoardItemGroup(this.speciesState.get(i).getCreaturesState(),
					this.speciesState.get(i).getImagePath(), 0, BOARD_SIZE - 1);
			mainWindow.insertElementsGroup(creaturesGroup);
		}
	}

	private void createCreatureAgents(List<SpecyState> speciesState, int specyIndex, int amount, int minPos, int maxPos) {
		PlatformController container = getContainerController();

		try {
			for (int i = 0; i < amount; i++) {
				String creatureName = speciesState.get(specyIndex).getName() + "_" + i;
				int pos[] = CreatureState.getRandomPos(speciesState, minPos, maxPos);
				
				AgentController creatureCtl = container.createNewAgent(creatureName,
						"simulation.creatures.CreatureAgent", new Object[] {pos[0], pos[1]});
				creatureCtl.start();


				speciesState.get(specyIndex)
						.addCreatureState(new CreatureState(new AID(creatureName, AID.ISLOCALNAME), pos[0], pos[1]));
			}
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
}
