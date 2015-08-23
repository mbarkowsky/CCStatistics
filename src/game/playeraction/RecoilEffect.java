package game.playeraction;

public class RecoilEffect implements AttackEffect {

	@Override
	public EffectType getType() {
		return EffectType.RECOIL;
	}

	public String toString(){
		return getType().toString();
	}
}
