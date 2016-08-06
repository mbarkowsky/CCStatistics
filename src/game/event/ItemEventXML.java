package game.event;

import game.GameXML.Player;


public class ItemEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private String item;
	
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

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String toString(){
		return owner.toString() + "'s " + pokemon + " used its "  + item;
	}
	
}
