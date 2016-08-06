package game.event;

public class FieldEndEventXML extends EventXML {

	private String effect;

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}
	
	public String toString(){
		return effect + " ended";
	}
	
}
