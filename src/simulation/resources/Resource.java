package simulation.resources;

import java.util.Arrays;
import java.util.List;

/*
 * Recurso para alimentar as criaturas
 */
public class Resource {
	
	private String name;
	private Integer foodAmount;
	private Integer xPos;
	private Integer yPos;
	
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
	
	public void setPos(Integer Xpos, Integer Ypos) {
		this.xPos = Xpos;
		this.yPos = Ypos;
	}
	
	public List<Integer> getPos() {
		return Arrays.asList(this.xPos, this.yPos);
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
}
