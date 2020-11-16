package simulation.creatures;

import jade.core.behaviours.Behaviour;

/*
 * Caracteristicas da criatura
 */
public class Creature extends Species{
	
	// Retorna se a criatura está viva.
	public void getStatus(){
		return this.alive;
	}

	// Muda o status da criatura.
	public void setStatus(boolean alive){
		this.alive = status;
	}

	// Construtor parametrizado
	public Creature (Integer name, Behaviour strategy){
		super(name, 1, strategy);
		this.alive = true;
	}
}
