package game.event;

import deprecated.game.Game.Player;

public class SetHPEventXML extends EventXML {

	private String p1Pokemon;
	private String p2Pokemon;
	private int p1Health;
	private int p2Health;
	
	public String getP2Pokemon() {
		return p2Pokemon;
	}

	public void setP2Pokemon(String p2Pokemon) {
		this.p2Pokemon = p2Pokemon;
	}

	public int getP1Health() {
		return p1Health;
	}

	public void setP1Health(int p1Health) {
		this.p1Health = p1Health;
	}

	public int getP2Health() {
		return p2Health;
	}

	public void setP2Health(int p2Health) {
		this.p2Health = p2Health;
	}

	public String getP1Pokemon() {
		return p1Pokemon;
	}
	
	public void setP1Pokemon(String p1Pokemon) {
		this.p1Pokemon = p1Pokemon;
	}
	
	public String toString(){
		return Player.PLAYER_ONE + "'s " + p1Pokemon + "'s health was set to " + p1Health + "%, " + Player.PLAYER_TWO + "'s " + p2Pokemon + "'s health was set to " + p2Health + "%";
	}
	
}
