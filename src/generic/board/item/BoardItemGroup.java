package generic.board.item;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BoardItemGroup {
	private Image groupImage;
	private BoardItem boardItems[];
	
	public BoardItemGroup(BoardItem boardItems[], String imagePath) {
		this.boardItems = boardItems;
		this.setImage(imagePath);
	}

	public Image getGroupImage() {
		return groupImage;
	}
	
	public BoardItem[] getBoardItems() {
		return boardItems;
	}
	
	private void setImage(String path) {
		try {
			this.groupImage = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
