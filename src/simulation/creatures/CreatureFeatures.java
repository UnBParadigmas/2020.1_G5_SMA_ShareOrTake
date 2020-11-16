package simulation.creatures;

import jade.core.behaviours.Behaviour;

/*
 * Caracteristicas da criatura
 */
public class CreatureFeatures {
	
	private String name;
	private Long creatureCount;
	private Behaviour shareStrategy;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getCreatureCount() {
		return creatureCount;
	}
	
	public void setCreatureCount(Long creatureCount) {
		this.creatureCount = creatureCount;
	}
	
	public Behaviour getShareStrategy() {
		return shareStrategy;
	}
	
	public void setShareStrategy(Behaviour shareStrategy) {
		this.shareStrategy = shareStrategy;
	}
}
