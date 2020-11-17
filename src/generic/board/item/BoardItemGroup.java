package generic.board.item;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class BoardItemGroup {
	private Image groupImage;
	private List<BoardItem> boardItems = new ArrayList<>();
	private int minPos = 0;
	private int maxPos = 0;
	
	public BoardItemGroup(List<? extends BoardItem> boardItems, String imagePath, int minPos, int maxPos) {
		this.setImage(imagePath);
		this.minPos = minPos;
		this.maxPos = maxPos;
		
		this.buildList(new ArrayList<BoardItem>(boardItems));
	}

	public Image getGroupImage() {
		return groupImage;
	}
	
	public List<BoardItem> getBoardItems() {
		return boardItems;
	}
	
	public void setBoardItems(List<BoardItem> boardItems) {
		this.boardItems = boardItems;
	}
	
	public void addItem(BoardItem boardItem) {
		setRandomPos(boardItem, this.boardItems, this.minPos, this.maxPos);
		this.boardItems.add(boardItem);
	}
	
	private void setImage(String path) {
		try {
			this.groupImage = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Adiciona os itens no board
	private void buildList(List<BoardItem> boardItems) {
		for (int i = 0; i < boardItems.size(); i++) {
			this.addItem(boardItems.get(i));
		}
	}
	
	// Escolhe uma posição aleatória dentro dos limites e sem conflito com os outros itens
	private static void setRandomPos(BoardItem item, List<BoardItem> otherItems, int minPos, int maxPos) {
		boolean repeated;
		do {
			repeated = false;
			item.randomPos(minPos, maxPos);
			for (BoardItem otherItem : otherItems) {
				if (otherItem.getXPos() == item.getXPos() && otherItem.getYPos() == item.getYPos()) {
					repeated = true;
					break;
				}
			}
		} while(repeated);
	}
}
