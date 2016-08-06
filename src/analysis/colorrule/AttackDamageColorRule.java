package analysis.colorrule;

import java.awt.Color;

import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;
import deprecated.game.playeraction.DamageEffect;
import deprecated.game.playeraction.PlayerAction;
import deprecated.game.playeraction.AttackEffect.EffectType;
import deprecated.game.playeraction.PlayerAction.ActionType;

public class AttackDamageColorRule implements ColorRule {

	@Override
	public Color getActionColor(PlayerAction action) {
		int damage = 0;
		if(action != null && action.getType() == ActionType.ATTACK){
			for(AttackEffect effect:((AttackAction)action).getEffects(EffectType.DAMAGE)){
				damage += ((DamageEffect)effect).getDamage();
			}
		}
		
		int value = (int)(255 - (2.55 * damage));
		Color color = new Color(value, value, value);
		return color;
	}

	@Override
	public String getName() {
		return "attack damage";
	}

}
