package game;

import game.Game.Player;

public class Turn {

	private PlayerAction playerOneAction;
	private PlayerAction playerTwoAction;
	
	public PlayerAction getPlayerAction(Player player){
		if(player == Player.PLAYER_ONE){
			return playerOneAction;
		}
		else{
			return playerTwoAction;
		}
	}
	
	public PlayerAction getPlayerOneAction() {
		return playerOneAction;
	}
	
	public void setPlayerOneAction(PlayerAction playerOneAction) {
		this.playerOneAction = playerOneAction;
	}

	public PlayerAction getPlayerTwoAction() {
		return playerTwoAction;
	}

	public void setPlayerTwoAction(PlayerAction playerTwoAction) {
		this.playerTwoAction = playerTwoAction;
	}

	public void addPlayerAction(Player player, PlayerAction action) {
		if(player == Player.PLAYER_ONE){
			setPlayerOneAction(action);
		}
		else{
			setPlayerTwoAction(action);
		}
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		if(playerOneAction != null){
			buffer.append("Player One Action: " + playerOneAction);
			if(playerTwoAction != null){
				buffer.append("\n");
			}
		}
		if(playerTwoAction != null){
			buffer.append("Player Two Action: " + playerTwoAction);	
		}
		return buffer.toString();
	}
	
}
