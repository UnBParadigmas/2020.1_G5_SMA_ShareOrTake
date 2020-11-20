package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import generic.board.item.BoardItemGroup;
import simulation.creatures.SpecyState;
import simulation.environment.EnvironmentAgent;
import simulation.resources.Food;

/**
 * Janela principal do programa
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = -5478484368604588317L;

	private static final String WIN_NAME = "Share or Take?";
	private static final int BOARD_SIZE = 20;
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 800;

	private JPanel contentPane;

	private EnvironmentAgent environment;

	InitialScreen initialScreen;
	private EnvironmentBoard envBoard;
	private ControllerMenu controllerMenu;

	public MainWindow(EnvironmentAgent environment) {
		this.environment = environment;
		
		try {
			this.buildWindow();
			this.buildContentPanel();
			this.buildSimulationLayout();
			this.startThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearBoard() {
		this.envBoard.clearBoard();
	}

	public void insertFood(List<Food> foodItems, Image foodImage) {
		this.envBoard.insertFood(foodItems, foodImage);
	}
	
	public void insertSpecies(List<SpecyState> species) {
		this.envBoard.insertSpecies(species);
	}

	private void buildWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setTitle(WIN_NAME);
		this.setSize(WIDTH, HEIGHT);
		this.setMinimumSize(new Dimension(600, 600));
		this.setLocation(middle(screenSize.width, getWidth()), middle(screenSize.height, getHeight()));
		this.setVisible(true);
		this.validate();
	}

	private void buildContentPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
	}

	private void buildStartSettingsLayout() {
		initialScreen = new InitialScreen();
		initialScreen.setBounds(0, 0, 800, 800);
		contentPane.add(initialScreen);
	}

	private void buildSimulationLayout() {
		int menuWidth = 240;
		int width = this.contentPane.getSize().width;
		int height = this.contentPane.getSize().height;
		int boardSize = Math.min(width - menuWidth, height);

		envBoard = new EnvironmentBoard(BOARD_SIZE);
		envBoard.setBounds(0, 0, boardSize, boardSize);
		contentPane.add(envBoard);
		
		controllerMenu = new ControllerMenu(environment, BOARD_SIZE);
		controllerMenu.setBounds(width - 240, 0, 240, height);
		contentPane.add(controllerMenu);
	}
	
	private void startThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					repaint();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void paint(Graphics g) {
		int menuWidth = 240;
		int width = this.contentPane.getSize().width;
		int height = this.contentPane.getSize().height;
		int boardSize = Math.min(width - menuWidth, height);
		

		if (envBoard != null) {
			envBoard.setBounds(0, 0, boardSize, boardSize);
			envBoard.repaint();
		}

		if (controllerMenu != null)
			controllerMenu.setBounds(width - menuWidth, 0, menuWidth, height);
	}

	private int middle(int totalSize, int itselfSize) {
		return (totalSize - itselfSize) / 2;
	}
}
