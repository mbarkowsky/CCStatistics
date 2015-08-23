package util;

public class GameUtil {

	public static enum Stat {HP, ATK, DEF, SPA, SPD, SPE, ACC, EVA}

	public static Stat getStatForString(String statString) {
		switch(statString){
		case "Hit Points":
			return Stat.HP;
		case "Attack":
			return Stat.ATK;
		case "Defense":
			return Stat.DEF;
		case "Special Attack":
			return Stat.SPA;
		case "Special Defense":
			return Stat.SPD;
		case "Speed":
			return Stat.SPE;
		case "Accuracy":
			return Stat.ACC;
		case "Evasiveness":
			return Stat.EVA;
		default:
			return null;
		}
	};
	
}
