package game;

import game.event.CompositeEvent;
import game.event.EventXML;
import game.event.HealthEvent;

import java.util.ArrayList;
import java.util.HashMap;
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
		turns = new ArrayList<>();
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
	
	public int getLastHealth(Player owner, String pokemon, int turnIndex, int eventIndex){
		TurnXML turn = getTurns().get(turnIndex);
		int health = getLastHealth(turn, owner, pokemon, eventIndex - 1);
		if(health != -1){
			return health;
		}
		
		for(int i = turnIndex - 1; i >= 0; i--){
			turn = getTurns().get(i);
			health = getLastHealth(turn, owner, pokemon, turn.getEvents().size() - 1);
			if(health != -1){
				return health;
			}
		}
		
		return 100;		//no information on health so Pokemon must be completely healthy, should never happen though
	}
	
	private int getLastHealth(TurnXML turn, Player owner, String pokemon, int eventIndex){
		List<EventXML> turnEvents = turn.getEvents();
		for(int j = eventIndex; j >= 0; j--){
			EventXML event = turnEvents.get(j);
			if(event.isHealthEvent() && ((HealthEvent) event).getPokemon().equals(pokemon) && ((HealthEvent) event).getOwner() == owner){
				return ((HealthEvent)event).getHealth();
			}
			else if(event.isCompositeEvent()){
				List<EventXML> effects = ((CompositeEvent) event).getEffects();
				for(int i = effects.size() - 1; i >= 0; i--){
					EventXML effect = effects.get(i);
					if(effect.isHealthEvent() && ((HealthEvent) effect).getPokemon().equals(pokemon) && ((HealthEvent) effect).getOwner() == owner){
						return ((HealthEvent)effect).getHealth();
					}
				}
			}
		}
		return -1;
	}
}
