package game.playeraction;

public interface AttackEffect {

	public enum EffectType {DAMAGE, HEALING, BOOST, DROP, STATUS, ITEM};
	
	public EffectType getType();
	
}
