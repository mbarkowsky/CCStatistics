package game.playeraction;

public class DamageEffect implements AttackEffect {

	public enum Effectiveness {NEUTRAL, SUPER_EFFECTIVE, NOT_VERY_EFFECTIVE, NO_EFFECT}
	
	private String defender;
	private boolean isCrit;
	private boolean isKO;
	private Effectiveness effectiveness;
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

	public Effectiveness getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(Effectiveness effectiveness) {
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
