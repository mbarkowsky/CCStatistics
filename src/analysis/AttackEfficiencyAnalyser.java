package analysis;

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
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import threshold.AvgDevThreshold;
import threshold.Threshold;
import util.CCStatisticsUtil;
import util.IndentedBoxLayout;
import util.TableLayout;

public class AttackEfficiencyAnalyser extends Analyser {

	private static List<String> criterias;
	
	private static List<String> getPossibleSortingCriterias() {
		if(criterias == null){
			criterias = new LinkedList<>();
			criterias.add("damage average");
			criterias.add("damage total");
			criterias.add("win rate");
		}
		return criterias;
	}
	
	private JPanel ui;
	private ButtonGroup buttons;
	
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
		buttons = new ButtonGroup();
		boolean selected = true;
		for(String criteria:getPossibleSortingCriterias()){
			JRadioButton radioButton = new JRadioButton();
			radioButton.setText(criteria);
			radioButton.setActionCommand(criteria);
			if(selected){
				radioButton.setSelected(selected);
				selected = false;
			}
			buttons.add(radioButton);
			buttonPanel.add(radioButton);
		}
		ui.add(buttonPanel, BorderLayout.CENTER);
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
		return buttons.getSelection().getActionCommand();
	}
	
	private Map<String, Double> getCriteriaResults(String criteria, Map<String, Result> results) {
		switch(criteria){
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
	protected JComponent doAnalyse(Collection<Game> games) {
		Map<String, Result> results = createResults(games);
		String criteria = getSelectedCriteria();
		Map<String, Double> criteriaResults = getCriteriaResults(criteria, results);
		
		List<String> efficiencyRanking = new ArrayList<String>(results.keySet());
		Comparator<String> c = new AttackComparator(criteriaResults);
		Collections.sort(efficiencyRanking, c);
		
		JPanel result = new JPanel(new BorderLayout());
		JLabel criteriaLabel = new JLabel(criteria);
		result.add(criteriaLabel, BorderLayout.PAGE_START);
		
		JPanel ranking = new JPanel(new TableLayout(2));
		for(int i = 0; i < 10; i++){
			String attack = efficiencyRanking.get(i);
			JLabel nameLabel = new JLabel((i+1) + ".: " + attack);
			ranking.add(nameLabel);
			
			long resultWhole = Math.round(criteriaResults.get(attack));
			JLabel resultLabel = new JLabel();
			Color resultLabelColor = CCStatisticsUtil.getAvgDevColor(resultWhole, (AvgDevThreshold)Threshold.getThreshold(criteria), CCStatisticsUtil.HIGH_IS_GOOD);
			resultLabel.setText(resultWhole + "%");
			resultLabel.setForeground(resultLabelColor);	//TODO
			ranking.add(resultLabel);
		}
		return ranking;
	}

	private Map<String, Result> createResults(Collection<Game> games) {
		Map<String, Result> results = new HashMap<>();
		Player[] players = {Player.PLAYER_ONE, Player.PLAYER_TWO};
		for(Game game:games){
			Map<Player, Set<String>> playerUsages = new HashMap<>();;
			playerUsages.put(Player.PLAYER_ONE, new HashSet<String>());
			playerUsages.put(Player.PLAYER_TWO, new HashSet<String>());
			
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
			for(String attack:playerUsages.get(winner)){
				results.get(attack).users++;
				results.get(attack).wins++;
			}
			
			Player loser = game.getLoser();
			for(String attack:playerUsages.get(loser)){
				results.get(attack).users++;
			}
		}
		return results;
	}

}
