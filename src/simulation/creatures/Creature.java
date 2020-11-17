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
	
	// Gera a posicao aleatoria da criatura
	// A criatura so deve iniciar nas bordas do board 
	@Override
	public void randomPos(int minPos, int maxPos) {
		while (true) {	
			this.setXPos((int) (Math.random() * ((maxPos + 1) - minPos) + minPos));
			this.setYPos((int) (Math.random() * ((maxPos + 1) - minPos) + minPos));
		
			if (isInBorder(this.getXPos(), minPos, maxPos) || isInBorder(this.getYPos(), minPos, maxPos)) {
				return;
			}
		}
	}
}
