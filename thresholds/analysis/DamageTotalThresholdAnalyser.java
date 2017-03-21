//package analysis;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import deprecated.game.Game;
//import deprecated.game.Turn;
//import deprecated.game.Game.Player;
//import deprecated.game.playeraction.AttackAction;
//import deprecated.game.playeraction.AttackEffect;
//import deprecated.game.playeraction.DamageEffect;
//import deprecated.game.playeraction.PlayerAction;
//import deprecated.game.playeraction.AttackEffect.EffectType;
//import deprecated.game.playeraction.PlayerAction.ActionType;
//import threshold.AvgDevThreshold;
//import threshold.Threshold;
//
//public class DamageTotalThresholdAnalyser implements ThresholdAnalyser {
//
//	@Override
//	public Threshold createThreshold(Collection<Game> samples) {		
//		AvgDevThreshold threshold = new AvgDevThreshold();
//		threshold.setName("damage total");
//		double total = calculateTotalDamage(samples);
//		double deviation = calculateDeviation(samples, total);
//		threshold.setAverage(total);
//		threshold.setDeviation(deviation);
//		return threshold;
//	}
//
//	private double calculateTotalDamage(Collection<Game> samples) {
//		double damageTotal = 0.0;
//		Set<String> attacks = new HashSet<>();
//		for(Game game:samples){
//			for(Turn turn:game.getTurns()){
//				PlayerAction playerOneAction = turn.getPlayerAction(Player.PLAYER_ONE);
//				if(playerOneAction != null && playerOneAction.getType() == ActionType.ATTACK){
//					for(AttackEffect effect:((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE)){
//						damageTotal += ((DamageEffect)effect).getDamage();
//						attacks.add(((AttackAction)playerOneAction).getAttack());
//					}
//				}
//				
//				PlayerAction playerTwoAction = turn.getPlayerAction(Player.PLAYER_TWO);
//				if(playerTwoAction != null && playerTwoAction.getType() == ActionType.ATTACK){
//					for(AttackEffect effect:((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE)){
//						damageTotal += ((DamageEffect)effect).getDamage();
//						attacks.add(((AttackAction)playerTwoAction).getAttack());
//					}
//				}
//			}
//		}
//		return damageTotal / attacks.size();
//	}
//	
//	private double calculateDeviation(Collection<Game> samples, double average) {
//		Map<String, Double> totals = new HashMap<>();
//		
//		for(Game game:samples){
//			for(Turn turn:game.getTurns()){
//				PlayerAction playerOneAction = turn.getPlayerAction(Player.PLAYER_ONE);
//				if(playerOneAction != null && playerOneAction.getType() == ActionType.ATTACK){
//					String attack = ((AttackAction)playerOneAction).getAttack();
//					boolean didDamage = false;
//					double damage = 0.0;
//					for(AttackEffect effect:((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE)){
//						damage += ((DamageEffect)effect).getDamage();
//						didDamage = true;
//					}
//					if(didDamage){
//						if(!totals.containsKey(attack)){
//							totals.put(attack, 0.0);
//						}
//						totals.put(attack, totals.get(attack) + damage);
//					}
//				}
//				
//				PlayerAction playerTwoAction = turn.getPlayerAction(Player.PLAYER_TWO);
//				if(playerTwoAction != null && playerTwoAction.getType() == ActionType.ATTACK){
//					String attack = ((AttackAction)playerTwoAction).getAttack();
//					boolean didDamage = false;
//					double damage = 0.0;
//					for(AttackEffect effect:((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE)){
//						damage += ((DamageEffect)effect).getDamage();
//						didDamage = true;
//					}
//					if(didDamage){
//						if(!totals.containsKey(attack)){
//							totals.put(attack, 0.0);
//						}
//						totals.put(attack, totals.get(attack) + damage);
//					}
//				}
//			}
//		}
//		
//		List<Double> vars = new LinkedList<>();
//		for(double total:totals.values()){
//			vars.add((total - average) * (total - average));
//		}
//		
//		double totalVar = 0.0;
//		for(double var:vars){
//			totalVar += var;
//		}
//		
//		double var = totals.size() > 0 ? totalVar / totals.size() : 0.0;
//		double dev = Math.sqrt(var);
//		return dev;
//	}
//
//}
