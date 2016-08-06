package game.event;

import game.GameXML.Player;

public class PrepareEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private String preparation;
	
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

	public String getPreparation() {
		return preparation;
	}

	public void setPreparation(String preparation) {
		this.preparation = preparation;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + " prepares " + preparation;
	}
	
}
