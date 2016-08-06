package game.event;

import game.GameXML.Player;

import java.util.ArrayList;
import java.util.List;

public class AbilityEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private String ability;
	private List<EventXML> effects;
	
	public AbilityEventXML(){
		effects = new ArrayList<>();
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

	public String getActivation() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
	}
	
	public List<EventXML> getEffects() {
		return effects;
	}

	public void addEffect(EventXML effect){
		effects.add(effect);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(owner.toString() + "'s " + pokemon + "'s Ability " + ability + " activated");
		for(EventXML effect:effects){
			sb.append("\n");
			sb.append("\t" + effect.toString());
		}
		return sb.toString();
	}
}
