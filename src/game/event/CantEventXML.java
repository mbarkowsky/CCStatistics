package game.event;

import game.GameXML.Player;

public class CantEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private Object reason;
	
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

	public Object getReason() {
		return reason;
	}

	public void setReason(Object reason) {
		this.reason = reason;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + " can't move because of " + reason;
	}

}
