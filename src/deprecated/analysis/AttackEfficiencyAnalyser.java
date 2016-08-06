package deprecated.analysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import deprecated.game.Game;
import deprecated.game.Turn;
import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;
import deprecated.game.playeraction.DamageEffect;
import deprecated.game.playeraction.PlayerAction;
import deprecated.game.playeraction.AttackEffect.EffectType;
import deprecated.game.playeraction.PlayerAction.ActionType;
import threshold.Threshold;
import util.CCStatisticsUtil;
import util.IndentedBoxLayout;
import util.TableLayout;

public class AttackEfficiencyAnalyser extends Analyser {

	private static List<String> criterias;
	
	private static List<String> getPossibleSortingCriterias() {
		if(criterias == null){
			criterias = new LinkedList<>();
			criterias.add("occurences");
			criterias.add("damage average");
			criterias.add("damage total");
			criterias.add("win rate");
		}
		return criterias;
	}
	
	private JPanel ui;
	private ButtonGroup buttonGroup;
	
	private class Result{
		
		protected int occurences;
		protected int wins;
		protected int users;
		protected double damageTotal;
		
		public Result(){
			this.occurences = 0;
			this.wins = 0;
			this.users = 0;
			this.damageTotal = 0;
		}
		
	}
	
	private class AttackComparator implements Comparator<String>{
		
		private Map<String, Double> damageAverages;
		
		public AttackComparator(Map<String, Double> damageAverages){
			this.damageAverages = damageAverages;
		}
		
		@Override
		public int compare(String o1, String o2) {
			return (-1) * signum(damageAverages.get(o1) - damageAverages.get(o2));
		}

		private int signum(double d) {
			if(d > 0){
				return 1;
			}
			else if( d == 0){
				return 0;
			}
			else{
				return -1;
			}
		}
		
	}
	
	public AttackEfficiencyAnalyser(){
		initialiseUI();
	}
	
	private void initialiseUI() {
		ui = new JPanel(new BorderLayout());
		ui.add(checkBox, BorderLayout.PAGE_START);
		
		JPanel buttonPanel = new JPanel(new IndentedBoxLayout(10));
		buttonGroup = new ButtonGroup();
		boolean selected = true;
		for(String criteria:getPossibleSortingCriterias()){
			JRadioButton radioButton = new JRadioButton();
			radioButton.setText(criteria);
			radioButton.setActionCommand(criteria);
			if(selected){
				radioButton.setSelected(selected);
				selected = false;
			}
			buttonGroup.add(radioButton);
			buttonPanel.add(radioButton);
		}
		ui.add(buttonPanel, BorderLayout.CENTER);
		
		checkBoxToggled();
	}

	@Override
	protected void checkBoxToggled(){
		Enumeration<AbstractButton> buttons = buttonGroup.getElements();
		AbstractButton button;
		while(buttons.hasMoreElements()){
			button = buttons.nextElement();
			button.setEnabled(checkBox.isSelected());
		}
	}
	
	@Override
	public String getName() {
		return "attack efficiency";
	}
	
	@Override
	public JComponent getUI(){
		return ui;
	}

	private String getSelectedCriteria(){
		return buttonGroup.getSelection().getActionCommand();
	}
	
	private Map<String, Double> getCriteriaResults(String criteria, Map<String, Result> results) {
		switch(criteria){
			case "occurences":
				return getOccurencesResults(results);
			case "damage average":
				return getDamageAverageResults(results);
			case "damage total":
				return getDamageTotalResults(results);
			case "win rate":
				return getWinRateResults(results);
			default:
				return getDamageAverageResults(results);
		}
	}

	private Map<String, Double> getOccurencesResults(Map<String, Result> results) {
		Map<String, Double> occurences = new HashMap<>();
		for(Entry<String, Result> entry:results.entrySet()){
			Result result = entry.getValue();
			double occs = result.occurences;
			occurences.put(entry.getKey(), occs);
		}
		return occurences;
	}

	private Map<String, Double> getDamageAverageResults(Map<String, Result> results) {
		Map<String, Double> damageAverages = new HashMap<>();
		for(Entry<String, Result> entry:results.entrySet()){
			Result result = entry.getValue();
			double damageAverage = result.damageTotal / result.occurences;
			damageAverages.put(entry.getKey(), damageAverage);
		}
		return damageAverages;
	}
	
