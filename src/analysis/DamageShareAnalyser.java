package analysis;

import game.GameXML;
import game.TurnXML;
import game.GameXML.Player;
import game.event.CompositeEvent;
import game.event.DamageEventXML;
import game.event.EventXML;
import game.event.HealthEvent;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.TableLayout;

public class DamageShareAnalyser extends AnalyserXML {

	private class Result implements Comparable<Result>{

		private int totalDamage;
		private String key;
		
		private Result(String key){
			this.key = key;
			this.totalDamage = 0;
		}

		@Override
		public int compareTo(Result o) {
			return Integer.signum(o.totalDamage - this.totalDamage);
		}
		
	}
	
	@Override
	public String getName() {
		return "damage share";
	}

	@Override
	protected JComponent doAnalyse(Collection<GameXML> games, String playerName) {
		Map<String, Result> results =  createResults(games, playerName);
		List<Result> ranking = new ArrayList<>(results.values());
		Collections.sort(ranking);
		
		int[] columnTypes = {TableLayout.MINIMUM_COLUMN_WIDTH, TableLayout.SCALING_COLUMN_WIDTH, TableLayout.SCALING_COLUMN_WIDTH};
		JPanel result = new JPanel(new TableLayout(3, columnTypes));
		for(int i = 0; i < ranking.size(); i++){
			Result r = ranking.get(i);
			
			JLabel rankingLabel = new JLabel((i+1) + "  ");
			result.add(rankingLabel);
			
			JLabel nameLabel = new JLabel(r.key);
			Font nameFont = rankingLabel.getFont().deriveFont(Font.PLAIN);
			nameLabel.setFont(nameFont);
			result.add(nameLabel);
			
			JLabel resultLabel = new JLabel(r.totalDamage + "%");
			result.add(resultLabel);
		}
		return result;
	}

	private Map<String, Result> createResults(Collection<GameXML> games, String playerName) {
		Map<String, Result> results = new HashMap<>();
		results.put("direct damage", new Result("direct damage"));
		results.put("status", new Result("status"));
		results.put("partial trapping", new Result("partial trapping"));
		results.put("recoil", new Result("recoil"));
		results.put("weather", new Result("weather"));
		results.put("item", new Result("item"));
		results.put("ability", new Result("ability"));
		results.put("entry hazards", new Result("entry hazards"));
		results.put("misc", new Result("misc"));
		for(GameXML game:games){
			List<Player> players = new LinkedList<>();
			if(playerName.equals("")){
				players.add(Player.PLAYER_ONE);
				players.add(Player.PLAYER_TWO);
			}
			else{
				Player player = game.getPlayer(playerName);
				if(player == null){
					continue;
				}
				players.add(player);	
			}
			
			List<TurnXML> turns = game.getTurns();
			for(int turnIndex = 0; turnIndex < turns.size(); turnIndex++){
				TurnXML turn = turns.get(turnIndex);
				List<EventXML> events = turn.getEvents();
				for(int eventIndex = 0; eventIndex < turn.getEvents().size(); eventIndex++){
					EventXML event = events.get(eventIndex);
					if(event.isDamageEvent() && players.contains(GameXML.getOpponent(((DamageEventXML)event).getOwner()))){
						String key = getKey((DamageEventXML)event, false);
						int damage = getDamage((DamageEventXML) event, game, turnIndex, eventIndex);
						Result result = results.get(key);
						result.totalDamage = result.totalDamage + damage;
					}
					else if(event.isCompositeEvent()){
						for(EventXML effect:((CompositeEvent)event).getEffects()){
							if(effect.isDamageEvent() && players.contains(GameXML.getOpponent(((DamageEventXML)effect).getOwner()))){
								String key = getKey((DamageEventXML)effect, event.isMoveEvent());
								int damage = getDamage((DamageEventXML) effect, game, turnIndex, eventIndex);
								Result result = results.get(key);
								result.totalDamage = result.totalDamage + damage;
							}
						}
					}
				}
			}
		}
		return results;
	}

	private int getDamage(HealthEvent lastEnemyHealthEvent, GameXML game, int turnIndex, int eventIndex){
		return game.getLastHealth(lastEnemyHealthEvent.getOwner(), lastEnemyHealthEvent.getPokemon(), turnIndex, eventIndex) - lastEnemyHealthEvent.getHealth();
	}
	
	private String getKey(DamageEventXML event, boolean parentIsMove) {
		if(parentIsMove && event.getExtras().isEmpty()){
			return "direct damage";
		}
		else{
			for(String extra:event.getExtras()){
				if(extra.startsWith("[from]")){
					String source = extra.substring(7);
					if(source.equals("recoil"))	return "recoil";
					else if(isWeather(source)) return "weather";
					else if(isStatus(source)) return "status";
					else if(isEntryHazards(source)) return "entry hazards";
					else if(source.startsWith("item:")) return "item";
					else if(source.startsWith("ability:")) return "ability";
				}
				else if(extra.equals("[partiallytrapped]")) return "partial trapping";
			}
		}
		return "misc";
	}

	private boolean isEntryHazards(String source) {
		switch(source.toLowerCase()){
		case "stealth rock":
			return true;
		case "spikes":
			return true;
		default:
			return false;
		}
	}

	private boolean isStatus(String source) {
		switch(source.toLowerCase()){
		case "psn":
			return true;
		case "brn":
			return true;
		case "confusion":
			return true;
		case "leech seed":
			return true;
		case "curse":
			return true;
		default:
			return false;
		}
	}

	private boolean isWeather(String source) {
		switch(source.toLowerCase()){
		case "sandstorm":
			return true;
		case "hail":
			return true;
		default:
			return false;
		}
	}

}
