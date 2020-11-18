package simulation.creatures;

import jade.core.Agent;
import jade.core.behaviours.*;
import simulation.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;


/**
 * Agente que representa uma creatura generica
 */
public class CreatureAgent extends Agent {
	private static final long serialVersionUID = 5935364544929084407L;
	private String name;
	private Behaviour shareStrategy;
	private int startingPop;
		
	@Override
	protected void setup() {
		this.registerInDFD();
		
		Specy species = new Specy(this.name, this.shareStrategy);
		for(int counter = 0;counter < startingPop;counter++) {
			Creature tempCreature = new Creature();
			species.addCreature(tempCreature);
		}
		
		// Add the behaviour to move each loop
		addBehaviour(new MovementBehaviour());
		
		// Add the share strategy behaviour
		addBehaviour(new ShareStrategyBehaviour());
		
		// Add the behaviour to sleep each loop
		addBehaviour(new SleepBehaviour());
		
	}
	
	@Override
	protected void takeDown() {
	  try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
	}
	
	private void registerInDFD() {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setSpecies(String name, Behaviour shareStrategy, int startingPop) {
		this.name = name;
		this.shareStrategy = shareStrategy;
		this.startingPop = startingPop;
	}
}
