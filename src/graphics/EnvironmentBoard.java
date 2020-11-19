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

	private int panelBoardSize;
	private int boardSize;
	private int elementSize;

	private List<BoardItemGroup> elementsGroups = new ArrayList<>();

	public EnvironmentBoard(int boardSize) {
		this.boardSize = boardSize;
	}

	@Override
	public void paint(Graphics g) {
		this.drawGrid(g);
		this.drawElementsGroups(g);
	}
	
	public void clearBoard() {
		this.elementsGroups.clear();
	}

	public void insertElementsGroup(BoardItemGroup elementGroup) {
		this.elementsGroups.add(elementGroup);
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

	private void drawElementsGroups(Graphics g) {
		for (BoardItemGroup elementsGroup : this.elementsGroups) {
			this.drawElements(g, elementsGroup);
		}
	}

	private void drawElements(Graphics g, BoardItemGroup elementsGroup) {
		for (int j = 0; j < elementsGroup.getBoardItems().size(); j++) {
			this.drawElement(g, elementsGroup, elementsGroup.getBoardItems().get(j).getXPos(),
					elementsGroup.getBoardItems().get(j).getYPos());
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
