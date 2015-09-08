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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import analysis.Analyser;

public class ExplosionFailAnalyser extends Analyser {

	private Set<String> validMoves;
	
	public ExplosionFailAnalyser() {
		validMoves = new HashSet<>();
		validMoves.add("Explosion");
		validMoves.add("Self-Destruct");
		validMoves.add("Final Gambit");
	}
	
	@Override
	public String getName() {
		return "explosion fails";
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games, String playerName) {
		int fails = 0;
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
					if(playerAction != null && playerAction.getType() == ActionType.ATTACK && validMoves.contains(((AttackAction)playerAction).getAttack())){
						int damage = 0;
						for(AttackEffect effect:((AttackAction)playerAction).getEffects(EffectType.DAMAGE)){
							damage += ((DamageEffect)effect).getDamage();
						}
						if(damage == 0){
							fails++;
						}
					}	
				}
			}
		}
		
		JPanel result = new JPanel();
		LayoutManager layout = new BoxLayout(result, BoxLayout.PAGE_AXIS);
		result.setLayout(layout);
		
		JLabel sacrificesLabel = new JLabel("sacrifices in vain:");
		Font victimFont = new Font("sacrifice font", Font.BOLD, 24);
		sacrificesLabel.setFont(victimFont);
		sacrificesLabel.setHorizontalAlignment(JLabel.CENTER);
		sacrificesLabel.setHorizontalTextPosition(JLabel.CENTER);
		sacrificesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(sacrificesLabel);
		
		JLabel failLabel = new JLabel(Integer.toString(fails));
		Font killFont = new Font("fail font", Font.BOLD, 48);
		failLabel.setFont(killFont);
		failLabel.setHorizontalAlignment(JLabel.CENTER);
		failLabel.setHorizontalTextPosition(JLabel.CENTER);
		failLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.add(failLabel);
		
		return result;
	}

}
