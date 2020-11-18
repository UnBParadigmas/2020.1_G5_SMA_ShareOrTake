package simulation.creatures;

import jade.core.*;

/**
 * Agente que representa uma creatura generica
 */
public class CreatureAgent extends Agent {
	private static final long serialVersionUID = 5935364544929084407L;
	
	private int xPos;
	private int yPos;
	private boolean alive;
	
	@Override
	protected void setup() {
		System.out.println("Criando criatura " + getLocalName());
		
		System.out.println("ARG 1: " + this.getArguments()[0]);
		this.getArguments();
	}
	
	@Override
	protected void takeDown() {
	}
}
