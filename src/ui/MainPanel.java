package ui;

import game.Game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import util.TableLayout;
import builder.GameLoader;
import analysis.Analyser;
import analysis.AttackDetailAnalyser;
import analysis.AttackEfficiencyAnalyser;
import analysis.HistoryAnalyser;
import analysis.VictoryAnalyser;
import analysis.misc.ExplosionFailAnalyser;
import analysis.misc.FlingKillAnalyser;
import analysis.misc.KarpKillAnalyser;
import analysis.misc.SpewpaTankinessAnalyser;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -8958557846840490170L;
	private static final String gameDirectoryPath = "games"; 
	
	private GameLoader gameLoader;
	private List<Analyser> analysers;
	
	private JPanel selectionPanel;
	private JPanel resultPanel;
	private JTabbedPane tabbedPanel;
	
	public MainPanel(int builderNumber){
		gameLoader = new GameLoader(builderNumber);
		initializeAnalysers();
		initializeUI();
	}
	
	public MainPanel(){
		this(3);
	}

	private void initializeAnalysers() {
		analysers = new LinkedList<>();
		List<Analyser> possibleAnalysers = getPossibleAnalysers();
		for(Analyser analyser:possibleAnalysers){
			analysers.add(analyser);
		}
	}

	private void initializeUI(){
		setLayout(new GridLayout(0, 2));
		
		selectionPanel = new JPanel();
		selectionPanel.setLayout(new BorderLayout());
		
		JPanel selection = new JPanel();
		selection.setLayout(new TableLayout(1));
		for(Analyser analyser:analysers){
			selection.add(analyser.getUI());
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
		
		resultPanel = new JPanel(new BorderLayout());
		
		JButton printButton = new JButton("Export");
		printButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				exportCurrentResult();
			}
			
		});
		resultPanel.add(printButton, BorderLayout.PAGE_END);
		
		add(resultPanel);
	}

	private void executeAnalysis() {
		long t1 = System.currentTimeMillis();
		loadGames();
		long t2 = System.currentTimeMillis();
		MainFrame.debugPrint("Loading games took " + (t2 - t1) + " milliseconds");
		
		boolean newResult = false;
		JTabbedPane newResultPanel = new JTabbedPane();
		for(Analyser analyser:analysers){
			Map<File, Game> games = gameLoader.getGames();
			JComponent analysisResult = analyser.analyse(games.values());
			if(analysisResult != null){
				newResultPanel.addTab(analyser.getName(), analysisResult);	
				newResult = true;
			}
		}
		
		if(newResult){
			if(tabbedPanel != null){
				resultPanel.remove(tabbedPanel);	
			}
			resultPanel.add(newResultPanel);
			tabbedPanel = newResultPanel;
		}
		
		updateUI();
	}
	
	private void exportCurrentResult() {
		Component currentSelection = tabbedPanel.getSelectedComponent();
		BufferedImage image = new BufferedImage(currentSelection.getWidth(), currentSelection.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		currentSelection.paintAll(graphics);
		String output = (String) JOptionPane.showInputDialog(null, "File Name", "Export", JOptionPane.PLAIN_MESSAGE, null, null, "result");
		if(output != null){
			try {
				ImageIO.write(image, "png", new File("export/" + output + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

	private void loadGames() {
		gameLoader.loadGames(gameDirectoryPath);
	}

	private List<Analyser> getPossibleAnalysers() {
		List<Analyser> possibleAnalysers = new LinkedList<>();
		possibleAnalysers.add(new VictoryAnalyser());
		possibleAnalysers.add(new AttackDetailAnalyser());
		possibleAnalysers.add(new AttackEfficiencyAnalyser());
		possibleAnalysers.add(new HistoryAnalyser());
		possibleAnalysers.add(new KarpKillAnalyser());
		possibleAnalysers.add(new SpewpaTankinessAnalyser());
		possibleAnalysers.add(new FlingKillAnalyser());
		possibleAnalysers.add(new ExplosionFailAnalyser());
		return possibleAnalysers;
	}
	
}
