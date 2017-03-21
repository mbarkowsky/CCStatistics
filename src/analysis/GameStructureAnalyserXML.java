package analysis;

import game.GameXML;
import game.GameXML.Player;
import game.TurnXML;
import game.event.CompositeEvent;
import game.event.EventXML;
import game.event.FaintEventXML;
import game.event.MoveEventXML;
import game.event.SwitchEventXML;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.TableLayout;

public class GameStructureAnalyserXML extends AnalyserXML {

	private class Result{
		
		private int gameNumber;
		private int maxTurns;
		private int minTurns;
		private int totalTurns;	
		private int moves;
		private int switches;
		
		
		private Result(int gameNumber, int maxTurns, int minTurns, int totalTurns, int moves, int switches){
			this.gameNumber = gameNumber;
			this.maxTurns = maxTurns;
			this.minTurns = minTurns;
			this.totalTurns = totalTurns;
			this.moves = moves;
			this.switches = switches;
		}
		
	}
	
	@Override
	public String getName() {
		return "game structure";
	}

	@Override
	protected JComponent doAnalyse(Collection<GameXML> games, String playerName) {
		Result r = calculateResult(games, playerName);
		
		JPanel result = new JPanel(new TableLayout(2));

		result.add(new JLabel("games total"));
		result.add(new JLabel(Integer.toString(r.gameNumber)));

		result.add(new JLabel("minimum turns"));
		result.add(new JLabel(Integer.toString(r.minTurns)));
		
		result.add(new JLabel("average turns"));
		result.add(new JLabel(Integer.toString(r.gameNumber > 0 ? r.totalTurns / r.gameNumber : 0)));
		
		result.add(new JLabel("maximum turns"));
		result.add(new JLabel(Integer.toString(r.maxTurns)));
		
		result.add(new JLabel("moves"));
		result.add(new JLabel(Integer.toString(r.moves)));		

		result.add(new JLabel("switches"));
		result.add(new JLabel(Integer.toString(r.switches)));
		
		return result;
	}

	private Result calculateResult(Collection<GameXML> games, String playerName) {
		int totalTurns = 0;
		int gameNumber = 0;
		int max = 0;
		int min = 0;
		int moves = 0;
		int switches = 0;
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
			int turnNumber = turns.size();
			if(turnNumber > max){
				max = turnNumber;
			}
			if(turnNumber < min || min == 0){
				min = turnNumber;
			}
			totalTurns += turnNumber;
			gameNumber++;
			
			for(int turnIndex = 1; turnIndex < turnNumber; turnIndex ++){
				TurnXML turn = turns.get(turnIndex);
				List<EventXML> events = turn.getEvents();
				for(int eventIndex = 0; eventIndex < events.size(); eventIndex++){
					EventXML event = events.get(eventIndex);
					if(event.isMoveEvent() && players.contains(((MoveEventXML)event).getOwner())){
						moves++;
					}
					else if(event.isSwitchEvent() && players.contains(((SwitchEventXML)event).getOwner()) && !followsFaint(game, (SwitchEventXML)event, eventIndex, turnIndex)){
						switches++;
					}
				}
			}
		}
		return new Result(gameNumber, max, min, totalTurns, moves, switches);
	}

	private boolean followsFaint(GameXML game, SwitchEventXML event, int eventIndex, int turnIndex) {
		Player owner = event.getOwner();
		if(eventIndex == 0){
			return false;
		}
		EventXML previousEvent = game.getTurns().get(turnIndex).getEvents().get(eventIndex - 1);
		if(previousEvent.isFaintEvent() && ((FaintEventXML)previousEvent).getOwner() == owner){
			return true;
		}
		else if(previousEvent.isCompositeEvent()){
			for(EventXML effect:((CompositeEvent)previousEvent).getEffects()){
				if(effect.isFaintEvent() && ((FaintEventXML)effect).getOwner() == owner){
					return true;
				}
			}
		}
		return false;
	}

}
