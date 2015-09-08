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
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.MainFrame;
import analysis.Analyser;

public class SpewpaTankinessAnalyser extends Analyser {

	private Icon spewpaIcon;
	
	public SpewpaTankinessAnalyser(){
		initializeIcon();
	}
	
	private void initializeIcon() {
		ImageIcon icon = new ImageIcon(MainFrame.imagePath + "spewpa.png");
		Image image = icon.getImage();
		spewpaIcon = new ImageIcon(image);
	}
	
	@Override
	public String getName() {
		return "spewpa tankiness";
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games) {
		int turns = 0;
		for(Game game:games){
			for(Turn turn:game.getTurns()){
				PlayerAction playerOneAction = turn.getPlayerAction(Player.PLAYER_ONE);
				if(playerOneAction != null && playerOneAction.getType() == ActionType.ATTACK){
					List<AttackEffect> effects = ((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE);
					if(effects.size() > 0){
						boolean tanked = true;
						for(AttackEffect effect:effects){
							if(!((DamageEffect)effect).getDefender().equals("Spewpa") || ((DamageEffect)effect).isKO()){
								tanked = false;
							}
						}
						if(tanked){
							turns++;
						}	
					}
				}
				
				PlayerAction playerTwoAction = turn.getPlayerAction(Player.PLAYER_TWO);
				if(playerTwoAction != null && playerTwoAction.getType() == ActionType.ATTACK){
					List<AttackEffect> effects = ((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE);
					if(effects.size() > 0){
						boolean tanked = true;
						for(AttackEffect effect:effects){
							if(!((DamageEffect)effect).getDefender().equals("Spewpa") || ((DamageEffect)effect).isKO()){
								tanked = false;
							}
						}
						if(tanked){
							turns++;
						}	
					}
				}
			}
		}
		
		JPanel result = new JPanel();
		LayoutManager layout = new BoxLayout(result, BoxLayout.PAGE_AXIS);
		result.setLayout(layout);
		
		JLabel walledLabel = new JLabel("walled attacks:");
		Font walledFont = new Font("walled font", Font.BOLD, 24);
		walledLabel.setFont(walledFont);
		walledLabel.setHorizontalAlignment(JLabel.CENTER);
		walledLabel.setHorizontalTextPosition(JLabel.CENTER);
		walledLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(walledLabel);
		
		JLabel spewpaLabel = new JLabel();
		spewpaLabel.setIcon(spewpaIcon);
		spewpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(spewpaLabel);
		
		JLabel turnLabel = new JLabel(Integer.toString(turns));
		Font turnFont = new Font("turns font", Font.BOLD, 48);
		turnLabel.setFont(turnFont);
		turnLabel.setHorizontalAlignment(JLabel.CENTER);
		turnLabel.setHorizontalTextPosition(JLabel.CENTER);
		turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(turnLabel);
		
		return result;
	}

}
