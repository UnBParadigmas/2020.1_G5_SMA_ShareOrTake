package generic.board.item;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class BoardItemGroup {
	private Image groupImage;
	private List<BoardItem> boardItems = new ArrayList<>();
	
	public BoardItemGroup(List<? extends BoardItem> boardItems, String imagePath) {
		this.setImage(imagePath);
		
		this.boardItems = new ArrayList<BoardItem>(boardItems);
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
	
	private void setImage(String path) {
		try {
			this.groupImage = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
