package simulation.creatures;

import jade.core.Agent;

import java.io.IOException;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import jade.lang.acl.ACLMessage;
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
	private String speciesName;

	@Override
	protected void setup() {
		try {
			this.initialXPos = (int) this.getArguments()[0];
			this.initialYPos = (int) this.getArguments()[1];
			this.shareStrategy = (String) this.getArguments()[2];
			this.speciesName = (String) this.getArguments()[3];

			this.xPos = this.initialXPos;
			this.yPos = this.initialYPos;

			System.out.println("Criando criatura " + getLocalName());

			this.registerInDFD();

			// Adicionar behaviour para mover quando chamado
			addBehaviour(new CyclicBehaviour(this) {
				private static final long serialVersionUID = 1L;

				public void action() {
					ACLMessage msg = receive();
					CreatureAgent ctrAgent = (CreatureAgent) myAgent;
					if (msg != null) {

						switch (msg.getPerformative()) {
						case ACLMessage.INFORM:

							switch (msg.getContent()) {
							case EnvironmentAgent.DAY_ARAISE:
								// Informacao de que esta de dia, as criaturas devem procurar comida
								seekFood();
								break;
							case EnvironmentAgent.NIGHT_FALL:
								// Informacao de que esta de noite, as criaturas devem voltar para casa
								doGoBack(ctrAgent);
								break;
							case EnvironmentAgent.DEAD:
								kill();
								doDelete();
								break;
							case EnvironmentAgent.REPRODUCE:
//								doReproduceRequest(ctrAgent, msg);
								break;
							default:
								System.out.println("Mensagem inesperada (creature).");
							}

							break;

						case ACLMessage.PROPOSE:
							doChangeCoords(ctrAgent, msg);
							doSendInfo(ctrAgent, msg);
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
		} catch (Exception e) {
			System.out.println("Excecao em " + e);
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

		seekFoodMsg.setContent(EnvironmentAgent.FOOD_SEEK);
		seekFoodMsg.addReceiver(new AID("environment", AID.ISLOCALNAME));
		send(seekFoodMsg);
	}

	public void setShareStrategy(String shareStrategy) {
		this.shareStrategy = shareStrategy;
	}

	public void kill() {
	}

	private void doChangeCoords(CreatureAgent ctrAgent, ACLMessage msg) {
		try {
			Object[] oMsg = (Object[]) msg.getContentObject();
			ctrAgent.xPos = (int) oMsg[0];
			ctrAgent.yPos = (int) oMsg[1];

		} catch (UnreadableException e) {
			// Nao reconheci a mensagem.
			System.out.println("Nao consegui ler a posicao nova!");
			e.printStackTrace();
		}
	}

	private void doGoBack(CreatureAgent ctrAgent) {
		ctrAgent.xPos = ctrAgent.initialXPos;
		ctrAgent.yPos = ctrAgent.initialYPos;
		
		ACLMessage doGoBackMsg = new ACLMessage(ACLMessage.INFORM);
		doGoBackMsg.setContent(EnvironmentAgent.GOBACK + ',' + ctrAgent.xPos + ',' + ctrAgent.yPos);
		
		doGoBackMsg.setSender(ctrAgent.getAID());
		doGoBackMsg.addReceiver(new AID("environment", AID.ISLOCALNAME));
		send(doGoBackMsg);
	}

	private void doSendInfo(CreatureAgent ctrAgent, ACLMessage msg) {
		ACLMessage share = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		share.setSender(ctrAgent.getAID());
		try {
			Object[] oMsg = new Object[4];
			oMsg[0] = ctrAgent.xPos;
			oMsg[1] = ctrAgent.yPos;
			oMsg[2] = ctrAgent.shareStrategy;
			oMsg[3] = ctrAgent.getAID();

			share.setContentObject(oMsg);
		} catch (IOException ex) {
			System.err.println("Nao consegui reconhecer mensagem. Mandando mensagem vazia.");
			ex.printStackTrace(System.err);
		}
		share.addReceiver(msg.getSender());
		send(share);
	}
}
