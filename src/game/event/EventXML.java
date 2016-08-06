package game.event;

public class EventXML {

	protected String eventString;
	protected EventType type;
	
	public EventXML(){
		type = EventType.getInstance();
	}
	
	public String getEventString() {
		return eventString;
	}

	public void setEventString(String eventString) {
		this.eventString = eventString;
	}
	
	public EventType getType() {
		return type;
	}

	public String toString(){
		return getEventString();
	}
	
}
