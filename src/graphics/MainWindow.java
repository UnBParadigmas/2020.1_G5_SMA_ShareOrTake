package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import generic.board.item.BoardItemGroup;
import simulation.environment.EnvironmentAgent;

/**
 * Janela principal do programa
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = -5478484368604588317L;

	private static final String WIN_NAME = "Share or Take?";

	private JPanel contentPane;

	private EnvironmentAgent environment;
	private EnvironmentBoard envBoard;
	private SideMenu sideMenu;

	public MainWindow(EnvironmentAgent environment, int width, int height, int rows, int columns) {
		try {
			this.buildWindow(width, height);
			this.buildLayout();
			this.createSideMenu();
			this.createEnvironmentBoard(rows, columns);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.environment = environment;
	}

	public void insertElementsGroup(BoardItemGroup elementGroup) {
		this.envBoard.insertElementsGroup(elementGroup);
	}

	private void buildWindow(int width, int height) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setTitle(WIN_NAME);
		this.setSize(width, height);
		this.setMinimumSize(new Dimension(600, 600));
		this.setLocation(middle(screenSize.width, getWidth()), middle(screenSize.height, getHeight()));
		this.setVisible(true);
		this.validate();
	}

	private void buildLayout() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
	}

	private void createEnvironmentBoard(int rows, int columns) {
		envBoard = new EnvironmentBoard(rows, columns);
		contentPane.add(envBoard);
	}

	private void createSideMenu() {
		sideMenu = new SideMenu();
		contentPane.add(sideMenu);
	}

	@Override
	public void paint(Graphics g) {
		int menuWidth = 240;
		int width = this.contentPane.getSize().width;
		int height = this.contentPane.getSize().height;
		int boardSize = Math.min(width - menuWidth, height);

		if (envBoard != null)
			envBoard.setBounds(0, 0, boardSize, boardSize);

		if (sideMenu != null)
			sideMenu.setBounds(width - menuWidth, 0, menuWidth, height);
	}

	private int middle(int totalSize, int itselfSize) {
		return (totalSize - itselfSize) / 2;
	}
}
