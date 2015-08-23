package game.playeraction;

public class HealingEffect implements AttackEffect {

	@Override
	public EffectType getType() {
		return EffectType.HEALING;
	}

	public String toString(){
		return getType().toString();
	}
	
}
