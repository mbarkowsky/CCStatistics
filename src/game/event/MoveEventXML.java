package game.event;

import game.GameXML;
import game.GameXML.Player;

import java.util.ArrayList;
import java.util.List;

public class MoveEventXML extends EventXML implements CompositeEvent{

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
	
	@Override
	public boolean isMoveEvent(){
		return true;
	}
	
	@Override
	public boolean isCompositeEvent() {
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
		if(effect == null){
			System.out.println(this);
		}
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

	public HealthEvent getLastEnemyHealthEvent() {
		HealthEvent lastEnemyHealthEvent = null;
		List<EventXML> effects = getEffects();
		for(int effectIndex = effects.size() - 1; effectIndex >= 0; effectIndex--){
			EventXML effect = effects.get(effectIndex);
			if(effect.isHealthEvent() && ((HealthEvent)effect).getOwner() == GameXML.getOpponent(getOwner())){
				return (HealthEvent)effect;
			}
		}
		return lastEnemyHealthEvent;
	}

}