	private Map<String, Double> getDamageTotalResults(Map<String, Result> results) {
		Map<String, Double> damageTotals = new HashMap<>();
		for(Entry<String, Result> entry:results.entrySet()){
			Result result = entry.getValue();
			damageTotals.put(entry.getKey(), result.damageTotal);
		}
		return damageTotals;
	}
	
	private Map<String, Double> getWinRateResults(Map<String, Result> results) {
		Map<String, Double> winRates = new HashMap<>();
		for(Entry<String, Result> entry:results.entrySet()){
			Result result = entry.getValue();
			double winRate = 100 * result.wins / result.users;
			winRates.put(entry.getKey(), winRate);
		}
		return winRates;
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games, String playerName) {
		Map<String, Result> results = createResults(games, playerName);
		String criteria = getSelectedCriteria();
		Map<String, Double> criteriaResults = getCriteriaResults(criteria, results);
		
		List<String> efficiencyRanking = new ArrayList<String>(results.keySet());
		Comparator<String> c = new AttackComparator(criteriaResults);
		Collections.sort(efficiencyRanking, c);
		
		int[] columnTypes = {TableLayout.MINIMUM_COLUMN_WIDTH, TableLayout.SCALING_COLUMN_WIDTH, TableLayout.SCALING_COLUMN_WIDTH};
		JPanel ranking = new JPanel(new TableLayout(3, columnTypes));
		for(int i = 0; i < 10 && i < efficiencyRanking.size(); i++){
			String attack = efficiencyRanking.get(i);
			
			JLabel rankingLabel = new JLabel((i+1) + "  ");
			ranking.add(rankingLabel);
			
			JLabel nameLabel = new JLabel(attack);
			Font nameFont = rankingLabel.getFont().deriveFont(Font.PLAIN);
			nameLabel.setFont(nameFont);
			ranking.add(nameLabel);
			
			long resultWhole = Math.round(criteriaResults.get(attack));
			JLabel resultLabel = new JLabel();
			Color resultLabelColor = CCStatisticsUtil.getThresholdColor(resultWhole, Threshold.getThreshold(criteria), CCStatisticsUtil.HIGH_IS_GOOD);
			
			String resultString = getResultString(resultWhole, criteria);
			resultLabel.setText(resultString);
			resultLabel.setForeground(resultLabelColor);
			ranking.add(resultLabel);
		}
		
		return ranking;
	}

	private Map<String, Result> createResults(Collection<Game> games, String playerName) {
		Map<String, Result> results = new HashMap<>();
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
			
			Map<Player, Set<String>> playerUsages = new HashMap<>();
			for(Player player:players){
				playerUsages.put(player, new HashSet<String>());
			}
			
			for(Turn turn:game.getTurns()){
				for(Player player:players){
					PlayerAction playerAction = turn.getPlayerAction(player);
					if(playerAction != null && playerAction.getType() == ActionType.ATTACK){
						String attack = ((AttackAction)playerAction).getAttack();
						if(!results.containsKey(attack)){
							results.put(attack, new Result());
						}
						if(!playerUsages.get(player).contains(attack)){
							playerUsages.get(player).add(attack);
						}
			
						Result result = results.get(attack);
						result.occurences++;
						
						double damage = 0.0;
						for(AttackEffect effect:((AttackAction)playerAction).getEffects(EffectType.DAMAGE)){
							damage += ((DamageEffect)effect).getDamage();
						}
						result.damageTotal += damage;
					}	
				}
			}
			
			Player winner = game.getWinner();
			if(playerUsages.containsKey(winner)){
				for(String attack:playerUsages.get(winner)){
					results.get(attack).users++;
					results.get(attack).wins++;
				}	
			}
			
			Player loser = game.getLoser();
			if(playerUsages.containsKey(loser)){
				for(String attack:playerUsages.get(loser)){
					results.get(attack).users++;
				}	
			}
			
		}
		return results;
	}
	
	private String getResultString(long resultWhole, String criteria) {
		switch (criteria) {
			case "occurences":
				return "" + resultWhole;
			case "damage average":
				return resultWhole + "%";
			case "damage total":
				return resultWhole + "%";
			case "win rate":
				return resultWhole + "%";
			default:
				return "" + resultWhole;
		}
	}

}
