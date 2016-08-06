package deprecated.builder.attackbuilder;

import java.util.List;

import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;
import deprecated.game.playeraction.ItemEffect;

public class KnockOffAttackBuilder extends GenericAttackBuilder {

	@Override
	public List<AttackEffect> buildEffects(Player player, AttackAction action, String[] log, int lastIndex) {
		List<AttackEffect> effects = super.buildEffects(player, action, log, lastIndex);
		String line = log[this.lastIndex];
		if(line.contains("knocked off")){
			String item = line.substring(line.lastIndexOf('\'') + 3, line.length() - 1);
			ItemEffect effect = new ItemEffect(item);
			effects.add(effect);
			this.lastIndex++;
		}
		return effects;
	}
	
}
