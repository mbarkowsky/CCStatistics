package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import analysis.DamageAverageThresholdAnalyser;
import analysis.DamageTotalThresholdAnalyser;
import analysis.ThresholdAnalyser;
import analysis.WinRateThresholdAnalyser;
import builder.GameLoader;
import threshold.Threshold;
import ui.MainFrame;

public class Main {

	private final String gameDirectoryPath = "games";
	
	private GameLoader gameLoader;
	private List<ThresholdAnalyser> analysers;
	
	public Main(){
		gameLoader = new GameLoader(3);
		initialiseAnalysers();
	}
	
	private void initialiseAnalysers() {
		analysers = new LinkedList<>();
		
		analysers.add(new DamageAverageThresholdAnalyser());
		analysers.add(new DamageTotalThresholdAnalyser());
		analysers.add(new WinRateThresholdAnalyser());
	}

	private List<Threshold> createThresholds() {
		gameLoader.loadGames(gameDirectoryPath);
		List<Threshold> thresholds = new LinkedList<>();
		for(ThresholdAnalyser analyser:analysers){
			thresholds.add(analyser.createThreshold(gameLoader.getGames().values()));
		}
		return thresholds;
	}

	private void saveThresholds(Collection<Threshold> thresholds) {
		try{
			FileWriter fw = new FileWriter(MainFrame.thresholdPath + "thresholds.txt");
			for(Threshold t:thresholds){
				fw.write(t.getStringForSave());
				fw.write("\n");
			}
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Main main = new Main();
		Collection<Threshold> thresholds = main.createThresholds();
		main.saveThresholds(thresholds);
	}
	
}
