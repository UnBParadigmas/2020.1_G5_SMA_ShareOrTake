package simulation.creatures;

import jade.core.behaviours.Behaviour;
/*
 * Caracteristicas da espécie
 */
public class Species {

    private String name;
	private Long creatureCount;
	private Behaviour shareStrategy;
	
	// Retorna o nome da espécie.
	public String getName() {
		return name;
	}
	
	// Muda o nome da espécie para o argumento.
	public void setName(String name) {
		this.name = name;
	}
	
    // Retorna quantidade de criaturas da espécie.
	public Long getCreatureCount() {
		return creatureCount;
	}
	
    // Muda quantidade de criaturas da espécie para valor do argumento.
	public void setCreatureCount(Long creatureCount) {
		this.creatureCount = creatureCount;
	}

    // Adiciona quantidade igual ao valor do argumento ao valor total.
    public void addCreatureCount(Long creatureCount) {
        this.creatureCount += creatureCount;
    }
	
    // Retorna estratégia da espécie.
	public Behaviour getShareStrategy() {
		return shareStrategy;
	}
	
    // Muda a estratégia da espécie para argumento.
	public void setShareStrategy(Behaviour shareStrategy) {
		this.shareStrategy = shareStrategy;
	}

    // Construtor parametrizado
    public Species(String name, Long creatureCount, Behaviour shareStrategy){
        this.name = name;
        this.creatureCount = creatureCount;
        this.shareStrategy = shareStrategy;
    }
}