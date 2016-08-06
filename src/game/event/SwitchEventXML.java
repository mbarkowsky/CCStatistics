package game.event;

import game.GameXML.Player;

public class SwitchEventXML extends EventXML implements HealthEvent{
	
	private String pokemon;
	private Player owner;
	private int health;
	
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
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String toString(){
		return owner.toString() + " switched in " + pokemon + " at " + health + "% HP";
	}
}
