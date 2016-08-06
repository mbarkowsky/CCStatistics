package deprecated.game.playeraction;

import util.GameUtil;

public class DamageEffect implements AttackEffect {

	private String defender;
	private boolean isCrit;
	private boolean isKO;
	private GameUtil.Effectiveness effectiveness;
	private double damage;
	
	@Override
	public EffectType getType() {
		return EffectType.DAMAGE;
	}

	public String getDefender() {
		return defender;
	}

	public void setDefender(String defender) {
		this.defender = defender;
	}

	public boolean isCrit() {
		return isCrit;
	}

	public void setCrit(boolean isCrit) {
		this.isCrit = isCrit;
	}

	public boolean isKO() {
		return isKO;
	}

	public void setKO(boolean isKO) {
		this.isKO = isKO;
	}

	public GameUtil.Effectiveness getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(GameUtil.Effectiveness effectiveness) {
		this.effectiveness = effectiveness;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public String toString(){
		String s = getType() + " (" + (isCrit ? "critical hit, " : "") + effectiveness.toString().toLowerCase() + ", " + damage + "%) to " + defender + (isKO ? "(fainted)" : "");
		return s;
	}
}
