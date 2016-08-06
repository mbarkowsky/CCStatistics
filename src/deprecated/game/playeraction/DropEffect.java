package deprecated.game.playeraction;

import util.GameUtil.Stat;

public class DropEffect implements AttackEffect {

	private Stat stat;
	
	@Override
	public EffectType getType() {
		return EffectType.DROP;
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
