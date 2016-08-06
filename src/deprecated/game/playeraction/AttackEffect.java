package deprecated.game.playeraction;

public interface AttackEffect {

	public enum EffectType {DAMAGE, RECOIL, HEALING, BOOST, DROP, STATUS, ITEM, BLOCK, SWITCH};
	
	public EffectType getType();
	
}
