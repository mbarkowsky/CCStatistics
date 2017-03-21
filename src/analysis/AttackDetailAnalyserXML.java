package analysis;

import game.GameXML;
import game.GameXML.Player;
import game.TurnXML;
import game.event.EventXML;
import game.event.HealthEvent;
import game.event.MoveEventXML;

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

import threshold.AvgDevThreshold;
import threshold.Threshold;
import util.CCStatisticsUtil;
import util.IndentedBoxLayout;
import util.TableLayout;

public class AttackDetailAnalyserXML extends AnalyserXML {

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
	
	public AttackDetailAnalyserXML(){
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
	protected JComponent doAnalyse(Collection<GameXML> games, String playerName) {
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
	
	private Result createResult(Collection<GameXML> games, String attackName, String playerName) {
		Result result = new Result();
		double damageTotal = 0.0;
		for(GameXML game:games){
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
			List<TurnXML> turns = game.getTurns();
			for(int turnIndex = 0; turnIndex < turns.size(); turnIndex++){
				TurnXML turn = turns.get(turnIndex);
				List<EventXML> events = turn.getEvents();
				for(int eventIndex = 0; eventIndex < events.size(); eventIndex ++){
					EventXML event = events.get(eventIndex);
					if(event.isMoveEvent() && actionMatches((MoveEventXML)event, attackName) && players.contains(((MoveEventXML)event).getOwner()) && !skip((MoveEventXML)event)){
						Player owner = ((MoveEventXML)event).getOwner();
						users.add(owner);
						result.addOccurrence();
						HealthEvent lastEnemyHealthEvent = ((MoveEventXML)event).getLastEnemyHealthEvent();
						if(lastEnemyHealthEvent != null){
							int damage = getDamage(lastEnemyHealthEvent, game, turnIndex, eventIndex);
							damageTotal += damage;	
						}
						
						if(owner == winner){
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

	private boolean actionMatches(MoveEventXML event, String attackName){
		return ((MoveEventXML)event).getMove().toLowerCase().equals(attackName.toLowerCase());
	}
	
	private int getDamage(HealthEvent lastEnemyHealthEvent, GameXML game, int turnIndex, int eventIndex){
		return game.getLastHealth(lastEnemyHealthEvent.getOwner(), lastEnemyHealthEvent.getPokemon(), turnIndex, eventIndex) - lastEnemyHealthEvent.getHealth();
	}
	
	private boolean skip(MoveEventXML event) {
		boolean skip = false;
		for(EventXML effect:event.getEffects()){
			if(effect.isPrepareEvent()){
				skip = true;
			}
		}
		return skip;
	}

}
