package game.event;

public class EventXML {

	protected String eventString;
	
	public String getEventString() {
		return eventString;
	}

	public void setEventString(String eventString) {
		this.eventString = eventString;
	}

	public boolean isHealthEvent(){
		return false;
	}

	public boolean isMoveEvent() {
		return false;
	}
	
	public boolean isCompositeEvent() {
		return false;
	}
	
	public String toString(){
		return getEventString();
	}

	public boolean isStatusEvent() {
		return false;
	}

	public boolean isStartEvent() {
		return false;
	}

	public boolean isPrepareEvent() {
		return false;
	}

	public boolean isDamageEvent() {
		return false;
	}

	public boolean isEndEvent() {
		return false;
	}

	public boolean isActivateEvent() {
		return false;
	}

	public boolean isSwitchEvent() {
		return false;
	}

	public boolean isFaintEvent() {
		return false;
	}
	
}
