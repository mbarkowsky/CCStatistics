package game.event;

import game.GameXML.Player;

public class FailEventXML extends EventXML {
	
	private String pokemon;
	private Player owner;
	
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

	public String toString(){
		return "failed to hit " + owner.toString() + "'s " + pokemon;
	}
}
