package game.event;

import java.util.List;

public interface CompositeEvent {

	public List<EventXML> getEffects();
	
	public void addEffect(EventXML effect);
	
}
