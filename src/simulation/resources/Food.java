package simulation.resources;

import generic.board.item.BoardItem;

/*
 * Recurso para alimentar as criaturas
 */
public class Food extends BoardItem {
	
	private static final int MAX_POS = 100;
	private static final int MIN_POS = 0;
	
	private String name;
	private Integer foodAmount;
	
	// Construtor parametrizado
	public Food(String name, Integer amount) {
		this.name = name;
		this.foodAmount = amount;
		this.randomPos();
	}
	
	// Retorna o nome atual do recurso.
	public String getName() {
		// Retorna o nome atual do recurso.
		return name;
	}
	
	// Muda o nome do recurso para o argumento.
	public void setName(String name) {
		this.name = name;
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
	
	// Muda a posição do recurso para um valor aleatório dentro dos limites.
	public void randomPos() {
		// Valores máximos e minimos para posições X e Y de um recurso.
		this.setXPos((int) Math.random() * (MAX_POS - MIN_POS) + MIN_POS);
		this.setYPos((int) Math.random() * (MAX_POS - MIN_POS) + MIN_POS);
	}
}
