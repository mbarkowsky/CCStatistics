package game.event;

import game.GameXML.Player;

public interface HealthEvent {

	public int getHealth();

	public String getPokemon();
	
	public Player getOwner();
}
