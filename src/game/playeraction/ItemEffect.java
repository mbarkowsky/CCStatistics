package game.playeraction;


public class ItemEffect implements AttackEffect {

	private String item;
	
	public ItemEffect(String item) {
		this.setItem(item);
	}

	@Override
	public EffectType getType() {
		return EffectType.ITEM;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
	public String toString(){
		return getType() + ": " + item;
	}

}
