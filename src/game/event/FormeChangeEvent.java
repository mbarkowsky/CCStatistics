package game.event;

import game.GameXML.Player;

public class FormeChangeEvent extends EventXML {

	private String oldForm;
	private String newForm;
	private Player owner;
	
	public String getOldForm() {
		return oldForm;
	}

	public void setOldForm(String oldForm) {
		this.oldForm = oldForm;
	}

	public String getNewForm() {
		return newForm;
	}

	public void setNewForm(String newForm) {
		this.newForm = newForm;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@Override
	public String toString(){
		return owner.toString() + "'s " + oldForm + " changed into " + newForm;
	}
}
