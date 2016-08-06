package util;

import java.awt.Color;

import threshold.AvgDevThreshold;
import threshold.Threshold;

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

	public static Color getThresholdColor(double value, Threshold threshold, int mode){
		if(threshold == null){
			return Color.BLACK;
		}
		else{
			switch(threshold.getType()){
				case AVG_DEV:
					return getAvgDevColor(value, (AvgDevThreshold)threshold, mode);
				default:
					return getPercentageColor(value, mode);
			}
		}
	}
	
	private static Color getAvgDevColor(double value, AvgDevThreshold threshold, int mode){
		Color c;
		if(mode == HIGH_IS_GOOD){
			if(value > threshold.getAverage() + threshold.getDeviation()){
				c = CCStatisticsUtil.goodColor;
			}
			else if(value < threshold.getAverage() - threshold.getDeviation()){
				c = CCStatisticsUtil.badColor;
			}
			else{
				c = CCStatisticsUtil.neutralColor;
			}
		}
		
		else{
			if(value < threshold.getAverage() - threshold.getDeviation()){
				c = CCStatisticsUtil.goodColor;
			}
			else if(value > threshold.getAverage() + threshold.getDeviation()){
				c = CCStatisticsUtil.badColor;
			}
			else{
				c = CCStatisticsUtil.neutralColor;
			}
		}
		return c;
	}
	
	public static Color getPercentageColor(double percentage, int mode) {
		Color c;
		if(mode == HIGH_IS_GOOD){
			if(percentage > 50){
				c = CCStatisticsUtil.goodColor;
			}
			else if(percentage == 50){
				c = CCStatisticsUtil.neutralColor;
			}
			else{
				c = CCStatisticsUtil.badColor;
			}
		}
		else{
			if(percentage < 50){
				c = CCStatisticsUtil.goodColor;
			}
			else if(percentage == 50){
				c = CCStatisticsUtil.neutralColor;
			}
			else{
				c = CCStatisticsUtil.badColor;
			}
		}
		return c;
	}
	
}
