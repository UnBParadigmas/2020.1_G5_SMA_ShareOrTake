package generic.board.item;

public abstract class BoardItem {
	private int xPos;
	private int yPos;
	
	public int getXPos() {
		return xPos;
	}

	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setYPos(int yPos) {
		this.yPos = yPos;
	}
	
	public void setPos(Integer xPos, Integer yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	protected static boolean isInBorder(int pos, int minPos, int maxPos) {
		return pos == minPos || pos == maxPos;
	}
}
