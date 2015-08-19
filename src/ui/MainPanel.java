package ui;

import game.Game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import builder.GameBuilder;
import anaylsis.Analyser;
import anaylsis.SwitchAnalyser;
import anaylsis.VictoryAnalyser;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -8958557846840490170L;
	private static final String gameDirectoryPath = "games"; 
	
	private List<Game> games;
	private Map<JCheckBox, Analyser> analysers;
	
	private List<JCheckBox> checkBoxes;
	private JPanel selectionPanel;
	private JTabbedPane resultPanel;
	
	public MainPanel(){
		initializeAnalysers();
		initializeUI();
	}
	
	private void loadGames(){
		long t1 = System.currentTimeMillis();
		games = new LinkedList<Game>();
		GameBuilder builder = new GameBuilder();
		
		File gameDirectory = new File(gameDirectoryPath);
		File[] gameFiles = gameDirectory.listFiles();
		
		for(File gameFile:gameFiles){
			Game game;
			try {
				game = builder.buildGame(gameFile);
				games.add(game);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Loading took " + (t2 - t1) + " milliseconds");
	}
	
	private void initializeAnalysers() {
		analysers = new HashMap<>();
		checkBoxes = new LinkedList<JCheckBox>();
		List<Analyser> possibleAnalysers = getPossibleAnalysers();
		for(Analyser analyser:possibleAnalysers){
			JCheckBox checkBox = new JCheckBox();
			checkBox.setText(analyser.getName());
			checkBoxes.add(checkBox);
			analysers.put(checkBox, analyser);
		}
	}

	private void initializeUI(){
		setLayout(new GridLayout(0, 2));
		
		selectionPanel = new JPanel();
		selectionPanel.setLayout(new BorderLayout());
		
		JPanel selection = new JPanel();
		selection.setLayout(new BoxLayout(selection, BoxLayout.Y_AXIS));
		for(JCheckBox checkBox:checkBoxes){
			selection.add(checkBox);
		}
		selectionPanel.add(selection, BorderLayout.CENTER);
		
		
		JButton analyseButton = new JButton("Analyse");
		analyseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent evt) {
				executeAnalysis();
			}
			
		});
		selectionPanel.add(analyseButton, BorderLayout.PAGE_END);
		
		add(selectionPanel);
	}

	private void executeAnalysis() {
		loadGames();
		
		JTabbedPane newResultPanel = new JTabbedPane();
		for(JCheckBox checkBox:checkBoxes){
			if(checkBox.isSelected()){
				Analyser analyser = analysers.get(checkBox);
				newResultPanel.addTab(analyser.getName(), analyser.analyse(games));
			}
		}
		
		if(resultPanel != null){
			remove(resultPanel);	
		}
		add(newResultPanel);
		doLayout();
		resultPanel = newResultPanel;
	}
	
	private List<Analyser> getPossibleAnalysers() {
		List<Analyser> possibleAnalysers = new LinkedList<>();
		possibleAnalysers.add(new VictoryAnalyser());
		possibleAnalysers.add(new SwitchAnalyser());
		return possibleAnalysers;
	}
	
}
