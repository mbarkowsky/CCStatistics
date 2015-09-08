package analysis.misc;

import game.Game;
import game.Turn;
import game.Game.Player;
import game.playeraction.AttackAction;
import game.playeraction.AttackEffect;
import game.playeraction.DamageEffect;
import game.playeraction.PlayerAction;
import game.playeraction.AttackEffect.EffectType;
import game.playeraction.PlayerAction.ActionType;

import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import analysis.Analyser;

public class FlingKillAnalyser extends Analyser {

	@Override
	public String getName() {
		return "fling kills";
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games, String playerName) {
		int kills = 0;
		for(Game game:games){
			List<Player> players = new LinkedList<>();
			if(playerName.equals("")){
				players.add(Player.PLAYER_ONE);
				players.add(Player.PLAYER_TWO);
			}
			else{
				Player player = game.getPlayer(playerName);
				if(player == null){
					continue;
				}
				players.add(player);	
			}
			
			for(Turn turn:game.getTurns()){				
				for(Player player:players){
					PlayerAction playerAction = turn.getPlayerAction(player);
					if(playerAction != null && playerAction.getType() == ActionType.ATTACK && ((AttackAction)playerAction).getAttack().equals("Fling")){
						for(AttackEffect effect:((AttackAction)playerAction).getEffects(EffectType.DAMAGE)){
							if(((DamageEffect)effect).isKO()){
								kills++;
							}
						}
					}	
				}
			}
		}
		
		JPanel result = new JPanel();
		LayoutManager layout = new BoxLayout(result, BoxLayout.PAGE_AXIS);
		result.setLayout(layout);
		
		JLabel goliathLabel = new JLabel("Goliaths felled:");
		Font goliathFont = new Font("victim font", Font.BOLD, 24);
		goliathLabel.setFont(goliathFont);
		goliathLabel.setHorizontalAlignment(JLabel.CENTER);
		goliathLabel.setHorizontalTextPosition(JLabel.CENTER);
		goliathLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(goliathLabel);
		
		JLabel killLabel = new JLabel(Integer.toString(kills));
		Font killFont = new Font("kill font", Font.BOLD, 48);
		killLabel.setFont(killFont);
		killLabel.setHorizontalAlignment(JLabel.CENTER);
		killLabel.setHorizontalTextPosition(JLabel.CENTER);
		killLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(killLabel);
		
		return result;
	}

}
