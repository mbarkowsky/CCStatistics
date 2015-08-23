package game.playeraction;

public interface AttackEffect {

	public enum EffectType {DAMAGE, RECOIL, HEALING, BOOST, DROP, STATUS, ITEM};
	
	public EffectType getType();
	
}
