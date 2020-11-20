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
	private String shareStrategy;
	private String imagePath;
	private Image image;
	private int creaturesAmount = 0;
	
	// Construtor parametrizado
    public SpeciesState(String name, int amount, String shareSttrategy, String imagePath){
    	this.setImage(imagePath);
        this.name = name;
        this.creaturesAmount = amount;
        this.shareStrategy = shareSttrategy;
        this.imagePath = imagePath;
    }
	
	// Retorna o nome da espécie.
	public String getName() {
		return name;
	}
	
	public int getCreaturesAmount() {
		return this.creaturesAmount;
	}
	
	public String getShareStrategy() {
		return this.shareStrategy;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
	
	public Image getGroupImage() {
		return image;
	}
	
	// Muda o nome da espécie para o argumento.
	public void setName(String name) {
		this.name = name;
	}
	
    private void setImage(String path) {
		try {
			this.image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
