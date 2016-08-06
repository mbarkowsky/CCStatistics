package game.event;

import game.GameXML.Player;

public class SideEndEventXML extends EventXML {
	
	private Player side;
	private String effect;
	
	public Player getSide() {
		return side;
	}
	
	public void setSide(Player side) {
		this.side = side;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}
	
	public String toString(){
		return effect + " ended for " + side;
	}
	
}
