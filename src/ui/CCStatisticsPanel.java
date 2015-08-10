package ui;

import game.Game;

import java.awt.GridLayout;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import prototype.PrototypeGameBuilder;
import builder.GameBuilder;
import anaylsis.Analyser;
import anaylsis.VictoryAnalyser;

public class CCStatisticsPanel extends JPanel {

	private static final long serialVersionUID = 8337704405470611847L;
	private static final String gameDirectoryPath = "games"; 
	
	private List<Game> games;
	private List<Analyser> analysers;
	
	public CCStatisticsPanel(){
		loadGames();
		
		initializeUI();
	}

	private void initializeUI() {
		// TODO initialize all required UI
		setLayout(new GridLayout(0, 2));
		
		analysers = new LinkedList<Analyser>();
		analysers.add(new VictoryAnalyser());	//TODO remove and create way to add analysers
		analysers.add(new VictoryAnalyser());	//TODO remove and create way to add analysers
		analysers.add(new VictoryAnalyser());	//TODO remove and create way to add analysers
		
		JTabbedPane tabbedPane = new JTabbedPane();
		for(Analyser analyser:analysers){
			JPanel result = analyser.analyse(games);
			tabbedPane.addTab(analyser.getName(), result);
		}
		add(tabbedPane);
	}

	private void loadGames(){
		games = new LinkedList<Game>();
		GameBuilder builder = new PrototypeGameBuilder();	//TODO replace with correct builder
		
		File gameDirectory = new File(gameDirectoryPath);
		File[] gameFiles = gameDirectory.listFiles();
		
		for(File gameFile:gameFiles){
			Game game = builder.buildGame(gameFile);
			games.add(game);
		}
	}
}
