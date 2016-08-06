package deprecated.builder.attackbuilder;

import java.util.List;
import java.util.StringTokenizer;

import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;
import deprecated.game.playeraction.SwitchEffect;

public class SwitchAttackBuilder extends GenericAttackBuilder {

	@Override
	public List<AttackEffect> buildEffects(Player player, AttackAction action, String[] log, int lastIndex) {
		List<AttackEffect> effects = super.buildEffects(player, action, log, lastIndex);
		if(log[this.lastIndex].contains("went back")){
			this.lastIndex++;
			StringTokenizer st = new StringTokenizer(log[this.lastIndex]);
			String pokemon = st.nextToken();
			while(st.hasMoreTokens()){
				pokemon = st.nextToken();
			}
			pokemon = pokemon.substring(0, pokemon.length() - 1);
			SwitchEffect switchEffect = new SwitchEffect();
			switchEffect.setPokemon(pokemon);
			effects.add(switchEffect);
			this.lastIndex++;
		}
		return effects;
	}
	
}
