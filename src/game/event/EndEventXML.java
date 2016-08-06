package game.event;

import game.GameXML.Player;

public class EndEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private String end;

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
	
	public String getEnd() {
		return end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + "'s " + end + " ended";
	}
	
}
