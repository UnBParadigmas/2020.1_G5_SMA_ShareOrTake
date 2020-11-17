package simulation.creatures;

import java.util.ArrayList;
import java.util.List;

import jade.core.behaviours.Behaviour;

/*
 * Grupo de especies com os mesmos comportamentos
 */
public class Specy{

    private String name;
	private Behaviour shareStrategy;
	private List<Creature> creatures = new ArrayList<Creature>();
	
	// Construtor parametrizado
    public Specy(String name, Behaviour shareStrategy){
        this.name = name;
        this.shareStrategy = shareStrategy;
    }
	
	// Retorna o nome da espécie.
	public String getName() {
		return name;
	}
	
	// Muda o nome da espécie para o argumento.
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Creature> getCreatures() {
		return this.creatures;
	}
	
    // Retorna quantidade de criaturas da espécie.
	public int getCreatureCount() {
		return this.creatures.size();
	}
	
    // Adiciona uma criatura a especie
    public void addCreature(Creature creature) {
    	this.creatures.add(creature);
    }
	
    // Retorna estratégia da espécie.
	public Behaviour getShareStrategy() {
		return shareStrategy;
	}
	
    // Muda a estratégia da espécie para argumento.
	public void setShareStrategy(Behaviour shareStrategy) {
		this.shareStrategy = shareStrategy;
	}
}