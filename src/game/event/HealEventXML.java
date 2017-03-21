package game.event;

import game.GameXML.Player;
import util.GameUtil;

public class HealEventXML extends EventXML implements HealthEvent{

	private String pokemon;
	private Player owner;
	private int health;
	private GameUtil.Status status;
	
	@Override
	public boolean isHealthEvent(){
		return true;
	}
	
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
	
	public GameUtil.Status getStatus() {
		return status;
	}

	public void setStatus(GameUtil.Status status) {
		this.status = status;
	}

	public String toString(){
		return owner.toString() + "'s " + pokemon + " was healed (" + health + "% HP)";
	}
	
}
