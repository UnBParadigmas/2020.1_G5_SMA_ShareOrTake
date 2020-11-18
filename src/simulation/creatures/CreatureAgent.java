package simulation.creatures;

import jade.core.Agent;
import simulation.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

/**
 * Agente que representa uma creatura generica
 */
public class CreatureAgent extends Agent {
	private static final long serialVersionUID = 5935364544929084407L;
	
	private ShareStrategyBehaviour shareStrategy;

	private int xPos;
	private int yPos;
	private boolean alive;
	
	@Override
	protected void setup() {
		this.xPos = (int) this.getArguments()[0];
		this.yPos = (int) this.getArguments()[1];
		this.alive = true;
		
		System.out.println("Criando criatura " + getLocalName());
		
		System.out.println("POS X: " + this.xPos + ", POS Y: " + this.yPos);

		this.registerInDFD();
		
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
	
	public void setShareStrategy(ShareStrategyBehaviour shareStrategy) {
		this.shareStrategy = shareStrategy;
	}
}
