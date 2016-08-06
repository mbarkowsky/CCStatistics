package deprecated.game.playeraction;

import util.GameUtil.Stat;

public class BoostEffect implements AttackEffect {
	
	private Stat stat;
	
	@Override
	public EffectType getType() {
		return EffectType.BOOST;
	}

	public Stat getStat() {
		return stat;
	}

	public void setStat(Stat stat) {
		this.stat = stat;
	}

	public String toString(){
		return getType() + ": " + stat;
	}
}
