package builder;

import game.PlayerAction;

public class AttackAction extends PlayerAction {

	private String pokemon;
	private String attack;
	
	public AttackAction(String pokemon, String attack){
		this.pokemon = pokemon;
		this.attack = attack;
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
	
	public String toString(){
		return "Attack: " + pokemon + "; " + attack;
	}

	@Override
	public ActionType getType() {
		return ActionType.ATTACK;
	}
}
