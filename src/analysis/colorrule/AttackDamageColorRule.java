package analysis.colorrule;

import game.playeraction.AttackAction;
import game.playeraction.AttackEffect;
import game.playeraction.DamageEffect;
import game.playeraction.PlayerAction;
import game.playeraction.AttackEffect.EffectType;
import game.playeraction.PlayerAction.ActionType;

import java.awt.Color;

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
