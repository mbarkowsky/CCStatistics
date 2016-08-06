package deprecated.builder.attackbuilder;

import java.util.List;

import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;

public interface AttackBuilder {
	
	public List<AttackEffect> buildEffects(Player player, AttackAction action, String[] log, int lastIndex);
	
}
