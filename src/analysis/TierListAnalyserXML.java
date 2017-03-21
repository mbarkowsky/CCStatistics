package analysis;

import game.GameXML;
import game.GameXML.Player;
import game.TurnXML;
import game.event.EventXML;
import game.event.HealthEvent;
import game.event.MoveEventXML;
import game.event.StartEventXML;
import game.event.StatusEventXML;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import util.GameUtil;
import util.IndentedBoxLayout;
import util.GameUtil.Status;
import util.TableLayout;

public class TierListAnalyserXML extends AnalyserXML {
	
	private static final Color sTierColor =  Color.BLUE;
	private static final Color aTierColor = Color.GREEN.darker();
	private static final Color bTierColor = Color.BLACK;
	private static final Color cTierColor = Color.RED;
	
	protected class Result{
		
		protected Result(String pokemon){
			this.pokemon = pokemon;
		}
		
		protected String pokemon;
		protected int score = 0;
		protected int appearances = 0;
		
		public void addAppearance() {
			appearances++;
		}
		
		public void addScore(int score) {
			this.score += score;
		}
	}

	private JPanel ui;
	private JTextField thresholdField;
	
	public TierListAnalyserXML(){
		initialiseUI();
	}
	
	private void initialiseUI() {
		ui = new JPanel(new BorderLayout());
		ui.add(checkBox, BorderLayout.PAGE_START);
		
		JPanel textBoxPanel = new JPanel(new IndentedBoxLayout(10));
		
		thresholdField = new JTextField();
		thresholdField.setText("10");
		thresholdField.setPreferredSize(new Dimension(100, 20));
		thresholdField.setHorizontalAlignment(JTextField.CENTER);
		textBoxPanel.add(thresholdField);
		
		ui.add(textBoxPanel, BorderLayout.CENTER);
		
		checkBoxToggled();
	}
	
	@Override
	protected void checkBoxToggled(){
		thresholdField.setEnabled(checkBox.isSelected());
	}
	
	private int getThreshold(){
		int threshold;
		try{
			threshold = Integer.parseInt(thresholdField.getText());
		}
		catch(NumberFormatException e){
			threshold = 0;
		}
		return threshold;
	}
	
	@Override
	public JComponent getUI(){
		return ui;
	}

	@Override
	public String getName() {
		return "tier list";
	}

	@Override
	protected JComponent doAnalyse(Collection<GameXML> games, String playerName) {
		Map<String, Result> scores = calculateResult(games);
		int threshold = getThreshold();
		List<Result> ranking = new ArrayList<>();
		for(Result score:scores.values()){
			if(score.appearances >= threshold){
				ranking.add(score);
			}
		}
		Collections.sort(ranking, new Comparator<Result>(){

			@Override
			public int compare(Result o1, Result o2) {
				double averageO1 = o1.score / o1.appearances;
				double averageO2 = o2.score / o2.appearances;
				return (-1) * Double.compare(averageO1, averageO2);
			}
			
		});
		
		int[] columnTypes = {TableLayout.MINIMUM_COLUMN_WIDTH, TableLayout.SCALING_COLUMN_WIDTH, TableLayout.SCALING_COLUMN_WIDTH};
		JPanel result = new JPanel(new TableLayout(3, columnTypes));
		
		int i = 0;
		JLabel sTierLabel = new JLabel("S-Tier");
		sTierLabel.setForeground(sTierColor);
		sTierLabel.setFont(sTierLabel.getFont().deriveFont(24.f));
		result.add(new JLabel());
		result.add(sTierLabel);
		result.add(new JLabel());
		while(ranking.get(i).score / ranking.get(i).appearances >= 200 && i < ranking.size()){
			addPokemon(i, ranking.get(i), result, sTierColor);
			i++;
		}
		JLabel aTierLabel = new JLabel("A-Tier");
		aTierLabel.setForeground(aTierColor);
		aTierLabel.setFont(aTierLabel.getFont().deriveFont(24.f));
		result.add(new JLabel());
		result.add(aTierLabel);
		result.add(new JLabel());
		while(ranking.get(i).score / ranking.get(i).appearances >= 125 && i < ranking.size()){
			addPokemon(i, ranking.get(i), result, aTierColor);
			i++;
		}
		JLabel bTierLabel = new JLabel("B-Tier");
		bTierLabel.setForeground(bTierColor);
		bTierLabel.setFont(bTierLabel.getFont().deriveFont(24.f));
		result.add(new JLabel());
		result.add(bTierLabel);
		result.add(new JLabel());
		while(ranking.get(i).score / ranking.get(i).appearances >= 75 && i < ranking.size()){
			addPokemon(i, ranking.get(i), result, bTierColor);
			i++;
		}
		JLabel cTierLabel = new JLabel("C-Tier");
		cTierLabel.setForeground(cTierColor);
		cTierLabel.setFont(cTierLabel.getFont().deriveFont(24.f));
		result.add(new JLabel());
		result.add(cTierLabel);
		result.add(new JLabel());
		while(i < ranking.size()){
			addPokemon(i, ranking.get(i), result, cTierColor);
			i++;
		}

		JScrollPane scrollPane = new JScrollPane(result);
		
		return scrollPane;
	}

