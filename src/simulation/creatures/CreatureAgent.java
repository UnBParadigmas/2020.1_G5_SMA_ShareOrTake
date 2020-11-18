package simulation.creatures;

import jade.core.Agent;
import jade.core.AID;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import simulation.behaviours.*;
import simulation.environment.*;

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
		try {
			this.xPos = (int) this.getArguments()[0];
			this.yPos = (int) this.getArguments()[1];
			this.alive = true;
			
			System.out.println("Criando criatura " + getLocalName());
			
			System.out.println("POS X: " + this.xPos + ", POS Y: " + this.yPos);
		
			this.registerInDFD();
		
			// Notificar board que a criatura está aqui
			ACLMessage hello = new ACLMessage(ACLMessage.INFORM);
			hello.setContent(EnvironmentAgent.HELLO);
			hello.addReceiver(new AID("host", AID.ISLOCALNAME));
			
			// Add the behaviour to move each loop
			addBehaviour(new MovementBehaviour());
			
			// Add the share strategy behaviour
			addBehaviour(new ShareStrategyBehaviour());
			
			// Add the behaviour to sleep each loop
			addBehaviour(new SleepBehaviour());
		} catch (Exception e){
			System.out.println( "Exceção em " + e );
            e.printStackTrace();
		}
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
