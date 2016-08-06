package game;

import game.event.EventXML;
import game.event.HealthEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameXML {

	public enum Player {PLAYER_ONE, PLAYER_TWO};
	
	public static Player getOpponent(Player player) {
		return player == Player.PLAYER_ONE ? Player.PLAYER_TWO : Player.PLAYER_ONE;
	}
	
	private String name;
	private Map<Player, String> playerNames;
	private Player winner;
	private List<TurnXML> turns;
	
	public GameXML(){
		playerNames = new HashMap<>();
		turns = new LinkedList<>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<TurnXML> getTurns() {
		return turns;
	}

	public void addTurn(TurnXML turn) {
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
			buffer.append("\n\n");
			for(int i = 0; i < turns.size(); i++){
				buffer.append("Turn " + i + "\n");
				buffer.append(turns.get(i).prettyString());
				buffer.append("\n");
			}
			buffer.append("Winner: " + getPlayerName(winner));
			return buffer.toString();
		}
	}
	
	public int getLastHealth(String pokemon, int turnIndex, int eventIndex){
		List<EventXML> turnEvents = getTurns().get(turnIndex).getEvents();
		for(int j = eventIndex; j >= 0; j--){
			EventXML event = turnEvents.get(j);
			if(event.getType().isHealthEvent() && ((HealthEvent) event).getPokemon().equals(pokemon)){
				return ((HealthEvent)event).getHealth();
			}
		}
		
		for(int i = turnIndex - 1; i >= 0; i--){
			turnEvents = getTurns().get(i).getEvents();
			for(int j = turnEvents.size() - 1; j >= 0; j--){
				EventXML event = turnEvents.get(j);
				if(event.getType().isHealthEvent() && ((HealthEvent) event).getPokemon().equals(pokemon)){
					return ((HealthEvent)event).getHealth();
				}
			}
		}
		
		return 100;		//no information on health so Pokemon must be completely healthy, should never happen though
	}
}
