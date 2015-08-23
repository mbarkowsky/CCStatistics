package anaylsis;

import game.Game;
import game.Game.Player;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.MainFrame;
import util.CCStatisticsUtil;
import util.TableLayout;

public class VictoryAnalyser implements Analyser {

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
	public JComponent analyse(Collection<Game> games) {
		long t1 = System.currentTimeMillis();
		
		Result result = doAnalyse(games);

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
		
		long t2 = System.currentTimeMillis();
		MainFrame.debugPrint(getName() + " analysis took " + (t2 - t1) + " milliseconds");
		
		return resultPanel;
	}

	private Result doAnalyse(Collection<Game> games) {
		Result result = new Result();
		for(Game game:games){
			Player winningPlayer = game.getWinner();
			Player losingPlayer = game.getLoser();
			String winner = game.getPlayerName(winningPlayer);
			String loser = game.getPlayerName(losingPlayer);
			
			result.addWin(winner);
			result.addLoss(loser);
		}
		return result;
	}

}