	private void addPokemon(int index, Result result, JPanel resultPanel, Color color) {
		String pokemon = result.pokemon;
		
		JLabel rankingLabel = new JLabel((index+1) + "  ");
		resultPanel.add(rankingLabel);
		
		JLabel nameLabel = new JLabel(pokemon);
		Font nameFont = rankingLabel.getFont().deriveFont(Font.PLAIN);
		nameLabel.setFont(nameFont);
		resultPanel.add(nameLabel);
		
		int scoreAverage = result.score / result.appearances;
		JLabel resultLabel = new JLabel(Integer.toString(scoreAverage));
		resultLabel.setForeground(color);
		resultPanel.add(resultLabel);
	}

	private Map<String, Result> calculateResult(Collection<GameXML> games) {
		Map<String, Result> scores = new HashMap<>();
		for(GameXML game:games){
			Map<String, Integer>[] scoresForGame = getScoresForGame(game);	
			
			for(Map<String, Integer> playerGameScores:scoresForGame){
				for(Entry<String, Integer> entry:playerGameScores.entrySet()){
					String pokemon = entry.getKey();
					if(!scores.containsKey(pokemon)){
						scores.put(pokemon, new Result(pokemon));
					}
					Result result = scores.get(pokemon);
					result.addAppearance();
					result.addScore(entry.getValue());
				}
			}
		}
	
		return scores;
	}

	private Map<String, Integer>[] getScoresForGame(GameXML game) {
		@SuppressWarnings("unchecked")
		Map<String, Integer>[] scores = new Map[2];
		scores[0] = new HashMap<String, Integer>();
		scores[1] = new HashMap<String, Integer>();
		
		List<TurnXML> turns = game.getTurns();
		for(int turnIndex = 0; turnIndex < turns.size(); turnIndex++){
			TurnXML turn = turns.get(turnIndex);
			List<EventXML> events = turn.getEvents();
			for(int eventIndex = 0; eventIndex < events.size(); eventIndex++){
				EventXML event = events.get(eventIndex);
				if(event.isMoveEvent()){
					Player owner = ((MoveEventXML)event).getOwner();
					String pokemon = ((MoveEventXML)event).getPokemon();
					int score = getScoreForMove((MoveEventXML)event, game, turnIndex, eventIndex);
					
					int playerKey = owner == Player.PLAYER_ONE ? 0 : 1;
					if(!scores[playerKey].containsKey(pokemon)){
						scores[playerKey].put(pokemon, 0);
					}
					int scoreTotal = scores[playerKey].get(pokemon) + score;
					scores[playerKey].put(pokemon, scoreTotal);
				}
			}
		}
		
		return scores;
	}

	private int getScoreForMove(MoveEventXML event, GameXML game, int turnIndex, int eventIndex) {
		int damageScore = getDamageScoreForMove(event, game, turnIndex, eventIndex);
		int statusScore = getStatusScoreForMove(event);
		return damageScore + statusScore;
	}

	private int getStatusScoreForMove(MoveEventXML event) {
		int statusScore = 0;
		for(EventXML effect:event.getEffects()){
			if(effect.isStatusEvent() && ((StatusEventXML)effect).getOwner() == GameXML.getOpponent(event.getOwner())){
				Status status = ((StatusEventXML)effect).getStatus();
				if(GameUtil.isMajorStatus(status)){
					statusScore += 50;
				}
				else if(GameUtil.isMinorStatus(status)){
					statusScore += 20;
				}
			}
			else if(effect.isStartEvent() && ((StartEventXML)effect).getStart().equals("confusion") && ((StartEventXML)effect).getOwner() == GameXML.getOpponent(event.getOwner())){
				statusScore += 20;
			}
		}
		return statusScore;
	}

	private int getDamageScoreForMove(MoveEventXML event, GameXML game, int turnIndex, int eventIndex) {
		int damage = 0;
		HealthEvent lastEnemyHealthEvent = ((MoveEventXML)event).getLastEnemyHealthEvent();
		if(lastEnemyHealthEvent != null){
			damage = game.getLastHealth(lastEnemyHealthEvent.getOwner(), lastEnemyHealthEvent.getPokemon(), turnIndex, eventIndex)- lastEnemyHealthEvent.getHealth();
		}
		return damage;
	}

}
