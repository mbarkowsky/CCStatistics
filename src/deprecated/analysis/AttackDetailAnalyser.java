package deprecated.analysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import deprecated.game.Game;
import deprecated.game.Turn;
import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;
import deprecated.game.playeraction.DamageEffect;
import deprecated.game.playeraction.PlayerAction;
import deprecated.game.playeraction.AttackEffect.EffectType;
import deprecated.game.playeraction.PlayerAction.ActionType;
import threshold.AvgDevThreshold;
import threshold.Threshold;
import util.CCStatisticsUtil;
import util.IndentedBoxLayout;
import util.TableLayout;

public class AttackDetailAnalyser extends Analyser {
	
	private JPanel ui;
	private JTextField attackNameField;
	
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
	
	public AttackDetailAnalyser() {
		initializeUI();
	}
	
	private void initializeUI() {
		ui = new JPanel(new BorderLayout());
		ui.add(checkBox, BorderLayout.PAGE_START);
		
		JPanel textFieldPanel = new JPanel(new IndentedBoxLayout(10));
		
		attackNameField = new JTextField();
		attackNameField.setText("Hyper Beam");
		attackNameField.setPreferredSize(new Dimension(100, 20));
		attackNameField.setHorizontalAlignment(JTextField.CENTER);
		textFieldPanel.add(attackNameField);
		
		ui.add(textFieldPanel);
		
		checkBoxToggled();
	}

	@Override
	public JComponent getUI(){
		return ui;
	}
	
	@Override
	public String getName() {
		return "attack details";
	}

	@Override
	protected void checkBoxToggled(){
		attackNameField.setEnabled(checkBox.isSelected());
	}
	
	@Override
	protected JComponent doAnalyse(Collection<Game> games, String playerName) {
		String attackName = getSelectedAttackName();
		
		if(attackName == null){
			return null;
		}
		
		attackName = CCStatisticsUtil.wordsToUpperCase(attackName);
		Result result = createResult(games, attackName, playerName);
		
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
		Color damageLabelColor = CCStatisticsUtil.getThresholdColor(averageDamage, (AvgDevThreshold)Threshold.getThreshold("damage average"), CCStatisticsUtil.HIGH_IS_GOOD);
		damageLabel.setForeground(damageLabelColor);
		results.add(damageLabel);
		
		results.add(new JLabel("win rate"));
		int wins = result.getWins();
		int winRate = occurrencesTotal > 0 ? Math.round(100.f * wins / gamesOccured) : 0;
		JLabel winRateLabel = new JLabel(Integer.toString(winRate) + "%");
		Color winRateLabelColor = CCStatisticsUtil.getThresholdColor(winRate, (AvgDevThreshold)Threshold.getThreshold("win rate"), CCStatisticsUtil.HIGH_IS_GOOD);
		winRateLabel.setForeground(winRateLabelColor);
		results.add(winRateLabel);
		
		resultPanel.add(results, BorderLayout.CENTER);
		
		return resultPanel;
	}

	private String getSelectedAttackName() {
		return attackNameField.getText();
	}

	private Result createResult(Collection<Game> games, String attackName, String playerName) {
		Result result = new Result();
		double damageTotal = 0.0;
		for(Game game:games){
			List<Player> players = new LinkedList<>();
			if(playerName.equals("")){
				players.add(Player.PLAYER_ONE);
				players.add(Player.PLAYER_TWO);
			}
			else{
				Player player = game.getPlayer(playerName);
				if(player == null){
					continue;
				}
				players.add(player);	
			}
			
			Player winner = game.getWinner();
			Set<Player> users = new HashSet<>();
			boolean won = false;
			List<Turn> turns = game.getTurns();
			for(Turn turn:turns){
				for(Player player:players){
					PlayerAction playerAction = turn.getPlayerAction(player);
					if(actionMatches(playerAction, attackName)){
						users.add(player);
						result.addOccurrence();
						for(AttackEffect effect:((AttackAction)playerAction).getEffects(EffectType.DAMAGE)){
							damageTotal += ((DamageEffect)effect).getDamage();
						}
						if(player == winner){
							won = true;
						}
					}
				}
			}
			for(int i = 0; i < users.size(); i++){
				result.addUser();
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
