package deprecated.game.playeraction;


public class BlockEffect implements AttackEffect {

	public enum BlockType {BLOCK, BLOCKED}
	
	private BlockType blockType;
	
	public BlockEffect(BlockType blockType) {
		this.setBlockType(blockType);
	}

	@Override
	public EffectType getType() {
		return EffectType.BLOCK;
	}

	public BlockType getBlockType() {
		return blockType;
	}

	public void setBlockType(BlockType blockType) {
		this.blockType = blockType;
	}
	
	@Override
	public String toString(){
		return getType() + " (" + blockType.toString().toLowerCase() + ")";
	}

}
