package graphics;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import generic.board.item.BoardItemGroup;

/**
 * Quadro de visualizacao do meio ambiente
 */
public class EnvironmentBoard extends Canvas {
	private static final long serialVersionUID = -2564620559534701364L;

	private static final int GRID_LINE_THICKNESS = 1;

	private int boardSize;
	private int rows;
	private int columns;
	private int elementSize;

	private List<BoardItemGroup> elementsGroups = new ArrayList<>();

	public EnvironmentBoard(int width, int height, int rows, int columns) {
		this.boardSize = height;
		this.rows = rows;
		this.columns = columns;

		this.setSize(this.boardSize, this.boardSize);
	}

	@Override
	public void paint(Graphics g) {
		this.updateSizes();
		this.drawGrid(g);
		this.drawElementsGroups(g);
	}

	public void insertElementsGroup(BoardItemGroup elementGroup) {
		this.elementsGroups.add(elementGroup);
	}

	private void updateSizes() {
		this.boardSize = getSize().height;
		this.elementSize = this.boardSize / (this.rows);
	}

	private void drawGrid(Graphics g) {
		for (int row = 0; row < this.rows; row++) {
			g.drawLine(0, getWindowPos(this.elementSize, row), boardSize, getWindowPos(this.elementSize, row));
		}

		for (int col = 0; col < this.columns; col++) {
			g.drawLine(getWindowPos(this.elementSize, col), 0, getWindowPos(this.elementSize, col), boardSize);
		}
	}

	private void drawElementsGroups(Graphics g) {
		for (BoardItemGroup elementsGroup : this.elementsGroups) {
			this.drawElements(g, elementsGroup);
		}
	}

	private void drawElements(Graphics g, BoardItemGroup elementsGroup) {
		for (int j = 0; j < elementsGroup.getBoardItems().length; j++) {
			this.drawElement(g, elementsGroup, elementsGroup.getBoardItems()[j].getXPos(),
					elementsGroup.getBoardItems()[j].getYPos());
		}
	}

	private void drawElement(Graphics g, BoardItemGroup elementsGroup, int xPos, int yPos) {
		Image image = this.resizeImage(elementsGroup.getGroupImage(), elementSize - GRID_LINE_THICKNESS,
				elementSize - GRID_LINE_THICKNESS);

		g.drawImage(image, getWindowPos(elementSize, xPos) + GRID_LINE_THICKNESS,
				getWindowPos(elementSize, yPos) + GRID_LINE_THICKNESS, this);
	}

	private int getWindowPos(int pieceSize, int boardPosition) {
		return pieceSize * boardPosition;
	}

	public Image resizeImage(Image image, int newW, int newH) {
		return image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	}
}
