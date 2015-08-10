package game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Game {

	public enum Player {PLAYER_ONE, PLAYER_TWO};
	
	private Map<Player, String> playerNames;
	private Player winner;
	private List<Turn> history;
	
	public Game(){
		playerNames = new HashMap<>();
		history = new LinkedList<>();
	}
	
	public List<PlayerAction> getPlayerActions(Player player){
		List<PlayerAction> actions = new LinkedList<PlayerAction>();
		for(Turn turn:history){
			actions.add(turn.getPlayerAction(player));
		}
		return actions;
	}
	
	public List<Turn> getHistory() {
		return history;
	}

	public void addTurn(Turn turn) {
		history.add(turn);
	}
	
	public String getPlayerName(Player player) {
		return playerNames.get(player);
	}
	
	public void setPlayerName(Player player, String playerName) {
		playerNames.put(player, playerName);
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public Player getLoser(){
		return winner == Player.PLAYER_ONE ? Player.PLAYER_TWO : Player.PLAYER_ONE;
	}
	
	public void setWinner(Player winner) {
		this.winner = winner;
	}
	
	
}
