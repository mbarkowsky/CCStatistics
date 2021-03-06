package game.event;

import game.GameXML.Player;
import util.GameUtil.Status;

public class CureStatusEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private Status status;
	
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + " cured its status " + status.toString();
	}
	
}
