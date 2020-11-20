package simulation.resources;

import java.util.List;

import generic.board.item.BoardItem;

/*
 * Recurso para alimentar as criaturas
 */
public class Food extends BoardItem {
	
	// Construtor parametrizado
	public Food(Integer amount, int xPos, int yPos) {
		this.setXPos(xPos);
		this.setYPos(yPos);
	}
	
	public static void createFoodResources(List<Food> foodItems, int amount, int minPos, int maxPos) {
		for (int i = 0; i < amount; i++) {
			int pos[] = getRandomPos(foodItems, minPos, maxPos);
			foodItems.add(new Food(2, pos[0], pos[1]));
		}
	}

	// Gera a posicao aleatoria da comida
	// A comida nao deve iniciar nas bordas do board 
	private static int[] getRandomPos(List<Food> currentFoodList, int minPos, int maxPos) {
		int pos[];
		boolean repeated;
		do {
			repeated = false;
			pos = randomPos(minPos, maxPos);
			for (Food otherFood : currentFoodList) {
				if (otherFood.getXPos() == pos[0] && otherFood.getYPos() == pos[1]) {
					repeated = true;
					break;
				}
			}
		} while(repeated);
		return pos;
	}

	public static int[] randomPos(int minPos, int maxPos) {
		int xPos = 0, yPos = 0;
		while (true) {			
			xPos = (int) (Math.random() * ((maxPos + 1) - minPos) + minPos);
			yPos = (int) (Math.random() * ((maxPos + 1) - minPos) + minPos);
			
			if (!(isInBorder(xPos, minPos, maxPos) || isInBorder(yPos, minPos, maxPos))) {
				return new int[] {xPos, yPos};
			}
		}
	}
}
