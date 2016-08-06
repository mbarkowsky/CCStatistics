package game.event;

import game.GameXML.Player;
import util.GameUtil.Effectiveness;

public class EffectivenessEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private Effectiveness effectiveness;
	
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

	public Effectiveness getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(Effectiveness effectiveness) {
		this.effectiveness = effectiveness;
	}
	
	public String toString(){
		return effectiveness.toString() + " on " + owner.toString() + "'s " + pokemon;
	}
}
