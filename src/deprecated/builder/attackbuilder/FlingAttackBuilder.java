package deprecated.builder.attackbuilder;

import java.util.LinkedList;
import java.util.List;

import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;
import deprecated.game.playeraction.ItemEffect;

public class FlingAttackBuilder extends GenericAttackBuilder {

	
	@Override
	public List<AttackEffect> buildEffects(Player player, AttackAction action, String[] log, int lastIndex) {
		List<AttackEffect> effects = new LinkedList<>();
		String line = log[lastIndex];
		String[] strings = line.split("flung its ");
		if(strings.length != 2){
			return effects;
		}
		String flungItem = strings[1].substring(0, strings[1].length() - 1);
		ItemEffect itemEffect = new ItemEffect(flungItem);
		effects.add(itemEffect);
		lastIndex++;
		effects.addAll(super.buildEffects(player, action, log, lastIndex));
		return effects;
	}

}