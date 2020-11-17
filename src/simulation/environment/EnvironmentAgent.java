package simulation.environment;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import generic.board.item.BoardItem;
import generic.board.item.BoardItemGroup;
import graphics.MainWindow;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import simulation.creatures.Creature;
import simulation.creatures.Specy;
import simulation.resources.Food;

/**
 * Agente que controla o meio ambiente em que as criaturas estao inseridas
 */
public class EnvironmentAgent extends Agent {
	private static final long serialVersionUID = -6481631683157763680L;

	private static final int BOARD_SIZE = 10;

	private MainWindow mainWindow = null;
	
	private List<Specy> species = new ArrayList<>();
	private List<Food> foodResources = new ArrayList<>();

	@Override
	protected void setup() {
		System.out.println("Iniciando " + getLocalName());

		this.registerInDFD();
		this.setUpUI();
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
	
	private Behaviour behaviourTest() {
		return new Behaviour() {
			
			@Override
			public boolean done() {
				return false;
			}
			
			@Override
			public void action() {
			}
		};
	}

	private void setUpUI() {
		mainWindow = new MainWindow(new EnvironmentAgent(), 400, 400, BOARD_SIZE, BOARD_SIZE);
		mainWindow.setVisible(true);
		
		// Especies Adicionadas
		species.add(new Specy("Dove", behaviourTest()));

		for (int i = 0; i < 10; i++) {
			species.get(0).addCreature(new Creature());
			foodResources.add(new Food(2));
		}
		
		// Teste de colisao dos items no board
		BoardItemGroup foodGroup = new BoardItemGroup(foodResources, "/food.png", 0, BOARD_SIZE - 1);
		BoardItemGroup creaturesGroup = new BoardItemGroup(species.get(0).getCreatures(), "/specy_4.png", 0, BOARD_SIZE - 1);
		
		mainWindow.insertElementsGroup(foodGroup);
		mainWindow.insertElementsGroup(creaturesGroup);
	}
}
