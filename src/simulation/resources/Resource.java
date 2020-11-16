package simulation.resources;

import java.util.Arrays;
import java.util.List;
import java.lang.Math.random;

/*
 * Recurso para alimentar as criaturas
 */
public class Resource {
	
	private String name;
	private Integer foodAmount;
	private Integer Xpos;
	private Integer Ypos;
	
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
	
	// Muda a posição para os valores fornecidos como argumento.
	public void setPos(Integer Xpos, Integer Ypos) {
		// Muda
		this.Xpos = Xpos;
		this.Ypos = Ypos;
	}

	// Muda a posição do recurso para um valor aleatório dentro dos limites.
	public void randompos() {
		// Valores máximos e minimos para posições X e Y de um recurso.
		int max = 100;
		int min = 0;

		this.Xpos = (Math.random() * (max - min) + min);
		this.Ypos = (Math.random() * (max - min) + min);
	}
	
	// Retorna a posição atual do recurso.
	public List<Integer> getPos() {
		return Arrays.asList(this.Xpos, this.Ypos);
	}

	// Construtor parametrizado
	public Resource(String name, Integer amount) {
		this.name = name;
		this.foodAmount = amount;
		this.randompos();
	}
}
