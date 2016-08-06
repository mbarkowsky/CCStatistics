package deprecated.analysis;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deprecated.game.Game;
import deprecated.game.Game.Player;
import util.CCStatisticsUtil;
import util.TableLayout;

public class VictoryAnalyser extends Analyser {

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
	
	@Override
	public String getName() {
		return "victories";
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games, String playerName) {		
		Result result = createResult(games, playerName);

		JPanel resultPanel = new JPanel();		
		resultPanel.setLayout(new TableLayout(2));
		
		for(String player:result.gamesPlayed.keySet()){
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

	private Result createResult(Collection<Game> games, String playerName) {
		Result result = new Result();
		for(Game game:games){
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
