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
	private int initialXPos;
	private int initialYPos;
	private boolean alive;
	private String shareStrategy;
	
	@Override
	protected void setup() {
		try {
			this.initialXPos = (int) this.getArguments()[0];
			this.initialYPos = (int) this.getArguments()[1];
			this.shareStrategy  = (String) this.getArguments()[2];
			this.alive = true;
			
			this.xPos = this.initialXPos;
			this.yPos = this.initialYPos;
			
			System.out.println("Criando criatura " + getLocalName());
			System.out.println("POS X: " + this.xPos + ", POS Y: " + this.yPos);
			System.out.println("Estrategia: " + this.shareStrategy);
		
			this.registerInDFD();
		
			// Notificar board que a criatura esta aqui
//			ACLMessage hello = new ACLMessage(ACLMessage.INFORM);
//			hello.setContent(EnvironmentAgent.HELLO);
//			hello.addReceiver(new AID("environment", AID.ISLOCALNAME));
//			send(hello);
			
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
									case EnvironmentAgent.DAY_ARAISE:
										// Informacao de que esta de dia, a criatura deve procurar comida
										seekFood();
										break;
									case EnvironmentAgent.NIGHT_FALL:
										// Informacao de que esta de dia, a criatura deve voltar para casa
										System.out.println(getLocalName() + " voltou para casa");
										break;
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
									         System.err.println("Nao consegui reconhecer mensagem. Mandando mensagem vazia.");
									         ex.printStackTrace(System.err);
									     }
									    share.addReceiver(msg.getSender());
										send(share);
										break;
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
									a.xPos = (int) oMsg[1];
									a.yPos = (int) oMsg[2];

									System.out.println(getLocalName() + " Nova posicao X: " + a.xPos + " Y: " + a.yPos);
									System.out.println(getLocalName() + " Comida disponivel: "+ oMsg[0]);
								} catch (UnreadableException e) {
									// Nao reconheci a mensagem.
									System.out.println("N�o consegui ler a posi��o nova!");
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
	
	private void seekFood() {
		ACLMessage seekFoodMsg = new ACLMessage(ACLMessage.REQUEST);
		
		System.out.println(getLocalName() +  " foi procurar comida");
		seekFoodMsg.setContent(EnvironmentAgent.FOOD_SEEK);
		seekFoodMsg.addReceiver(new AID("environment", AID.ISLOCALNAME));
		send(seekFoodMsg);
	}
	
	public void setShareStrategy(String shareStrategy) {
		this.shareStrategy = shareStrategy;
	}
	
	public void kill() {
		this.alive = false;
	}
}
