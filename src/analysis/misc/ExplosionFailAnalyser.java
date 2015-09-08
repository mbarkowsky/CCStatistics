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
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import analysis.Analyser;

public class ExplosionFailAnalyser extends Analyser {

	private Set<String> validMoves;
	
	public ExplosionFailAnalyser() {
		validMoves = new HashSet<>();
		validMoves.add("Explosion");
		validMoves.add("Self-Destruct");
		validMoves.add("Final Gambit");
	}
	
	@Override
	public String getName() {
		return "explosion fails";
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games) {
		int fails = 0;
		for(Game game:games){
			for(Turn turn:game.getTurns()){
				PlayerAction playerOneAction = turn.getPlayerAction(Player.PLAYER_ONE);
				if(playerOneAction != null && playerOneAction.getType() == ActionType.ATTACK && validMoves.contains(((AttackAction)playerOneAction).getAttack())){
					int damage = 0;
					for(AttackEffect effect:((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE)){
						damage += ((DamageEffect)effect).getDamage();
					}
					if(damage == 0){
						fails++;
					}
				}
				
				PlayerAction playerTwoAction = turn.getPlayerAction(Player.PLAYER_TWO);
				if(playerTwoAction != null && playerTwoAction.getType() == ActionType.ATTACK && validMoves.contains(((AttackAction)playerTwoAction).getAttack())){
					int damage = 0;
					for(AttackEffect effect:((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE)){
						damage += ((DamageEffect)effect).getDamage();
					}
					if(damage == 0){
						fails++;
					}
				}
			}
		}
		
		JPanel result = new JPanel();
		LayoutManager layout = new BoxLayout(result, BoxLayout.PAGE_AXIS);
		result.setLayout(layout);
		
		JLabel sacrificesLabel = new JLabel("sacrifices in vain:");
		Font victimFont = new Font("sacrifice font", Font.BOLD, 24);
		sacrificesLabel.setFont(victimFont);
		sacrificesLabel.setHorizontalAlignment(JLabel.CENTER);
		sacrificesLabel.setHorizontalTextPosition(JLabel.CENTER);
		sacrificesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(sacrificesLabel);
		
		JLabel failLabel = new JLabel(Integer.toString(fails));
		Font killFont = new Font("fail font", Font.BOLD, 48);
		failLabel.setFont(killFont);
		failLabel.setHorizontalAlignment(JLabel.CENTER);
		failLabel.setHorizontalTextPosition(JLabel.CENTER);
		failLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(failLabel);
		
		return result;
	}

}
