package game.event;

import game.GameXML.Player;

import java.util.ArrayList;
import java.util.List;

public class MoveEventXML extends EventXML {

	private String pokemon;
	private Player owner;
	private String move;
	private boolean missed;
	private boolean noTarget;
	private String from;
	private List<EventXML> effects;
	
	public MoveEventXML(){
		missed = false;
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

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public boolean getMissed() {
		return missed;
	}

	public void setMissed(boolean missed) {
		this.missed = missed;
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setNoTarget(boolean noTarget) {
		this.noTarget = noTarget;
	}
	
	public boolean getNoTarget(){
		return noTarget;
	}
	
	public List<EventXML> getEffects() {
		return effects;
	}

	public void addEffect(EventXML effect){
		effects.add(effect);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(owner.toString() + "'s " + pokemon + " used " + move);
		for(EventXML effect:effects){
			sb.append("\n");
			sb.append("\t" + effect.toString());
		}
		return sb.toString();
	}

}
