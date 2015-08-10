package ui;

import game.Game;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import prototype.PrototypeGameBuilder;
import builder.GameBuilder;
import anaylsis.Analyser;
import anaylsis.VictoryAnalyser;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -8958557846840490170L;
	private static final String gameDirectoryPath = "games"; 
	
	private List<Game> games;
	private Map<JCheckBox, Analyser> analysers;
	
	private JPanel selectionPanel;
	private JTabbedPane resultPanel;
	
	public MainPanel(){
		initializeAnalysers();
		initializeUI();
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
	
	private void initializeAnalysers() {
		analysers = new HashMap<>();
		List<Analyser> possibleAnalysers = getPossibleAnalysers();
		for(Analyser analyser:possibleAnalysers){
			JCheckBox checkBox = new JCheckBox();
			analysers.put(checkBox, analyser);
		}
	}

	private void initializeUI(){
		setLayout(new GridLayout(0, 2));
		
		selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
		
		for(Entry<JCheckBox, Analyser> entry:analysers.entrySet()){
			JPanel selection = new JPanel();
			selection.setLayout(new FlowLayout());
			selection.setSize(new Dimension(0, 10));
			selection.add(entry.getKey());
			selection.add(new JLabel(entry.getValue().getName()));
			selectionPanel.add(selection);
		}
		
		JButton analyseButton = new JButton("Analyse");
		analyseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent evt) {
				executeAnalysis();
			}
			
		});
		selectionPanel.add(analyseButton);
		
		add(selectionPanel);
	}

	private void executeAnalysis() {
		loadGames();
		
		JTabbedPane newResultPanel = new JTabbedPane();
		for(Entry<JCheckBox, Analyser> entry:analysers.entrySet()){
			if(entry.getKey().isSelected()){
				Analyser analyser = entry.getValue();
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
	
	private List<Analyser> getPossibleAnalysers() {		//TODO create a correct listing
		List<Analyser> possibleAnalysers = new LinkedList<>();
		possibleAnalysers.add(new VictoryAnalyser());
		possibleAnalysers.add(new VictoryAnalyser());
		return possibleAnalysers;
	}
	
}
