package simulation.creatures;

import java.util.ArrayList;
import java.util.List;

/*
 * Grupo de especies com os mesmos comportamentos
 */
public class SpecyState{

    private String name;
	private List<CreatureState> creaturesState = new ArrayList<CreatureState>();
	private String shareStrategy;
	private String imagePath;
	
	// Construtor parametrizado
    public SpecyState(String name, String shareSttrategy, String imagePath){
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
}
