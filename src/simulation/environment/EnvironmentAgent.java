package simulation.environment;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import generic.board.item.BoardItemGroup;
import graphics.MainWindow;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.lang.acl.ACLMessage;
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
	private static final long serialVersionUID = -6481631683157763680L;
	
	public final static String KILL = "KILL";

	private MainWindow mainWindow = null;
	
	int boardSize = 0;
	
	private List<Food> foodResources = new ArrayList<>();
	private List<SpecyState> speciesState = new ArrayList<>();

	@Override
	protected void setup() {
		System.out.println("Iniciando " + getLocalName());

		this.registerInDFD();
		this.setUpUI();
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
			createCreatureAgents(this.speciesState, i, creaturesPerSpecy, 0, boardSize - 1);
			creaturesGroup = new BoardItemGroup(this.speciesState.get(i).getCreaturesState(),
					this.speciesState.get(i).getImagePath());
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
