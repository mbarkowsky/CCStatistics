package util;

import java.util.HashSet;
import java.util.Set;


public class GameUtil {

	public static enum Stat {HP, ATK, DEF, SPA, SPD, SPE, ACC, EVA}
	public static enum Status {FREEZE, PARALYSIS, BURN, POISON, TOXIN, SLEEP, FAINT, FLINCH, ATTRACT, RECHARGE, DISABLE, FOCUS_PUNCH, TAUNT};
	
	public enum Effectiveness {NEUTRAL, SUPER_EFFECTIVE, NOT_VERY_EFFECTIVE, IMMUNE}

	private static Set<String> partialTrappingMoves;
	
	public static Status getStatusForString(String statusString){
		statusString = statusString.toLowerCase();
		switch(statusString){
		case "frz":
			return Status.FREEZE;
		case "par":
			return Status.PARALYSIS;
		case "psn":
			return Status.POISON;
		case "tox":
			return Status.TOXIN;
		case "brn":
			return Status.BURN;
		case "slp":
			return Status.SLEEP;
		case "flinch":
			return Status.FLINCH;
		case "attract":
			return Status.ATTRACT;
		case "disable":
			return Status.DISABLE;
		case "recharge":
			return Status.RECHARGE;
		case "focus punch":
			return Status.FOCUS_PUNCH;
		case "fnt":
			return Status.FAINT;
		case "move: taunt":
			return Status.TAUNT;
		default:
			System.out.println("unrecognized status condition: " + statusString);
			return null;
		}
	}
	
	public static boolean isMajorStatus(Status status) {
		switch(status){
			case FREEZE:
				return true;
			case PARALYSIS:
				return true;
			case POISON:
				return true;
			case TOXIN:
				return true;
			case SLEEP:
				return true;
			case BURN:
				return true;
			default:
				return false;
		}
	}
	

	public static boolean isMinorStatus(Status status) {
		switch(status){
		case DISABLE:
			return true;
		case TAUNT:
			return true;
		default:
			return false;
	}
	}
	
	public static Stat getStatForString(String statString) {
		statString = statString.toLowerCase();
		switch(statString){
		case "hp":
			return Stat.HP;
		case "atk":
			return Stat.ATK;
		case "def":
			return Stat.DEF;
		case "spa":
			return Stat.SPA;
		case "spd":
			return Stat.SPD;
		case "spe":
			return Stat.SPE;
		case "accuracy":
			return Stat.ACC;
		case "evasion":
			return Stat.EVA;
		default:
			System.out.println("unrecognized stat: " + statString);
			return null;
		}
	}

	public static Effectiveness getEffectivenessForString(String string) {
		string = string.toLowerCase();
		switch(string){
		case "supereffective":
			return Effectiveness.SUPER_EFFECTIVE;
		case "resisted":
			return Effectiveness.NOT_VERY_EFFECTIVE;
		case "immune":
			return Effectiveness.IMMUNE;
		default:
			return null;
		}
	}

	public static Set<String> partialTrappingMoves() {
		if(partialTrappingMoves == null){
			partialTrappingMoves = new HashSet<>();
			partialTrappingMoves.add("Bind");
			partialTrappingMoves.add("Clamp");
			partialTrappingMoves.add("Fire Spin");
			partialTrappingMoves.add("Infestation");
			partialTrappingMoves.add("Magma Storm");
			partialTrappingMoves.add("Sand Tomb");
			partialTrappingMoves.add("Whirlpool");
			partialTrappingMoves.add("Wrap");
		}
		return partialTrappingMoves;
	}
	
}
