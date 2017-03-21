package game.event;

import game.GameXML.Player;

public class RechargeEventXML extends EventXML {

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
	
}
