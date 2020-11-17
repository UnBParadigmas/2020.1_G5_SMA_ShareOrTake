package simulation.environment;

import javax.swing.JFrame;

import graphics.MainWindow;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

/**
 * Agente que controla o meio ambiente em que as criaturas estao inseridas
 */
public class EnvironmentAgent extends Agent {
	private static final long serialVersionUID = -6481631683157763680L;
	
	protected MainWindow mainWindow = null;

	@Override
	protected void setup() {
		System.out.println("Iniciando " + getLocalName());
		
		this.registerInDFD();
		this.setUpUI();
	}
	
	@Override
	protected void takeDown() {
	}
	
	private void registerInDFD() {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setUpUI() {
		mainWindow = new MainWindow(this, 400, 200, 10, 10);
		
		mainWindow.setSize(400, 200);
		mainWindow.setLocation(400, 400);
		mainWindow.setVisible(true);
		mainWindow.validate();
		
//		mainWindow.setBoardSize(10, 10);
	}
}
