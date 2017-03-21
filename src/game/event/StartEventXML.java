package game.event;

import game.GameXML.Player;

public class StartEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private String start;
	private String from;

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
	
	public String getStart() {
		return start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public boolean isStartEvent() {
		return true;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + "'s " + start + " started";
	}
	
}
