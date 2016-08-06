package deprecated.analysis.misc;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deprecated.analysis.Analyser;
import deprecated.game.Game;
import deprecated.game.Turn;
import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;
import deprecated.game.playeraction.DamageEffect;
import deprecated.game.playeraction.PlayerAction;
import deprecated.game.playeraction.AttackEffect.EffectType;
import deprecated.game.playeraction.PlayerAction.ActionType;
import ui.MainFrame;

public class KarpKillAnalyser extends Analyser {

	private Icon karpIcon;
	
	public KarpKillAnalyser(){
		initializeIcon();
	}
	
	private void initializeIcon() {
		ImageIcon icon = new ImageIcon(MainFrame.imagePath + "magikarp.png");
		Image image = icon.getImage();
		karpIcon = new ImageIcon(image);
	}

	@Override
	public String getName() {
		return "karp kills";
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
					if(playerAction != null && playerAction.getType() == ActionType.ATTACK && ((AttackAction)playerAction).getPokemon().equals("Magikarp")){
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
		
		JLabel victimLabel = new JLabel("innocent victims:");
		Font victimFont = new Font("victim font", Font.BOLD, 24);
		victimLabel.setFont(victimFont);
		victimLabel.setHorizontalAlignment(JLabel.CENTER);
		victimLabel.setHorizontalTextPosition(JLabel.CENTER);
		victimLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(victimLabel);
		
		JLabel karpLabel = new JLabel();
		karpLabel.setIcon(karpIcon);
		karpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(karpLabel);
		
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