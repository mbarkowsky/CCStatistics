package util;

import java.awt.Color;

public class CCStatisticsUtil {
	
	public static final int HIGH_IS_GOOD = 1;
	public static final int HIGH_IS_BAD = 2;
	
	public static Color goodColor = Color.GREEN.darker();
	public static Color neutralColor = Color.ORANGE;
	public static Color badColor = Color.RED.darker();
	
	public static String wordsToUpperCase(String string){
		char[] result = new char[string.length()];
		for(int i = 0; i < string.length(); i++){
			char c = string.charAt(i);
			if(i == 0 || string.charAt(i-1) == ' '){
				result[i] = Character.toUpperCase(c);
			}
			else{
				result[i] = c;
			}
		}
		return String.valueOf(result);
	}

	public static Color getPercentageColor(int winRate, int mode) {
		Color c;
		if(mode == HIGH_IS_GOOD){
			if(winRate > 50){
				c = CCStatisticsUtil.goodColor;
			}
			else if(winRate == 50){
				c = CCStatisticsUtil.neutralColor;
			}
			else{
				c = CCStatisticsUtil.badColor;
			}
		}
		else{
			if(winRate < 50){
				c = CCStatisticsUtil.goodColor;
			}
			else if(winRate == 50){
				c = CCStatisticsUtil.neutralColor;
			}
			else{
				c = CCStatisticsUtil.badColor;
			}
		}
		return c;
	}
	
}
