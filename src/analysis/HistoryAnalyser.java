package analysis;

import game.Game;
import game.Game.Player;
import game.playeraction.PlayerAction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import analysis.colorrule.ActionTypeColorRule;
import analysis.colorrule.AttackDamageColorRule;
import analysis.colorrule.ColorRule;
import ui.MainFrame;
import util.AlphaNumericalComparator;
import util.CCStatisticsUtil;
import util.IndentedBoxLayout;
import util.TableLayout;

public class HistoryAnalyser extends Analyser {

	private static Map<String, ColorRule> possibleColorRules;
	

	private static Map<String, ColorRule> getPossibleColorRules() {
		if(possibleColorRules == null){
			possibleColorRules = new HashMap<>();
			
			ColorRule actionTypeRule = new ActionTypeColorRule();
			possibleColorRules.put(actionTypeRule.getName(), actionTypeRule);
			
			ColorRule attackDamageRule = new AttackDamageColorRule();
			possibleColorRules.put(attackDamageRule.getName(), attackDamageRule);	
		}
		return possibleColorRules;
	}
	
	private JPanel ui;
	private ButtonGroup colorRuleGroup;
	
	public HistoryAnalyser(){
		super();
		initializeUI();
	}
	
	private void initializeUI() {
		ui = new JPanel(new BorderLayout());
		ui.add(checkBox, BorderLayout.PAGE_START);
		
		JPanel buttonPanel = new JPanel(new IndentedBoxLayout(10));
		colorRuleGroup = new ButtonGroup();
		boolean selected = true;
		List<ColorRule> colorRules = new ArrayList<>(getPossibleColorRules().values());
		Comparator<ColorRule> c = new Comparator<ColorRule>(){

			@Override
			public int compare(ColorRule o1, ColorRule o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
			
		};
		Collections.sort(colorRules, c);
		for(ColorRule colorRule:colorRules){
			JRadioButton colorRuleButton = new JRadioButton();
			String name = colorRule.getName();
			colorRuleButton.setText(name);
			colorRuleButton.setActionCommand(name);
			if(selected){
				colorRuleButton.setSelected(selected);
				selected = false;
			}

			colorRuleGroup.add(colorRuleButton);
			buttonPanel.add(colorRuleButton);
		}
		ui.add(buttonPanel, BorderLayout.CENTER);
	}

	@Override
	public String getName() {
		return "history";
	}

	@Override
	public JComponent getUI(){
		return ui;
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games) {
		long t1 = System.currentTimeMillis();
		
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		
		List<Game> gameList = new ArrayList<>(games);
		Comparator<Game> c = new Comparator<Game>(){

			private AlphaNumericalComparator comp = new AlphaNumericalComparator();
			
			@Override
			public int compare(Game o1, Game o2) {
				return comp.compare(o1.getName(), o2.getName());
			}
			
		};
		Collections.sort(gameList, c);
		
		ColorRule colorRule = getSelectedColorRule();
		for(Game game:gameList){
			resultPanel.add(createGameStructure(game, colorRule));
		}
		
		JScrollPane scrollPane = new JScrollPane(resultPanel);
		
		long t2 = System.currentTimeMillis();
		MainFrame.debugPrint(getName() + " analysis took " + (t2 - t1) + " milliseconds");
		
		return scrollPane;
	}

	private ColorRule getSelectedColorRule() {
		String actionCommand = colorRuleGroup.getSelection().getActionCommand();
		ColorRule colorRule = getPossibleColorRules().get(actionCommand);
		return colorRule;
	}

	private JPanel createGameStructure(Game game, ColorRule colorRule) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new LineBorder(Color.BLACK, 2));
		JLabel gameLabel = new JLabel(game.getName());
		gameLabel.setHorizontalAlignment(JLabel.CENTER);
		gameLabel.setHorizontalTextPosition(JLabel.CENTER);
		panel.add(gameLabel, BorderLayout.PAGE_START);
		
		int[] columnTypes = {TableLayout.MINIMUM_COLUMN_WIDTH, TableLayout.SCALING_COLUMN_WIDTH};
		JPanel playerStructures = new JPanel(new TableLayout(2, columnTypes));
		JLabel playerOneLabel = createPlayerLabel(game, Player.PLAYER_ONE);
		JPanel playerOneHistory = createPlayerHistory(game, Player.PLAYER_ONE, playerOneLabel.getPreferredSize().height, colorRule);	
		JLabel playerTwoLabel = createPlayerLabel(game, Player.PLAYER_TWO);
		JPanel playerTwoHistory = createPlayerHistory(game, Player.PLAYER_TWO, playerOneLabel.getPreferredSize().height, colorRule);
		playerStructures.add(playerOneLabel);
		playerStructures.add(playerOneHistory);
		playerStructures.add(playerTwoLabel);
		playerStructures.add(playerTwoHistory);
		
		panel.add(playerStructures, BorderLayout.CENTER);
		return panel;
	}

	private JLabel createPlayerLabel(Game game, Player player){
		JLabel playerLabel = new JLabel(game.getPlayerName(player));
		playerLabel.setHorizontalAlignment(JLabel.CENTER);
		playerLabel.setHorizontalTextPosition(JLabel.CENTER);
		
		playerLabel.setVerticalAlignment(JLabel.CENTER);
		playerLabel.setVerticalTextPosition(JLabel.CENTER);
		if(player == game.getWinner()){
			playerLabel.setForeground(CCStatisticsUtil.goodColor);
		}
		else{
			playerLabel.setForeground(CCStatisticsUtil.badColor);
		}
		int height = playerLabel.getPreferredSize().height;
		int width = playerLabel.getPreferredSize().width;
		playerLabel.setPreferredSize(new Dimension(width + 25, height + 10));
		return playerLabel;
	}

	private JPanel createPlayerHistory(Game game, Player player, int height, ColorRule colorRule) {
		int labelHeight = height - 10;
		int vgap = 5;
		JPanel playerHistory = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setVgap(vgap);
		layout.setHgap(0);
		playerHistory.setLayout(layout);
		
		for(PlayerAction action:game.getPlayerActions(player)){
			JLabel actionLabel = createActionLabel(action, labelHeight, colorRule);
			playerHistory.add(actionLabel);
		}
		return playerHistory;
	}

	private JLabel createActionLabel(PlayerAction action, int height, ColorRule colorRule) {
		JLabel actionLabel = new JLabel();
		actionLabel.setPreferredSize(new Dimension (10, height));
		actionLabel.setOpaque(true);
		
		actionLabel.setBackground(colorRule.getActionColor(action));
		
		if(action != null){
			actionLabel.setToolTipText(action.toString());	
		}
		
		Border border = new LineBorder(Color.BLACK, 1);
		actionLabel.setBorder(border);
		
		return actionLabel;
	}

}
