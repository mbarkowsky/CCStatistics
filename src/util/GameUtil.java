package util;

public class GameUtil {

	public static enum Stat {HP, ATK, DEF, SPA, SPD, SPE, ACC, EVA}

	public static Stat getStatForString(String statString) {
		statString = statString.toLowerCase();
		switch(statString){
		case "hit points":
			return Stat.HP;
		case "attack":
			return Stat.ATK;
		case "defense":
			return Stat.DEF;
		case "special attack":
			return Stat.SPA;
		case "special defense":
			return Stat.SPD;
		case "speed":
			return Stat.SPE;
		case "accuracy":
			return Stat.ACC;
		case "evasiveness":
			return Stat.EVA;
		default:
			return null;
		}
	};
	
}
