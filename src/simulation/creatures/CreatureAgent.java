package simulation.creatures;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;

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
			
			// Adicionar behaviour para mover quando chamado
			addBehaviour(new CyclicBehaviour(this){
				private static final long serialVersionUID = 1L;

				public void action() {
					ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					
					if (msg != null) {
						switch (msg.getContent()) {
							case EnvironmentAgent.MOVE:
								// Move here
								break;
							case EnvironmentAgent.GOBACK:
								// Go back here
								break;
							case EnvironmentAgent.SHARE:
								// Share here
								break;
							case EnvironmentAgent.SLEEP:
								// Sleep here
								break;
							case EnvironmentAgent.DIE:
								// Die here
								break;
							default:
								System.out.println("Mensagem inesperada.");
						}
					} else {
						// Se não houver mensagem, bloquear behaviour.
						block();
					}
				}
			});
			
			// Add the behaviour to move each loop
			// addBehaviour(new MovementBehaviour());
			
			// Add the share strategy behaviour
			// addBehaviour(new ShareStrategyBehaviour());
			
			// Add the behaviour to sleep each loop
			// addBehaviour(new SleepBehaviour());
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
