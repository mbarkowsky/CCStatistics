package builder;

import java.util.List;

import game.Game.Player;
import game.playeraction.AttackAction;
import game.playeraction.AttackEffect;

public interface AttackBuilder {
	
	public List<AttackEffect> buildEffects(Player player, AttackAction action, String[] log, int lastIndex);
	
}
