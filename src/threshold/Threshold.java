package threshold;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import ui.MainFrame;

public abstract class Threshold{
	
	private static Map<String, Threshold> thresholds;
	
	public static Threshold getThreshold(String name){
		if(thresholds == null){
			loadThresholds();
		}
		return thresholds.get(name);
	}
	
	private static void loadThresholds() {
		thresholds = new HashMap<>();
		try {
			FileReader fr = new FileReader(MainFrame.thresholdPath + "thresholds.txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null && line.length() > 0){
				Threshold t = buildThreshold(line);
				thresholds.put(t.getName(), t);
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Threshold buildThreshold(String string) {
		String[] strings = string.split("#");
		ThresholdType type = ThresholdType.valueOf(strings[0]);
		switch(type){
		case AVG_DEV:
			return buildAvgDevThreshold(strings[1]);
		default:
			return null;
		}
	}

	private static AvgDevThreshold buildAvgDevThreshold(String string) {
		StringTokenizer st = new StringTokenizer(string, ";");
		AvgDevThreshold threshold = new AvgDevThreshold();
		
		threshold.setName(st.nextToken());
		threshold.setAverage(Double.parseDouble(st.nextToken()));
		threshold.setDeviation(Double.parseDouble(st.nextToken()));
		return threshold;
	}

	public enum ThresholdType {AVG_DEV}
	
	public abstract ThresholdType getType();
	
	public abstract String getName();
	
	public abstract String getStringForSave();
	
}
