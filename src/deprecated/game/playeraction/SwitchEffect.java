package deprecated.game.playeraction;

public class SwitchEffect implements AttackEffect {

	private String pokemon;
	
	@Override
	public EffectType getType() {
		return EffectType.SWITCH;
	}

	public String getPokemon() {
		return pokemon;
	}

	public void setPokemon(String pokemon) {
		this.pokemon = pokemon;
	}
	
	public String toString(){
		return getType() + " -> " + pokemon;
	}

}
