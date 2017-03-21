package game.event;

import java.util.ArrayList;
import java.util.List;

import game.GameXML.Player;
import util.GameUtil;

public class DamageEventXML extends EventXML implements HealthEvent{

	private String pokemon;
	private Player owner;
	private int health;
	private List<String> extras;
	private GameUtil.Status status;
	
	public DamageEventXML(){
		extras = new ArrayList<>();
	}
	
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

	public List<String> getExtras(){
		return extras;
	}
	
	public void addExtra(String string){
		extras.add(string);
	}
	
	@Override
	public boolean isDamageEvent() {
		return true;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + " took damage (" + health + "% HP)";
	}
	
}
