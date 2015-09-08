package builder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import builder.BlockEffect.BlockType;
import util.GameUtil;
import util.GameUtil.Stat;
import game.Game.Player;
import game.playeraction.AttackAction;
import game.playeraction.AttackEffect;
import game.playeraction.BoostEffect;
import game.playeraction.DamageEffect;
import game.playeraction.DropEffect;
import game.playeraction.HealingEffect;
import game.playeraction.ItemEffect;
import game.playeraction.RecoilEffect;
import game.playeraction.AttackEffect.EffectType;
import game.playeraction.DamageEffect.Effectiveness;

public class GenericAttackBuilder implements AttackBuilder {

	private Map<String, EffectType> attackKeyWords;
	
	private String[] log;
	protected Integer lastIndex;
	
	public GenericAttackBuilder(){
		initializeKeyWords();
	}
	
	private void initializeKeyWords() {
		attackKeyWords = new HashMap<>();
		attackKeyWords.put("lost", EffectType.DAMAGE);
		attackKeyWords.put("affect", EffectType.DAMAGE);
		attackKeyWords.put("regained", EffectType.HEALING);
		attackKeyWords.put("healthy!", EffectType.HEALING);
		attackKeyWords.put("rose", EffectType.BOOST);
		attackKeyWords.put("rose!", EffectType.BOOST);
		attackKeyWords.put("fell!", EffectType.DROP);
		attackKeyWords.put("strengthened", EffectType.ITEM);
		for(String berryName:getDefenseBerryNames()){
			attackKeyWords.put(berryName, EffectType.ITEM);
		}
		attackKeyWords.put("recoil!", EffectType.RECOIL);
		attackKeyWords.put("substitute", EffectType.BLOCK);
		attackKeyWords.put("substitute!", EffectType.BLOCK);
		attackKeyWords.put("protected", EffectType.BLOCK);
	}
	
	private List<String> getDefenseBerryNames(){
		List<String> berryNames = new LinkedList<>();
		berryNames.add("Occa");
		berryNames.add("Passho");
		berryNames.add("Wacan");
		berryNames.add("Rindo");
		berryNames.add("Yache");
		berryNames.add("Chople");
		berryNames.add("Kebia");
		berryNames.add("Shuca");
		berryNames.add("Coba");
		berryNames.add("Payapa");
		berryNames.add("Tanga");
		berryNames.add("Charti");
		berryNames.add("Kasib");
		berryNames.add("Haban");
		berryNames.add("Colbur");
		berryNames.add("Babiri");
		berryNames.add("Chilan");
		berryNames.add("Roseli");
		return berryNames;
	}
	
	private Player identifyPlayer(String line) {
		if(line.contains("opposing")){
			return Player.PLAYER_TWO;
		}
		else{
			return Player.PLAYER_ONE;	
		}
	}
	
	@Override
	public List<AttackEffect> buildEffects(Player player, AttackAction action, String[] log, int lastIndex) {
		this.log = log;
		this.lastIndex = lastIndex;
		boolean addedEffect;
		List<AttackEffect> effects = new LinkedList<>();
		String line;
		do{
			addedEffect = false;
			line = log[this.lastIndex];
			StringTokenizer st = new StringTokenizer(line);	
			while(st.hasMoreTokens()){
				String token = st.nextToken();
				EffectType effectType = attackKeyWords.get(token);
				if(effectType != null){
					AttackEffect effect = buildAttackEffect(effectType, line, player);
					if(effect != null){
						effects.add(effect);
						addedEffect = true;
						this.lastIndex++;
						break;	
					}
				}
			}
		}
		while(addedEffect);
		return effects;
	}

	private AttackEffect buildAttackEffect(EffectType effectType, String line, Player player) {
		AttackEffect effect = null;
		
		switch(effectType){
		case DAMAGE:
			if(!line.contains("focus") && (line.startsWith("It doesn't affect") || player != identifyPlayer(line))){
				effect = buildDamageEffect(line);	
			}
			break;
		case HEALING:
			effect = buildHealingEffect(line);
			break;
		case BOOST:
			effect = buildBoostEffect(line);
			break;
		case DROP:
			effect = buildDropEffect(line);
			break;
		case ITEM:
			effect = buildItemEffect(line);	
			break;
		case RECOIL:
			effect = buildRecoilEffect(line);
		case STATUS:
			break;
		case BLOCK:
			if(player != identifyPlayer(line)){
				effect = buildBlockedEffect(line);
			}
			else{
				effect = buildBlockEffect(line);	
			}
			break;
		default:
			break;
		}
		return effect;
	}

