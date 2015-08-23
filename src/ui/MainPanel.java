package ui;

import game.Game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import builder.GameBuilder;
import builder.GameBuilderWorker;
import anaylsis.Analyser;
import anaylsis.HistoryAnalyser;
import anaylsis.AttackOccurrenceAnalyser;
import anaylsis.VictoryAnalyser;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -8958557846840490170L;
	private static final String gameDirectoryPath = "games"; 
	
	private Map<File, Game> games;
	private Map<JCheckBox, Analyser> analysers;
	
	private List<JCheckBox> checkBoxes;
	private JPanel selectionPanel;
	private JTabbedPane resultPanel;
	
	private Vector<GameBuilder> idleBuilders;
	
	public MainPanel(int builderNumber){
		initializeBuilders(builderNumber);
		initializeAnalysers();
		initializeUI();
	}
	
	public MainPanel(){
		this(3);
	}
	
	private void initializeBuilders(int builderNumber) {
		idleBuilders = new Vector<>();
		for(int i = 0; i < builderNumber; i++){
			idleBuilders.add(new GameBuilder());
		}
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
		long t1 = System.currentTimeMillis();
		loadGames();
		long t2 = System.currentTimeMillis();
		MainFrame.debugPrint("Loading games took " + (t2 - t1) + " milliseconds");
		
		boolean newResult = false;
		JTabbedPane newResultPanel = new JTabbedPane();
		for(JCheckBox checkBox:checkBoxes){
			if(checkBox.isSelected()){
				Analyser analyser = analysers.get(checkBox);
				JComponent analysisResult = analyser.analyse(games.values());
				if(analysisResult != null){
					newResultPanel.addTab(analyser.getName(), analysisResult);	
					newResult = true;
				}
			}
		}
		
		if(newResult){
			if(resultPanel != null){
				remove(resultPanel);	
			}
			add(newResultPanel);
			resultPanel = newResultPanel;
		}
		
		updateUI();
	}
	
	public void loadGames(){
		if(games == null){
			games = new HashMap<>();
		}
		
		File gameDirectory = new File(gameDirectoryPath);
		File[] gameFiles = gameDirectory.listFiles();

		Map<Thread, GameBuilderWorker> workerThreads = new HashMap<>();
		for(File gameFile:gameFiles){
			if(!games.containsKey(gameFile)){
				try {
					GameBuilder builder = getIdleGameBuilder();
					GameBuilderWorker builderWorker = new GameBuilderWorker(this, builder, gameFile);
					Thread worker = new Thread(builderWorker);
					worker.start();
					workerThreads.put(worker, builderWorker);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		for(Entry<Thread, GameBuilderWorker> entry:workerThreads.entrySet()){
			try {
				entry.getKey().join();
				GameBuilderWorker builderWorker = entry.getValue();
				games.put(builderWorker.getGameFile(), builderWorker.getResult());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private synchronized GameBuilder getIdleGameBuilder() throws InterruptedException {
		while(idleBuilders.size() < 1){
			wait();
		}
		return idleBuilders.remove(0);
	}
	
	public synchronized void returnGameBuilder(GameBuilder builder){
		idleBuilders.add(builder);
		notifyAll();
	}

	private List<Analyser> getPossibleAnalysers() {
		List<Analyser> possibleAnalysers = new LinkedList<>();
		possibleAnalysers.add(new VictoryAnalyser());
		possibleAnalysers.add(new AttackOccurrenceAnalyser());
		possibleAnalysers.add(new HistoryAnalyser());
		return possibleAnalysers;
	}
	
}
