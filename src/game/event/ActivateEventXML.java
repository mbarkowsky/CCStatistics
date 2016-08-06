package game.event;

import game.GameXML.Player;

import java.util.ArrayList;
import java.util.List;

public class ActivateEventXML extends EventXML {
	
	private String pokemon;
	private Player owner;
	private String activation;
	private List<EventXML> effects;
	
	public ActivateEventXML(){
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
		return activation;
	}

	public void setActivation(String activation) {
		this.activation = activation;
	}

	public List<EventXML> getEffects() {
		return effects;
	}

	public void addEffect(EventXML effect){
		effects.add(effect);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(owner.toString() + "'s " + pokemon + "'s " + activation + " activated");
		for(EventXML effect:effects){
			sb.append("\n");
			sb.append("\t" + effect.toString());
		}
		return sb.toString();
	}
}
