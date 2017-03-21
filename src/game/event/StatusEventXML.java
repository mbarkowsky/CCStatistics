package game.event;

import game.GameXML.Player;
import util.GameUtil.Status;

public class StatusEventXML extends EventXML {

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
	
	@Override
	public boolean isStatusEvent() {
		return true;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + " got status " + status.toString();
	}
	
}
