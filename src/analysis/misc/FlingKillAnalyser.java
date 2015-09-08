package analysis.misc;

import game.Game;
import game.Turn;
import game.Game.Player;
import game.playeraction.AttackAction;
import game.playeraction.AttackEffect;
import game.playeraction.DamageEffect;
import game.playeraction.PlayerAction;
import game.playeraction.AttackEffect.EffectType;
import game.playeraction.PlayerAction.ActionType;

import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import analysis.Analyser;

public class FlingKillAnalyser extends Analyser {

	@Override
	public String getName() {
		return "fling kills";
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games) {
		int kills = 0;
		for(Game game:games){
			for(Turn turn:game.getTurns()){
				PlayerAction playerOneAction = turn.getPlayerAction(Player.PLAYER_ONE);
				if(playerOneAction != null && playerOneAction.getType() == ActionType.ATTACK && ((AttackAction)playerOneAction).getAttack().equals("Fling")){
					for(AttackEffect effect:((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE)){
						if(((DamageEffect)effect).isKO()){
							kills++;
						}
					}
				}
				
				PlayerAction playerTwoAction = turn.getPlayerAction(Player.PLAYER_TWO);
				if(playerTwoAction != null && playerTwoAction.getType() == ActionType.ATTACK && ((AttackAction)playerTwoAction).getAttack().equals("Fling")){
					for(AttackEffect effect:((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE)){
						if(((DamageEffect)effect).isKO()){
							kills++;
						}
					}
				}
			}
		}
		
		JPanel result = new JPanel();
		LayoutManager layout = new BoxLayout(result, BoxLayout.PAGE_AXIS);
		result.setLayout(layout);
		
		JLabel goliathLabel = new JLabel("Goliaths felled:");
		Font goliathFont = new Font("victim font", Font.BOLD, 24);
		goliathLabel.setFont(goliathFont);
		goliathLabel.setHorizontalAlignment(JLabel.CENTER);
		goliathLabel.setHorizontalTextPosition(JLabel.CENTER);
		goliathLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(goliathLabel);
		
		JLabel killLabel = new JLabel(Integer.toString(kills));
		Font killFont = new Font("kill font", Font.BOLD, 48);
		killLabel.setFont(killFont);
		killLabel.setHorizontalAlignment(JLabel.CENTER);
		killLabel.setHorizontalTextPosition(JLabel.CENTER);
		killLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(killLabel);
		
		return result;
	}

}
