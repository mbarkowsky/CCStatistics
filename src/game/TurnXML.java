package game;

import game.event.EventXML;

import java.util.ArrayList;
import java.util.List;

public class TurnXML {
	
	private List<EventXML> events;

	public TurnXML(){
		events = new ArrayList<>();
	}

	public List<EventXML> getEvents() {
		return events;
	}

	public void addEvent(EventXML event){
		events.add(event);
	}
	
	public String prettyString(){
		StringBuilder sb = new StringBuilder();
		for(EventXML event:events){
			sb.append(event.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
