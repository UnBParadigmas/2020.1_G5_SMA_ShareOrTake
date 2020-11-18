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
	private Creature creature;
	private ShareStrategyBehaviour shareStrategy;
	
	@Override
	protected void setup() {
		this.registerInDFD();
		
		creature = new Creature();
		shareStrategy = new ShareStrategyBehaviour();
				
		// Add the behaviour to move each loop
		addBehaviour(new MovementBehaviour());
		
		// Add the share strategy behaviour
		addBehaviour(shareStrategy);
		
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
	
	public Creature getCreature() {
		return this.creature;
	}
	
	public void setShareStrategy(ShareStrategyBehaviour shareStrategy) {
		this.shareStrategy = shareStrategy;
	}
}
