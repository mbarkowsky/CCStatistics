package analysis.colorrule;

import java.awt.Color;

import deprecated.game.playeraction.PlayerAction;
import util.CCStatisticsUtil;

public class ActionTypeColorRule implements ColorRule {

	@Override
	public Color getActionColor(PlayerAction action) {
		if(action == null){
			return CCStatisticsUtil.badColor;
		}
		else{
			if(action.getType() == PlayerAction.ActionType.ATTACK){
				return CCStatisticsUtil.goodColor;
			}
			else{
				return CCStatisticsUtil.neutralColor;
			}
		}
	}

	@Override
	public String getName() {
		return "action types";
	}

}
