package ghaphics;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;

import simulation.environment.EnvironmentAgent;
import simulation.resources.Resource;

/**
 * Janela principal do programa
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = -5478484368604588317L;

	private static final String WIN_NAME = "Share or Take?";

	private int rows = 0;
	private int columns = 0;

	private EnvironmentAgent environment;
	private EnvironmentBoard envBoard;
	
	public static void main(String[] args) {
		MainWindow window = new MainWindow(new EnvironmentAgent(), 400, 400, 10, 10);
		Resource resourcesTest[] = new Resource[1];
		
		resourcesTest[0] = new Resource();
		resourcesTest[0].setPos(1, 1);
		
		window.setVisible(true);
		window.insertElements(resourcesTest);
	}

	public MainWindow(EnvironmentAgent environment, int width, int height, int rows, int columns) {
		try {
			this.buildWindow(width, height);
			this.setBoardSize(rows, columns);
			this.createEnvironmentBoard();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.environment = environment;
	}

	public void setBoardSize(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}
	
	public void insertElements(Resource resources[]) {
		this.envBoard.insertElements(resources);
	}
	
	private void buildWindow(int width, int height) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.setTitle(WIN_NAME);
		this.setSize(width, height);
		this.setLocation(middle(screenSize.width, getWidth()), middle(screenSize.height, getHeight()));
		this.setVisible(true);
		this.validate();
	}
	
	private int middle(int totalSize, int itselfSize) {
		return (totalSize - itselfSize)/2;
	}

	private void createEnvironmentBoard() {
		this.envBoard = new EnvironmentBoard(this.getWidth(), this.getHeight(), rows, columns);
		this.add(envBoard);
	}
}
