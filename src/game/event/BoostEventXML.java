package game.event;

import game.GameXML.Player;
import util.GameUtil;

public class BoostEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private GameUtil.Stat stat;
	private int levels;
	
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

	public GameUtil.Stat getStat() {
		return stat;
	}

	public void setStat(GameUtil.Stat stat) {
		this.stat = stat;
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}
	
	public String toString(){
		return owner.toString() + "'s " + pokemon + "'s " + stat.toString() + " changed by " + levels;
	}
}
