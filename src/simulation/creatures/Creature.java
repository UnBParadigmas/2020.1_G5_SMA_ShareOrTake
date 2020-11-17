package simulation.creatures;

import generic.board.item.BoardItem;
import jade.core.behaviours.Behaviour;

/*
 * Caracteristicas da criatura
 */
public class Creature extends BoardItem{
	
	private boolean alive;
	
	// Construtor parametrizado
	public Creature (){
		this.alive = true;
	}

	// Retorna se a criatura está viva.
	public boolean getStatus(){
		return this.alive;
	}

	// Muda o status da criatura.
	public void setStatus(boolean alive){
		this.alive = alive;
	}
}
