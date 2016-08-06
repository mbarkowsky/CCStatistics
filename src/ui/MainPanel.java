package ui;

import game.GameXML;

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import analysis.AnalyserXML;
import builder.GameLoader;
import util.TableLayout;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -8958557846840490170L;
	private GameLoader gameLoader;
	private List<AnalyserXML> analysers;
	
	private JPanel selectionPanel;
	private JPanel resultPanel;
	private JTabbedPane tabbedPanel;
	private JTextField playerField;
	
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
		List<AnalyserXML> possibleAnalysers = getPossibleAnalysersXML();
		for(AnalyserXML analyser:possibleAnalysers){
			analysers.add(analyser);
		}
	}

	private void initializeUI(){
		setLayout(new GridLayout(0, 2));
		
		selectionPanel = new JPanel();
		selectionPanel.setLayout(new BorderLayout());
		
		JPanel playerPanel = new JPanel(new GridLayout(0, 1));

		JLabel playerLabel = new JLabel("Player:");
		playerLabel.setHorizontalTextPosition(JLabel.CENTER);
		playerPanel.add(playerLabel);
		
		playerField = new JTextField();
		playerField.setHorizontalAlignment(JTextField.CENTER);
		playerPanel.add(playerField);
		
		selectionPanel.add(playerPanel, BorderLayout.PAGE_START);
		
		JPanel selection = new JPanel();
		selection.setLayout(new TableLayout(1));
		for(AnalyserXML analyser:analysers){
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
		
		String playerName = getSelectedPlayerName();
		
		boolean newResult = false;
		JTabbedPane newResultPanel = new JTabbedPane();
		for(AnalyserXML analyser:analysers){
			Map<File, GameXML> games = gameLoader.getGames();
			JComponent analysisResult = analyser.analyse(games.values(), playerName);
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
	
	private String getSelectedPlayerName() {
		return playerField.getText();
	}

	private void exportCurrentResult() {
		Component currentSelection = tabbedPanel.getSelectedComponent();
		BufferedImage image = new BufferedImage(currentSelection.getWidth(), currentSelection.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		currentSelection.paintAll(graphics);
		String exportSuggestion = currentSelection.getName().replace(' ', '_');
		String output = (String) JOptionPane.showInputDialog(null, "File Name", "Export", JOptionPane.PLAIN_MESSAGE, null, null, exportSuggestion);
		if(output != null){
			try {
				ImageIO.write(image, "png", new File("export/" + output + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

	private void loadGames() {
		gameLoader.loadGames(MainFrame.gameDirectoryPath);	
	}
	
	private List<AnalyserXML> getPossibleAnalysersXML() {
		List<AnalyserXML> possibleAnalysers = new LinkedList<>();
		possibleAnalysers.add(new analysis.VictoryAnalyser());
		return possibleAnalysers;
	}

	
}
