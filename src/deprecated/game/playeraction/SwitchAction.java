package deprecated.game.playeraction;


public class SwitchAction implements PlayerAction {

	private String switchOut;
	private String switchIn;
	
	public SwitchAction(String switchOut, String switchIn) {
		this.setSwitchOut(switchOut);
		this.setSwitchIn(switchIn);
	}

	public String getSwitchOut() {
		return switchOut;
	}

	public void setSwitchOut(String switchOut) {
		this.switchOut = switchOut;
	}

	public String getSwitchIn() {
		return switchIn;
	}

	public void setSwitchIn(String switchIn) {
		this.switchIn = switchIn;
	}
	
	public String toString(){
		return "Switch: " + switchOut + " -> " + switchIn;
	}
	
	@Override
	public ActionType getType() {
		return ActionType.SWITCH;
	}
}
