package deprecated.game.playeraction;

import java.util.LinkedList;
import java.util.List;

import deprecated.game.playeraction.AttackEffect.EffectType;


public class AttackAction implements PlayerAction {
	
	private List<AttackEffect> effects;
	private String pokemon;
	private String attack;
	
	public AttackAction(String pokemon, String attack){
		this.pokemon = pokemon;
		this.attack = attack;
		this.effects = new LinkedList<>();
	}

	public String getPokemon() {
		return pokemon;
	}

	public void setPokemon(String pokemon) {
		this.pokemon = pokemon;
	}

	public String getAttack() {
		return attack;
	}

	public void setAttack(String attack) {
		this.attack = attack;
	}

	@Override
	public ActionType getType() {
		return ActionType.ATTACK;
	}

	public List<AttackEffect> getEffects() {
		return effects;
	}

	public void addEffect(AttackEffect effect) {
		effects.add(effect);
	}
	
	@Override
	public String toString(){
		return "Attack: " + pokemon + ": " + attack + "\n" + (effects.isEmpty() ? "" : effects.toString());
	}

	public List<AttackEffect> getEffects(EffectType type) {
		List<AttackEffect> list = new LinkedList<AttackEffect>();
		for(AttackEffect effect:effects){
			if(effect.getType() == type){
				list.add(effect);
			}
		}
		return list;
	}

	public void addEffects(List<AttackEffect> effects) {
		this.effects.addAll(effects);
	}
}
