package game.event;

import game.GameXML.Player;

public class FaintEventXML extends EventXML {

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
	
	@Override
	public boolean isFaintEvent() {
		return true;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + " fainted";
	}
}
