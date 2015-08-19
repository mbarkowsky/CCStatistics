package anaylsis;

import game.Game;
import game.Game.Player;
import game.PlayerAction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class SwitchAnalyser extends Analyser {

	@Override
	public String getName() {
		return "switches";
	}

	@Override
	public JPanel analyse(List<Game> games) {
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		
		//JScrollPane scrollPane = new JScrollPane();	//TODO use the scrollPane
		for(Game game:games){
			//scrollPane.add(createStructure(game));
			resultPanel.add(createStructure(game));
		}
		
		//resultPanel.add(scrollPane);
		return resultPanel;
	}

	private JPanel createStructure(Game game) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new LineBorder(Color.BLACK, 2));
		JLabel gameLabel = new JLabel(game.getName());
		panel.add(gameLabel, BorderLayout.PAGE_START);
		
		JPanel structures = new JPanel();
		JPanel playerOnePanel = createPlayerStructure(game, Player.PLAYER_ONE);
		JPanel playerTwoPanel = createPlayerStructure(game, Player.PLAYER_TWO);
		structures.add(playerOnePanel);
		structures.add(playerTwoPanel);
		
		panel.add(structures, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createPlayerStructure(Game game, Player player){ 		
		JPanel playerStructure = new JPanel(new GridLayout(2, 0));
		JLabel playerLabel = new JLabel(game.getPlayerName(player));
		int height = playerLabel.getPreferredSize().height;
		int width = playerLabel.getPreferredSize().width;
		playerLabel.setPreferredSize(new Dimension(width + 5, height));
		playerStructure.add(playerLabel);
		
		JPanel playerHistory = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setHgap(0);
		playerHistory.setLayout(layout);
		
		for(PlayerAction action:game.getPlayerActions(player)){
			JLabel actionLabel = new JLabel();
			actionLabel.setPreferredSize(new Dimension (10, height));
			actionLabel.setOpaque(true);
			if(action == null){
				actionLabel.setBackground(Color.GRAY);
			}
			else if(action.getType() == PlayerAction.ActionType.ATTACK){
				actionLabel.setBackground(Color.ORANGE);
			}
			else{
				actionLabel.setBackground(Color.BLUE);
			}
			playerHistory.add(actionLabel);
		}
		playerStructure.add(playerHistory);
		
		return playerStructure;
	}

}
