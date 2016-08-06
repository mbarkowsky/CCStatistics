package analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import deprecated.game.Game;
import deprecated.game.Turn;
import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.PlayerAction;
import deprecated.game.playeraction.PlayerAction.ActionType;
import threshold.AvgDevThreshold;
import threshold.Threshold;

public class WinRateThresholdAnalyser implements ThresholdAnalyser {

	@Override
	public Threshold createThreshold(Collection<Game> samples) {		
		AvgDevThreshold threshold = new AvgDevThreshold();
		threshold.setName("win rate");
		List<Double> winRates = calculateWinRates(samples);
		double average = 0;
		for(double winRate:winRates){
			average += winRate;
		}
		average = average / winRates.size();
		double deviation = calculateDeviation(samples, average, winRates);
		threshold.setAverage(average);
		threshold.setDeviation(deviation);
		return threshold;
	}


	private List<Double> calculateWinRates(Collection<Game> samples) {
		Player[] players = {Player.PLAYER_ONE, Player.PLAYER_TWO};
		Map<Player, Set<String>> usedAttacks = new HashMap<>();
		Map<String, Integer> users = new HashMap<>();
		Map<String, Integer> wins = new HashMap<>();
		for(Game game:samples){
			usedAttacks.put(Player.PLAYER_ONE, new HashSet<String>());
			usedAttacks.put(Player.PLAYER_TWO, new HashSet<String>());
			for(Turn turn:game.getTurns()){
				for(Player player:players){
					PlayerAction playerAction = turn.getPlayerAction(player);
					if(playerAction != null && playerAction.getType() == ActionType.ATTACK){
						usedAttacks.get(player).add(((AttackAction)playerAction).getAttack());
					}
				}
			}
			for(Player player:players){
				for(String attack:usedAttacks.get(player)){
					if(!users.containsKey(attack)){
						users.put(attack, 0);
						wins.put(attack, 0);
					}
					if(player == game.getWinner()){
						users.put(attack, users.get(attack) + 1);
						wins.put(attack, wins.get(attack) + 1);
					}
					else{
						users.put(attack, users.get(attack) + 1);
					}
				}
			}
		}
		List<Double> winRates = new LinkedList<Double>();
		for(String attack:users.keySet()){
			double winRate = (100.0 * wins.get(attack)) / users.get(attack);
			winRates.add(winRate);
		}
		return winRates;
	}
	
	private double calculateDeviation(Collection<Game> samples, double average, List<Double> winRates) {
		List<Double> diffs = new LinkedList<>();
		for(double winRate:winRates){
			diffs.add((winRate - average) * (winRate - average));
		}
		double var = 0;
		for(double diff:diffs){
			var += diff;
		}
		var = diffs.size() > 0 ? var / diffs.size() : 0;
		double dev = Math.sqrt(var);
		return dev;
	}
}
