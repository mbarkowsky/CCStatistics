package game;

import game.playeraction.PlayerAction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Game {

	public enum Player {PLAYER_ONE, PLAYER_TWO};
	
	public static Player getOpponent(Player player) {
		return player == Player.PLAYER_ONE ? Player.PLAYER_TWO : Player.PLAYER_ONE;
	}
	
	private String name;
	private Map<Player, String> playerNames;
	private Player winner;
	private List<Turn> turns;
	
	public Game(){
		playerNames = new HashMap<>();
		turns = new LinkedList<>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PlayerAction> getPlayerActions(Player player){
		List<PlayerAction> actions = new LinkedList<PlayerAction>();
		for(Turn turn:turns){
			actions.add(turn.getPlayerAction(player));
		}
		return actions;
	}
	
	public List<Turn> getTurns() {
		return turns;
	}

	public void addTurn(Turn turn) {
		turns.add(turn);
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
	
	public String toString(){
		return name;
	}
	
	public String prettyString(){
		if(!playerNames.containsKey(Player.PLAYER_ONE) || !playerNames.containsKey(Player.PLAYER_TWO)){
			return super.toString();
		}
		else{
			StringBuffer buffer = new StringBuffer();
			buffer.append("Game between " + getPlayerName(Player.PLAYER_ONE));
			buffer.append(" and " + getPlayerName(Player.PLAYER_TWO));
			buffer.append("\n");
			for(int i = 0; i < turns.size(); i++){
				buffer.append("Turn " + (i + 1) + "\n");
				buffer.append(turns.get(i));
				buffer.append("\n");
			}
			buffer.append("Winner: " + getPlayerName(winner));
			return buffer.toString();
		}
	}

	public Player getPlayer(String playerName) {
		if(playerNames.get(Player.PLAYER_ONE).equals(playerName)){
			return Player.PLAYER_ONE;
		}
		else if(playerNames.get(Player.PLAYER_TWO).equals(playerName)){
			return Player.PLAYER_TWO;
		}
		else{
			return null;
		}
	}
}
