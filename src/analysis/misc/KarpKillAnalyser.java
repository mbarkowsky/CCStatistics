package analysis.misc;

import game.Game;
import game.Game.Player;
import game.Turn;
import game.playeraction.AttackAction;
import game.playeraction.AttackEffect;
import game.playeraction.AttackEffect.EffectType;
import game.playeraction.DamageEffect;
import game.playeraction.PlayerAction;
import game.playeraction.PlayerAction.ActionType;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.MainFrame;
import analysis.Analyser;

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
	protected JComponent doAnalyse(Collection<Game> games) {
		int kills = 0;
		for(Game game:games){
			for(Turn turn:game.getTurns()){
				PlayerAction playerOneAction = turn.getPlayerAction(Player.PLAYER_ONE);
				if(playerOneAction != null && playerOneAction.getType() == ActionType.ATTACK && ((AttackAction)playerOneAction).getPokemon().equals("Magikarp")){
					for(AttackEffect effect:((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE)){
						if(((DamageEffect)effect).isKO()){
							kills++;
						}
					}
				}
				
				PlayerAction playerTwoAction = turn.getPlayerAction(Player.PLAYER_TWO);
				if(playerTwoAction != null && playerTwoAction.getType() == ActionType.ATTACK && ((AttackAction)playerTwoAction).getPokemon().equals("Magikarp")){
					for(AttackEffect effect:((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE)){
						if(((DamageEffect)effect).isKO()){
							kills++;
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
