package anaylsis;

import game.Game;
import game.Game.Player;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

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
	public JPanel analyse(List<Game> games) {
		Result result = doAnalyse(games);
		
		JPanel resultPanel = new JPanel();
		
		resultPanel.setLayout(new GridLayout(0, 2));
		for(String player:result.gamesPlayed.keySet()){
			int played = result.gamesPlayed.get(player);
			int won = result.gamesWon.containsKey(player)? result.gamesWon.get(player) : 0;
			int winRate = Math.round(100.f * won / played);
			
			resultPanel.add(new JLabel(player));
			resultPanel.add(new JLabel(winRate + "%"));
		}
		
		return resultPanel;
	}

	private Result doAnalyse(List<Game> games) {
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
