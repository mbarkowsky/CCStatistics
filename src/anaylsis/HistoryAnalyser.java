package anaylsis;

import game.Game;
import game.Game.Player;
import game.playeraction.PlayerAction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import ui.MainFrame;
import util.CCStatisticsUtil;

public class HistoryAnalyser implements Analyser {

	@Override
	public String getName() {
		return "history";
	}

	@Override
	public JComponent analyse(Collection<Game> games) {
		long t1 = System.currentTimeMillis();
		
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		
		for(Game game:games){
			resultPanel.add(createStructure(game));
		}
		
		JScrollPane scrollPane = new JScrollPane(resultPanel);
		
		long t2 = System.currentTimeMillis();
		MainFrame.debugPrint(getName() + " analysis took " + (t2 - t1) + " milliseconds");
		
		return scrollPane;
	}

	private JPanel createStructure(Game game) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new LineBorder(Color.BLACK, 2));
		JLabel gameLabel = new JLabel(game.getName());
		gameLabel.setHorizontalAlignment(JLabel.CENTER);
		gameLabel.setHorizontalTextPosition(JLabel.CENTER);
		panel.add(gameLabel, BorderLayout.PAGE_START);
		
		JPanel structures = new JPanel();
		structures.setLayout(new BoxLayout(structures, BoxLayout.Y_AXIS));
		JPanel playerOnePanel = createPlayerStructure(game, Player.PLAYER_ONE);
		JPanel playerTwoPanel = createPlayerStructure(game, Player.PLAYER_TWO);
		structures.add(playerOnePanel);
		structures.add(playerTwoPanel);
		
		panel.add(structures, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createPlayerStructure(Game game, Player player){ 		
		JPanel playerStructure = new JPanel(new GridLayout(0, 2));
		JLabel playerLabel = new JLabel(game.getPlayerName(player));
		playerLabel.setHorizontalAlignment(JLabel.CENTER);
		playerLabel.setHorizontalTextPosition(JLabel.CENTER);
		if(player == game.getWinner()){
			playerLabel.setForeground(CCStatisticsUtil.goodColor);
		}
		else{
			playerLabel.setForeground(CCStatisticsUtil.badColor);
		}
		int height = playerLabel.getPreferredSize().height;
		int width = playerLabel.getPreferredSize().width;
		playerLabel.setPreferredSize(new Dimension(width + 5, height));
		playerStructure.add(playerLabel);
		
		JPanel playerHistory = createPlayerHistory(game, player, height);
		playerStructure.add(playerHistory);
		
		return playerStructure;
	}

	private JPanel createPlayerHistory(Game game, Player player, int labelHeight) {
		JPanel playerHistory = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setHgap(0);
		playerHistory.setLayout(layout);
		
		for(PlayerAction action:game.getPlayerActions(player)){
			JLabel actionLabel = createActionLabel(action, labelHeight);
			playerHistory.add(actionLabel);
		}
		return playerHistory;
	}

	private JLabel createActionLabel(PlayerAction action, int height) {
		JLabel actionLabel = new JLabel();
		actionLabel.setPreferredSize(new Dimension (10, height));
		actionLabel.setOpaque(true);
		
		if(action == null){
			actionLabel.setBackground(CCStatisticsUtil.badColor);
		}
		else{
			if(action.getType() == PlayerAction.ActionType.ATTACK){
				actionLabel.setBackground(CCStatisticsUtil.goodColor);
			}
			else{
				actionLabel.setBackground(CCStatisticsUtil.neutralColor);
			}

			actionLabel.setToolTipText(action.toString());
		}
		
		Border border = new LineBorder(Color.BLACK, 1);
		actionLabel.setBorder(border);
		
		return actionLabel;
	}

}
