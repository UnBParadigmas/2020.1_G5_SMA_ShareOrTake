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
	
	private AID creatureId;
	private int xPos;
	private int yPos;
	private int xPosOld;
	private int yPosOld;
	private boolean alive;
	private String shareStrategy;
	
	@Override
	protected void setup() {
		try {
			this.xPos = (int) this.getArguments()[0];
			this.yPos = (int) this.getArguments()[1];
			this.shareStrategy  = (String) this.getArguments()[2];
			this.creatureId = (AID) this.getArguments()[3];
			this.alive = true;
			
			System.out.println("Criando criatura " + getLocalName());
			System.out.println("POS X: " + this.xPos + ", POS Y: " + this.yPos);
			System.out.println("Estrategia: " + this.shareStrategy);
		
			this.registerInDFD();
		
			// Notificar board que a criatura estï¿½ aqui
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
									case EnvironmentAgent.DEAD:
										kill();
										doDelete();
										break;
									default:
										System.out.println("Mensagem inesperada (creature).");
								}	
								
								break;
								
							case ACLMessage.PROPOSE:
								try {
									Object[] oMsg = (Object []) msg.getContentObject();
									a.xPosOld = a.xPos;
									a.yPosOld = a.yPos;
									a.xPos = (int) oMsg[1];
									a.yPos = (int) oMsg[2];
									
									System.out.println("Nova posicao X: " + a.xPos + " Nova posicao Y: " + a.yPos);
									System.out.println("Comida disponivel: "+ oMsg[0]);
									
									
									 
								} catch (UnreadableException e) {
									// Nao reconheci a mensagem.
									System.out.println("Não consegui ler a posição nova!");
									e.printStackTrace();
								}
								break;
							case ACLMessage.ACCEPT_PROPOSAL:
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
							         System.err.println("Nao consegui reconhecer mensagem. Mandando mensagem vazia.");
							         ex.printStackTrace(System.err);
							     }
							    share.addReceiver(msg.getSender());
								send(share);
								break;
							default:
								System.out.println("Mensagem no formato inesperado. (creature");
						}
								
					} else {
						// Se nao houver mensagem, bloquear behaviour.
						block();
					}
				}
			});
		} catch (Exception e){
			System.out.println( "Excecao em " + e );
            e.printStackTrace();
		}
	}
	
	@Override
	protected void takeDown() {
	  try {
		  	System.out.println(getLocalName() + " morto");
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
