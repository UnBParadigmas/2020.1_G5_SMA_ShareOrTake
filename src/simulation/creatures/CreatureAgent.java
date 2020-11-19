package simulation.creatures;

import jade.core.Agent;

import java.io.IOException;
import java.io.Serializable;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import simulation.environment.*;

/**
 * Agente que representa uma creatura generica
 */
public class CreatureAgent extends Agent {
	// Constantes
    public final static String FRIENDLY = "FRIENDLY";
    public final static String AGGRESSIVE = "AGGRESIVE";
	private static final long serialVersionUID = 5935364544929084407L;
	
	private int xPos;
	private int yPos;
	private boolean alive;
	private String shareStrategy;
	
	@Override
	protected void setup() {
		try {
			this.xPos = (int) this.getArguments()[0];
			this.yPos = (int) this.getArguments()[1];
			this.shareStrategy  = (String) this.getArguments()[2];
			this.alive = true;
			
			System.out.println("Criando criatura " + getLocalName());
			System.out.println("POS X: " + this.xPos + ", POS Y: " + this.yPos);
			System.out.println("Estrategia: " + this.shareStrategy);
		
			this.registerInDFD();
		
			// Notificar board que a criatura est� aqui
			ACLMessage hello = new ACLMessage(ACLMessage.INFORM);
			hello.setContent(EnvironmentAgent.HELLO);
			hello.addReceiver(new AID("environment", AID.ISLOCALNAME));
			send(hello);
			
			// Adicionar behaviour para mover quando chamado
			addBehaviour(new CyclicBehaviour(this){
				private static final long serialVersionUID = 1L;

				public void action() {
					ACLMessage msg = receive();
					CreatureAgent a = (CreatureAgent) myAgent;
					if (msg != null) {
						
						switch (msg.getPerformative()) {
							case ACLMessage.INFORM:
								
								switch (msg.getContent()) {
									case EnvironmentAgent.SHARE:
										ACLMessage share = new ACLMessage(ACLMessage.PROPOSE);
										share.setSender(a.getAID());
									     try {
									         Object[] oMsg = new Object[4];
									         oMsg[0] = a.alive;
									         oMsg[1] = a.xPos;
									         oMsg[2] = a.yPos;
									         oMsg[3] = a.shareStrategy;
									         
									         share.setContentObject(oMsg);
									     } catch (IOException ex) {
									         System.err.println("N�o consegui reconhecer mensagem. Mandando mensagem vazia.");
									         ex.printStackTrace(System.err);
									     }
									    share.addReceiver(msg.getSender());
										send(share);
										break;
									case EnvironmentAgent.DEAD:
										kill();
										ACLMessage dead = new ACLMessage(ACLMessage.INFORM);
										dead.setContent(EnvironmentAgent.DEAD);
										dead.setSender(a.getAID());
										dead.addReceiver(msg.getSender());
										send(dead);
										takeDown();
										break;
									default:
										System.out.println("Mensagem inesperada.");
								}	
								
								break;
								
							case ACLMessage.PROPOSE:
								try {
									Object[] oMsg = (Object []) msg.getContentObject();
									a.xPos = (int) oMsg[1];
									a.yPos = (int) oMsg[2];
									System.out.println("Nova posicao X: " + a.xPos + " Nova posicao Y: " + a.yPos);
									System.out.println("Comida disponivel: "+ oMsg[0]);
									 
								} catch (UnreadableException e) {
									// N�o reconheci a mensagem.
									e.printStackTrace();
								}
								break;
						}
								
					} else {
						// Se n�o houver mensagem, bloquear behaviour.
						block();
					}
				}
			});
		} catch (Exception e){
			System.out.println( "Exce��o em " + e );
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
	
	public void setShareStrategy(String shareStrategy) {
		this.shareStrategy = shareStrategy;
	}
	
	public void kill() {
		this.alive = false;
	}
}
