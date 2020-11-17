package ghaphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import simulation.resources.Resource;

/**
 * Quadro de visualizacao do meio ambiente
 */
public class EnvironmentBoard extends Canvas {

	private static final int GRID_LINE_THICKNESS = 1;

	private int width;
	private int height;
	private int rows;
	private int columns;
	private int elHeightSize;
	private int elWidthSize;

	Resource resources[] = new Resource[0];
	Image foodImage;

	public EnvironmentBoard(int width, int height, int rows, int columns) {
		this.width = width;
		this.height = height;
		this.rows = rows;
		this.columns = columns;

		this.setSize(this.columns, this.height);
		this.setImage("/food.png");
	}

	@Override
	public void paint(Graphics g) {
		this.updateSizes();
		this.drawGrid(g);
		this.drawElements(g, resources);
	}

	public void insertElements(Resource resources[]) {
		this.resources = resources;
	}

	private void setImage(String path) {
		try {
			this.foodImage = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateSizes() {
		this.width = getSize().width;
		this.height = getSize().height;
		this.elHeightSize = this.height / (this.rows);
		this.elWidthSize = this.width / (this.columns);
	}

	private void drawGrid(Graphics g) {
		for (int row = 0; row < this.rows; row++) {
			g.drawLine(0, getWindowPos(this.elHeightSize, row), width, getWindowPos(this.elHeightSize, row));
		}

		for (int col = 0; col < this.columns; col++) {
			g.drawLine(getWindowPos(this.elWidthSize, col), 0, getWindowPos(this.elWidthSize, col), height);
		}
	}

	private void drawElements(Graphics g, Resource elements[]) {
		for (int i = 0; i < elements.length; i++) {
			this.drawElement(g, foodImage, elements[i].getXPos(), elements[i].getYPos());
		}
	}

	private void drawElement(Graphics g, Image image, int xPos, int yPos) {
		image = resizeImage(image, elWidthSize - GRID_LINE_THICKNESS, elHeightSize - GRID_LINE_THICKNESS);
		g.drawImage(image, getWindowPos(elWidthSize, xPos) + GRID_LINE_THICKNESS,
				getWindowPos(elHeightSize, yPos) + GRID_LINE_THICKNESS, this);
	}

	private int getWindowPos(int pieceSize, int boardPosition) {
		return pieceSize * boardPosition;
	}

	private static Image resizeImage(Image img, int newW, int newH) {
		return img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	}
}
