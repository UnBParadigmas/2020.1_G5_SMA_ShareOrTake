package simulation.resources;

import generic.board.item.BoardItem;

/*
 * Recurso para alimentar as criaturas
 */
public class Food extends BoardItem {
	
	private Integer foodAmount;
	
	// Construtor parametrizado
	public Food(Integer amount) {
		this.foodAmount = amount;
	}
	
	// Retorna quantia atual de comida.
	public Integer getFoodAmount() {
		return foodAmount;
	}

	// Muda a quantia de um recurso para o argumento recebido.
	public void setFoodAmount(Integer foodAmount) {
		this.foodAmount = foodAmount;
	}

	// Adiciona a quantia igual ao argumento recebido ao valor atual de quantia.
	public void addFoodAmount(Integer foodAmount) {
		this.foodAmount += foodAmount;
	}
	
	// Gera a posicao aleatoria da comida
	// A comida nao deve iniciar nas bordas do board 
	@Override
	public void randomPos(int minPos, int maxPos) {
		while (true) {			
			this.setXPos((int) (Math.random() * ((maxPos + 1) - minPos) + minPos));
			this.setYPos((int) (Math.random() * ((maxPos + 1) - minPos) + minPos));
			
			if (!(isInBorder(this.getXPos(), minPos, maxPos) || isInBorder(this.getYPos(), minPos, maxPos))) {
				return;
			}
		}
	}
}
