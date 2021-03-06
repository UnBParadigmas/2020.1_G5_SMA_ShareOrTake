package simulation.creatures;

import java.awt.Image;
import java.util.List;

import generic.board.item.BoardItem;
import jade.core.AID;

/*
 * Caracteristicas da criatura
 */
public class CreatureState extends BoardItem{
	//Constants
	public final static String FRIENDLY = "FRIENDLY";
	public final static String AGGRESSIVE = "AGGRESSIVE";
	
	private AID creatureId;
	private boolean alive;
	private String shareStrategy;
	private Image speciesImage;
	private String speciesName;
	
	private int initialXPos;
	private int initialYPos;
	
	// Construtor parametrizado
	public CreatureState (AID creatureId, String speciesName, int xPos, int yPos, String shareStrategy, Image speciesImage){
		this.creatureId = creatureId;
		this.speciesName = speciesName;
		this.initialXPos = xPos;
		this.initialYPos = yPos;
		this.shareStrategy = shareStrategy;
		this.speciesImage = speciesImage;
	
		this.setXPos(this.initialXPos);
		this.setYPos(this.initialYPos);
		this.alive = true;
	}

	// Retorna se a criatura está viva.
	public boolean getStatus(){
		return this.alive;
	}
	
	public Image getImage() {
		return this.speciesImage;
	}

	// Muda o status da criatura.
	public void setStatus(boolean alive){
		this.alive = alive;
	}
	
	public AID getId() {
		return this.creatureId;
	}
	
	public String getSpeciesName() {
		return this.speciesName;
	}
	
	public void setShareStrategy(String shareStrategy) {
		this.shareStrategy = shareStrategy;
	}
	
	public String getShareStrategy() {
		return this.shareStrategy;
	}
	
	public int getInitialXPos() {
		return this.initialXPos;
	}
	
	public int getInitialYPos() {
		return this.initialYPos;
	}
	
	// Gera a posicao aleatoria da criatura
	// A criatura so deve iniciar nas bordas do board 
	public static int[] getRandomPos(List<CreatureState> creaturesState, int minPos, int maxPos) {
		int pos[];
		boolean repeated;
		do {
			repeated = false;
			pos = randomPos(minPos, maxPos);
			for (CreatureState otherCreature : creaturesState) {
				if (otherCreature.getInitialXPos() == pos[0] && otherCreature.getInitialYPos() == pos[1]) {
					repeated = true;
					break;
				}
			}
		} while (repeated);
		return pos;
	}

	private static int[] randomPos(int minPos, int maxPos) {
		int xPos = 0, yPos = 0;
		while (true) {
			xPos = (int) (Math.random() * ((maxPos + 1) - minPos) + minPos);
			yPos = (int) (Math.random() * ((maxPos + 1) - minPos) + minPos);

			if (isInBorder(xPos, minPos, maxPos) || isInBorder(yPos, minPos, maxPos)) {
				return new int[] { xPos, yPos };
			}
		}
	}
}
