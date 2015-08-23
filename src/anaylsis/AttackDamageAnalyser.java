package anaylsis;

import game.Game;
import game.Turn;
import game.Game.Player;
import game.playeraction.AttackAction;
import game.playeraction.AttackEffect;
import game.playeraction.DamageEffect;
import game.playeraction.PlayerAction;
import game.playeraction.AttackEffect.EffectType;
import game.playeraction.PlayerAction.ActionType;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ui.MainFrame;
import util.CCStatisticsUtil;
import util.TableLayout;

public class AttackDamageAnalyser implements Analyser {

	@Override
	public String getName() {
		return "attack damage";
	}

	@Override
	public JComponent analyse(Collection<Game> games) {
		String attackName = (String) JOptionPane.showInputDialog(null, "Attack", getName(), JOptionPane.PLAIN_MESSAGE, null, null, "Hyper Beam");
		
		if(attackName == null){
			return null;
		}
		
		long t1 = System.currentTimeMillis();
		
		attackName = CCStatisticsUtil.wordsToUpperCase(attackName);
	
		double damageAverage = doAnalyse(games, attackName);
		damageAverage = Math.round(damageAverage);


		JPanel resultPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setVgap(10);
		resultPanel.setLayout(layout);
		
		JLabel attackLabel = new JLabel(attackName);
		attackLabel.setHorizontalAlignment(JLabel.CENTER);
		attackLabel.setHorizontalTextPosition(JLabel.CENTER);
		resultPanel.add(attackLabel, BorderLayout.PAGE_START);
		
		JPanel result = new JPanel(new TableLayout(2));
		result.add(new JLabel("average damage"));
		JLabel damageLabel = new JLabel(damageAverage + "%");
		damageLabel.setForeground(CCStatisticsUtil.getPercentageColor(damageAverage, CCStatisticsUtil.HIGH_IS_GOOD));
		result.add(damageLabel);
		
		resultPanel.add(result);
		
		long t2 = System.currentTimeMillis();
		MainFrame.debugPrint(getName() + " analysis took " + (t2 - t1) + " milliseconds");
		
		return resultPanel;
	}
	
	private double doAnalyse(Collection<Game> games, String attackName) {
		double damageTotal = 0.0;
		int occurences = 0;
		
		for(Game game:games){
			for(Turn turn:game.getHistory()){
				PlayerAction action = turn.getPlayerAction(Player.PLAYER_ONE);
				if(actionMatches(action, attackName)){
					occurences++;
					List<AttackEffect> effects = ((AttackAction)action).getEffects(EffectType.DAMAGE);
					for(AttackEffect effect:effects){
						damageTotal += ((DamageEffect)effect).getDamage();
					}
				}
				
				action = turn.getPlayerAction(Player.PLAYER_TWO);
				if(actionMatches(action, attackName)){
					occurences++;
					List<AttackEffect> effects = ((AttackAction)action).getEffects(EffectType.DAMAGE);
					for(AttackEffect effect:effects){
						damageTotal += ((DamageEffect)effect).getDamage();
					}
				}
			}
		}
		return occurences > 0 ? damageTotal/occurences : 0.0;
	}

	private boolean actionMatches(PlayerAction action, String attackName){
		return action != null && action.getType() == ActionType.ATTACK && ((AttackAction)action).getAttack().equals(attackName);
	}

}
