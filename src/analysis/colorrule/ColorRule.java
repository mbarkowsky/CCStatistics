package analysis.colorrule;

import java.awt.Color;

import deprecated.game.playeraction.PlayerAction;

public interface ColorRule {

	public abstract Color getActionColor(PlayerAction action);

	public abstract String getName();
	
}
