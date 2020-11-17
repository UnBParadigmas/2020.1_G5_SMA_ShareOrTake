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
		mainWindow = new MainWindow(new EnvironmentAgent(), 800, 800, BOARD_SIZE, BOARD_SIZE);
		mainWindow.setVisible(true);

		// Especies Adicionadas
		species.add(new Specy("Dove", behaviourTest()));
		species.add(new Specy("Evo", behaviourTest()));

		// Teste de colisao dos items no board
		createCreatures(species.get(0), 5, "/specy_1.png");
		createCreatures(species.get(1), 2, "/specy_4.png");
		
		createFoodResources(5, "/food.png");
	}

	private void createFoodResources(int amount, String pathImage) {
		for (int i = 0; i < amount; i++) {
			this.foodResources.add(this.getFood(new Food(2), 0, BOARD_SIZE - 1));
		}

		BoardItemGroup foodGroup = new BoardItemGroup(this.foodResources, pathImage, 0, BOARD_SIZE - 1);
		mainWindow.insertElementsGroup(foodGroup);
	}

	private Food getFood(Food food, int minPos, int maxPos) {
		setFoodRandomPos(food, minPos, maxPos);
		return food;
	}
	
	private void setFoodRandomPos(Food food, int minPos, int maxPos) {
		boolean repeated;
		do {
			repeated = false;
			food.randomPos(minPos, maxPos);
			for (Food otherFood : this.foodResources) {
				if (otherFood.getXPos() == food.getXPos() && otherFood.getYPos() == food.getYPos()) {
					repeated = true;
					break;
				}
			}
		} while(repeated);
	}

	private void createCreatures(Specy specy, int amount, String pathImage) {
		for (int i = 0; i < amount; i++) {
			specy.addCreature(getCreature(new Creature(), 0, BOARD_SIZE - 1));
		}

		BoardItemGroup creaturesGroup = new BoardItemGroup(specy.getCreatures(), pathImage, 0, BOARD_SIZE - 1);
		mainWindow.insertElementsGroup(creaturesGroup);
	}

	private Creature getCreature(Creature creature, int minPos, int maxPos) {
		setCreatureRandomPos(creature, minPos, maxPos);
		return creature;
	}

	// Escolhe uma posicao aleatoria dentro dos limites e sem conflito com as outras
	// criaturas
	private void setCreatureRandomPos(BoardItem item, int minPos, int maxPos) {
		boolean repeated;
		do {
			repeated = false;
			item.randomPos(minPos, maxPos);
			for (Specy specyGroup : species) {
				for (BoardItem otherCreature : specyGroup.getCreatures()) {
					if (otherCreature.getXPos() == item.getXPos() && otherCreature.getYPos() == item.getYPos()) {
						repeated = true;
						break;
					}
				}
				if (repeated)
					break;
			}
		} while (repeated);
	}
}
