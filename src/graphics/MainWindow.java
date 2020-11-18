package graphics;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import generic.board.item.BoardItem;
import generic.board.item.BoardItemGroup;
import simulation.creatures.CreatureState;
import simulation.environment.EnvironmentAgent;
import simulation.resources.Food;

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
	
	public void insertElementsGroup(BoardItemGroup elementGroup) {
		this.envBoard.insertElementsGroup(elementGroup);
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