	private DamageEffect buildDamageEffect(String line) {
		DamageEffect effect = new DamageEffect();		//TODO make more performant?
		if(line.contains("It doesn't affect")){
			String defender = line.substring(line.lastIndexOf(" "), line.length() - 3);
			effect.setDefender(defender);
			effect.setEffectiveness(Effectiveness.NO_EFFECT);
			return effect;
		}
		
		effect.setCrit(line.contains("A critical hit!"));
		if(line.contains("It's not very effective...")){
			effect.setEffectiveness(Effectiveness.NOT_VERY_EFFECTIVE);
		}
		else if(line.contains("It's super effective!")){
			effect.setEffectiveness(Effectiveness.SUPER_EFFECTIVE);
		}
		else{
			effect.setEffectiveness(Effectiveness.NEUTRAL);
		}
		
		String[] strings = line.split(" lost ");
		StringTokenizer left = new StringTokenizer(strings[0]);
		String defender = "";
		while(left.hasMoreTokens()){
			defender = left.nextToken();
		}
		effect.setDefender(defender);
		
		StringTokenizer right = new StringTokenizer(strings[1]);
		String damage = right.nextToken();
		damage = damage.substring(0, damage.length() - 1);
		effect.setDamage(Double.parseDouble(damage));	

		if(log[lastIndex + 1].endsWith(defender + " fainted!")){
			effect.setKO(true);
			lastIndex++;
		}
		return effect;
	}
	
	private AttackEffect buildRecoilEffect(String line) {
		return new RecoilEffect();
	}
	
	private HealingEffect buildHealingEffect(String line){
		return new HealingEffect();
	}

	private AttackEffect buildBoostEffect(String line) {
		StringTokenizer st = new StringTokenizer(line);
		String token;
		String token1 = "";
		String token2 = "";
		String statString;
		while(!((token = st.nextToken()).equals("sharply") || token.equals("rose") || token.equals("rose!"))){	//TODO drastically
			token1 = token2;
			token2 = token;
		}
		
		if(token1.equals("Special")){
			statString = token1 + " " + token2;
		}
		else{
			statString = token2;
		}
		Stat stat = GameUtil.getStatForString(statString);
		
		BoostEffect effect = new BoostEffect();
		effect.setStat(stat);
		return effect;
	}
	

	private AttackEffect buildDropEffect(String line) {
		StringTokenizer st = new StringTokenizer(line);
		String token;
		String token1 = "";
		String token2 = "";
		String statString;
		while(!((token = st.nextToken()).equals("fell") || token.equals("harshly") || token.equals("fell!"))){
			token1 = token2;
			token2 = token;
		}
		
		if(token1.equals("Special")){
			statString = token1 + " " + token2;
		}
		else{
			statString = token2;
		}
		Stat stat = GameUtil.getStatForString(statString);
		
		DropEffect effect = new DropEffect();	//TODO maybe make one class for drop and boost
		effect.setStat(stat);
		return effect;
	}
	
	private ItemEffect buildItemEffect(String line){	//TODO specialize to gems
		String item;
		StringTokenizer st = new StringTokenizer(line);
		if(line.contains("strengthened")){
			st.nextToken();
			item = st.nextToken() + " " + st.nextToken();
		}
		else{
			String name = "";
			String berry = st.nextToken();
			while(st.hasMoreTokens()){
				name = berry;
				berry = st.nextToken();
			}
			berry = berry.substring(0, berry.length() - 1);
			item = name + " " + berry;
		}
		return new ItemEffect(item);
	}
	
	private AttackEffect buildBlockEffect(String line) {
		return new BlockEffect(BlockType.BLOCK);
	}
	
	private AttackEffect buildBlockedEffect(String line) {
		return new BlockEffect(BlockType.BLOCKED);
	}
	
}
