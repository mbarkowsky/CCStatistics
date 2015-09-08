package analysis;

import game.Game;
import game.Game.Player;
import game.playeraction.AttackAction;
import game.playeraction.AttackEffect;
import game.playeraction.DamageEffect;
import game.playeraction.PlayerAction;
import game.playeraction.AttackEffect.EffectType;
import game.playeraction.PlayerAction.ActionType;
import game.Turn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import threshold.AvgDevThreshold;
import threshold.Threshold;
import ui.MainFrame;
import util.CCStatisticsUtil;
import util.TableLayout;

public class AttackDetailAnalyser extends Analyser {
	
	private class Result{
		
		protected int users;
		protected int occurrences;
		protected int wins;
		private double averageDamage;
		
		protected int getUsers(){
			return users;
		}
		
		protected int getOccurrences(){
			return occurrences;
		}
		
		protected int getWins(){
			return wins;
		}
		
		protected void addUser(){
			users++;
		}
		
		protected void addOccurrence(){
			occurrences++;
		}
		
		protected void addWin(){
			wins++;
		}

		protected double getAverageDamage() {
			return averageDamage;
		}

		protected void setAverageDamage(double averageDamage) {
			this.averageDamage = averageDamage;
		}
	}
	
	@Override
	public String getName() {
		return "attack details";
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games) {
		String attackName = (String) JOptionPane.showInputDialog(null, "Attack", getName(), JOptionPane.PLAIN_MESSAGE, null, null, "Hyper Beam");
		
		if(attackName == null){
			return null;
		}
		
		long t1 = System.currentTimeMillis();
		
		attackName = CCStatisticsUtil.wordsToUpperCase(attackName);
		Result result = createResult(games, attackName);
		
		JPanel resultPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setVgap(10);
		resultPanel.setLayout(layout);
		
		JLabel attackLabel = new JLabel(attackName);
		attackLabel.setHorizontalAlignment(JLabel.CENTER);
		attackLabel.setHorizontalTextPosition(JLabel.CENTER);
		resultPanel.add(attackLabel, BorderLayout.PAGE_START);
		
		JPanel results = new JPanel(new TableLayout(2));
		
		results.add(new JLabel("games total"));
		int gamesTotal = games.size();
		results.add(new JLabel(Integer.toString(gamesTotal)));
		
		results.add(new JLabel("users total"));
		int gamesOccured = result.getUsers();
		results.add(new JLabel(Integer.toString(gamesOccured)));
		
		results.add(new JLabel("occurrences total"));
		int occurrencesTotal = result.getOccurrences();
		results.add(new JLabel(Integer.toString(occurrencesTotal)));

		results.add(new JLabel("average damage"));
		long averageDamage = Math.round(result.getAverageDamage());
		JLabel damageLabel = new JLabel(averageDamage + "%");
		Color damageLabelColor = CCStatisticsUtil.getAvgDevColor(averageDamage, (AvgDevThreshold)Threshold.getThreshold("damage average"), CCStatisticsUtil.HIGH_IS_GOOD);
		damageLabel.setForeground(damageLabelColor);
		results.add(damageLabel);
		
		results.add(new JLabel("win rate"));
		int wins = result.getWins();
		int winRate = occurrencesTotal > 0 ? Math.round(100.f * wins / gamesOccured) : 0;
		JLabel winRateLabel = new JLabel(Integer.toString(winRate) + "%");
		Color winRateLabelColor = CCStatisticsUtil.getAvgDevColor(winRate, (AvgDevThreshold)Threshold.getThreshold("win rate"), CCStatisticsUtil.HIGH_IS_GOOD);
		winRateLabel.setForeground(winRateLabelColor);
		results.add(winRateLabel);
		
		resultPanel.add(results, BorderLayout.CENTER);
		

		long t2 = System.currentTimeMillis();
		MainFrame.debugPrint(getName() + " analysis took " + (t2 - t1) + " milliseconds");
		
		return resultPanel;
	}

	private Result createResult(Collection<Game> games, String attackName) {
		Result result = new Result();
		double damageTotal = 0.0;
		for(Game game:games){
			Player winner = game.getWinner();
			boolean used[] = new boolean[2];
			boolean won = false;
			List<Turn> turns = game.getTurns();
			for(Turn turn:turns){
				PlayerAction playerOneAction = turn.getPlayerOneAction();
				if(actionMatches(playerOneAction, attackName)){
					used[0] = true;
					result.addOccurrence();
					for(AttackEffect effect:((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE)){
						damageTotal += ((DamageEffect)effect).getDamage();
					}
					if(winner == Player.PLAYER_ONE){
						won = true;
					}
				}
				
				PlayerAction playerTwoAction = turn.getPlayerTwoAction();
				if(actionMatches(playerTwoAction, attackName)){
					used[1] = true;
					result.addOccurrence();
					for(AttackEffect effect:((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE)){
						damageTotal += ((DamageEffect)effect).getDamage();
					}
					if(winner == Player.PLAYER_TWO){
						won = true;
					}
				}
			}
			for(boolean u:used){
				if(u){
					result.addUser();
				}
			}
			if(won){
				result.addWin();
			}
		}
		result.setAverageDamage(damageTotal/result.getOccurrences());
		return result;
	}
	
	private boolean actionMatches(PlayerAction action, String attackName){
		return action != null && action.getType() == ActionType.ATTACK && ((AttackAction)action).getAttack().toLowerCase().equals(attackName.toLowerCase());
	}

}
