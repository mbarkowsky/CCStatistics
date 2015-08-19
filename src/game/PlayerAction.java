package game;

public abstract class PlayerAction {

	public enum ActionType {ATTACK, SWITCH}

	public abstract ActionType getType();

}
