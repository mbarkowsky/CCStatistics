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
	
}
