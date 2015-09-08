package analysis.colorrule;

import game.playeraction.PlayerAction;

import java.awt.Color;

public interface ColorRule {

	public abstract Color getActionColor(PlayerAction action);

	public abstract String getName();
	
}
