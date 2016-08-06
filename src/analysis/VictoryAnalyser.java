package analysis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.GameXML;
import game.GameXML.Player;
import util.CCStatisticsUtil;
import util.TableLayout;

public class VictoryAnalyser extends AnalyserXML {

	private class Result{
		
		protected Map<String, Integer> gamesPlayed;
		protected Map<String, Integer> gamesWon;
		
		protected Result(){
			gamesPlayed = new HashMap<>();
			gamesWon = new HashMap<>();
		}
		
		protected void addWin(String playerName){
			doAddGame(playerName);
			doAddWin(playerName);
		}

		protected void addLoss(String playerName){
			doAddGame(playerName);
		}
		
		private void doAddGame(String playerName) {
			int games;
			if(!gamesPlayed.containsKey(playerName)){
				games = 0;
			}
			else{
				games = gamesPlayed.get(playerName);
			}
			gamesPlayed.put(playerName, games + 1);
		}
		
		private void doAddWin(String playerName) {
			int wins;
			if(!gamesWon.containsKey(playerName)){
				wins = 0;
			}
			else{
				wins = gamesWon.get(playerName);
			}
			gamesWon.put(playerName, wins + 1);
		}
		
	}
	
	private class ResultComparator implements Comparator<String>{
		
		Result result;
		
		public ResultComparator(Result result){
			this.result = result;
		}

		@Override
		public int compare(String o1, String o2) {
			int played1 = result.gamesPlayed.get(o1);
			int won1 = result.gamesWon.containsKey(o1)? result.gamesWon.get(o1) : 0;
			int winRate1 = Math.round(100.f * won1 / played1);
			
			int played2 = result.gamesPlayed.get(o2);
			int won2 = result.gamesWon.containsKey(o2)? result.gamesWon.get(o2) : 0;
			int winRate2 = Math.round(100.f * won2 / played2);
			
			return (-1) * (winRate1 - winRate2);
		}
		
		
	}
	
	@Override
	public String getName() {
		return "victories";
	}

	@Override
	protected JComponent doAnalyse(Collection<GameXML> games, String playerName) {		
		Result result = createResult(games, playerName);

		JPanel resultPanel = new JPanel();		
		resultPanel.setLayout(new TableLayout(2));
		
		List<String> players = new ArrayList<>(result.gamesPlayed.keySet());
		Comparator<String> comparator = new ResultComparator(result);
		
		Collections.sort(players, comparator);
		
		for(String player:players){
			int played = result.gamesPlayed.get(player);
			int won = result.gamesWon.containsKey(player)? result.gamesWon.get(player) : 0;
			int winRate = Math.round(100.f * won / played);
			
			JLabel playerLabel = new JLabel(player);
			resultPanel.add(playerLabel);
			
			JLabel winRateLabel = new JLabel(winRate + "%");
			Color labelColor = CCStatisticsUtil.getPercentageColor(winRate, CCStatisticsUtil.HIGH_IS_GOOD);
			winRateLabel.setForeground(labelColor);
			resultPanel.add(winRateLabel);
		}
		
		return resultPanel;
	}

	private Result createResult(Collection<GameXML> games, String playerName) {
		Result result = new Result();
		for(GameXML game:games){
			Player winningPlayer = game.getWinner();
			Player losingPlayer = game.getLoser();
			String winner = game.getPlayerName(winningPlayer);
			String loser = game.getPlayerName(losingPlayer);
			
			if(playerName.equals("") || playerName.equals(winner)){
				result.addWin(winner);	
			}
			if(playerName.equals("") || playerName.equals(loser)){
				result.addLoss(loser);	
			}
		}
		return result;
	}

}
