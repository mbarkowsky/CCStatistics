package game.event;

public class EventType {

	private static EventType instance;
	
	public static EventType getInstance(){
		if(instance == null){
			instance = new EventType();
		}
		return instance;
	}

	public boolean isHealthEvent(){
		return false;
	}
	
}
