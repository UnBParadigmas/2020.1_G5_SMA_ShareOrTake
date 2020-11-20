package graphics;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import generic.board.item.BoardItemGroup;
import simulation.creatures.CreatureState;
import simulation.creatures.SpecyState;
import simulation.resources.Food;

/**
 * Quadro de visualizacao do meio ambiente
 */
public class EnvironmentBoard extends JPanel {
	private static final long serialVersionUID = -2564620559534701364L;

	private static final int GRID_LINE_THICKNESS = 1;

	private int panelBoardSize;
	private int boardSize;
	private int elementSize;

	private Image foodImage = null;
	private List<Food> foodItems = new ArrayList<>();
	private List<CreatureState> creaturesItems = new ArrayList<>();

	public EnvironmentBoard(int boardSize) {
		this.boardSize = boardSize;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.drawGrid(g);
		this.drawSpecies(g);
		this.drawFood(g);
	}
	
	public void clearBoard() {
		this.creaturesItems.clear();
		this.foodItems.clear();
	}

	public void insertFood(List<Food> food, Image foodImage) {
		this.foodItems = food;
		this.foodImage = foodImage;
		this.repaint();
	}
	
	public void insertSpecies(List<CreatureState> creatures) {
		this.creaturesItems = creatures;
		this.repaint();
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		this.panelBoardSize = width;
		this.elementSize = this.panelBoardSize / (this.boardSize);
	}

	private void drawGrid(Graphics g) {
		for (int row = 0; row < this.boardSize; row++) {
			g.drawLine(0, getWindowPos(this.elementSize, row), panelBoardSize, getWindowPos(this.elementSize, row));
		}

		for (int col = 0; col < this.boardSize; col++) {
			g.drawLine(getWindowPos(this.elementSize, col), 0, getWindowPos(this.elementSize, col), panelBoardSize);
		}
	}

	private void drawSpecies(Graphics g) {
		for (int i = 0; i < this.creaturesItems.size(); i++) {
			Image groupImage = this.resizeImage(creaturesItems.get(i).getImage(), elementSize - GRID_LINE_THICKNESS,
					elementSize - GRID_LINE_THICKNESS);
			
			this.drawElement(g, groupImage, this.creaturesItems.get(i).getXPos(),
					this.creaturesItems.get(i).getYPos());
		}
	}
	
	private void drawFood(Graphics g) {
		if (foodImage != null) {			
			Image groupImage = this.resizeImage(foodImage, elementSize - GRID_LINE_THICKNESS,
					elementSize - GRID_LINE_THICKNESS);
			for (int i = 0; i < this.foodItems.size(); i++) {
				this.drawElement(g, groupImage, this.foodItems.get(i).getXPos(),
						this.foodItems.get(i).getYPos());
			}
		}
	}


	private void drawElement(Graphics g, Image groupImage, int xPos, int yPos) {
		g.drawImage(groupImage, getWindowPos(elementSize, xPos) + GRID_LINE_THICKNESS,
				getWindowPos(elementSize, yPos) + GRID_LINE_THICKNESS, this);
	}

	private int getWindowPos(int pieceSize, int boardPosition) {
		return pieceSize * boardPosition;
	}

	public Image resizeImage(Image image, int newW, int newH) {
		return image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	}
}
