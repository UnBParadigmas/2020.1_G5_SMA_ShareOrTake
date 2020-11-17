package simulation.resources;

import java.util.Arrays;
import java.util.List;

import generic.board.item.BoardItem;

/*
 * Recurso para alimentar as criaturas
 */
public class Food extends BoardItem {
	
	private String name;
	private Integer foodAmount;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getFoodAmount() {
		return foodAmount;
	}
	
	public void setFoodAmount(Integer foodAmount) {
		this.foodAmount = foodAmount;
	}
}
