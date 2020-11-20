package simulation.creatures;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/*
 * Grupo de especies com os mesmos comportamentos
 */
public class SpeciesState{

    private String name;
	private List<CreatureState> creaturesState = new ArrayList<CreatureState>();
	private String shareStrategy;
	private String imagePath;
	private Image groupImage;
	
	// Construtor parametrizado
    public SpeciesState(String name, String shareSttrategy, String imagePath){
    	this.setImage(imagePath);
        this.name = name;
        this.shareStrategy = shareSttrategy;
        this.imagePath = imagePath;
    }
	
	// Retorna o nome da espécie.
	public String getName() {
		return name;
	}
	
	public String getShareStrategy() {
		return this.shareStrategy;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
	
	public Image getGroupImage() {
		return groupImage;
	}
	
	// Muda o nome da espécie para o argumento.
	public void setName(String name) {
		this.name = name;
	}
	
	public List<CreatureState> getCreaturesState() {
		return this.creaturesState;
	}
	
    // Retorna quantidade de criaturas da espécie.
	public int getCreatureCount() {
		return this.creaturesState.size();
	}
	
    // Adiciona uma criatura a especie
    public void addCreatureState(CreatureState creature) {
    	this.creaturesState.add(creature);
    }
    
    private void setImage(String path) {
		try {
			this.groupImage = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
