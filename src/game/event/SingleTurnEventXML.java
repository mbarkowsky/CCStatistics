package game.event;

import game.GameXML.Player;

public class SingleTurnEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private String effect;
	
	public String getPokemon() {
		return pokemon;
	}
	
	public void setPokemon(String pokemon) {
		this.pokemon = pokemon;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}
	
	public String toString(){
		return owner + "'s " + pokemon + " is under the effect of " + effect;
	}
}
