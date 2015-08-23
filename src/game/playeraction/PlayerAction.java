package game.playeraction;

public interface PlayerAction {

	public enum ActionType {ATTACK, SWITCH}

	public abstract ActionType getType();

}
