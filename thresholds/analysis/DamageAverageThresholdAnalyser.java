//package analysis;
//
//import java.util.Collection;
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
//public class DamageAverageThresholdAnalyser implements ThresholdAnalyser{
//
//	@Override
//	public Threshold createThreshold(Collection<Game> samples) {		
//		AvgDevThreshold threshold = new AvgDevThreshold();
//		threshold.setName("damage average");
//		double average = calculateAverageDamage(samples);
//		double deviation = calculateDeviation(samples, average);
//		threshold.setAverage(average);
//		threshold.setDeviation(deviation);
//		return threshold;
//	}
//
//	private double calculateAverageDamage(Collection<Game> samples) {
//		double damageTotal = 0.0;
//		int attacks = 0;
//		
//		for(Game game:samples){
//			for(Turn turn:game.getTurns()){
//				PlayerAction playerOneAction = turn.getPlayerAction(Player.PLAYER_ONE);
//				if(playerOneAction != null && playerOneAction.getType() == ActionType.ATTACK){
//					boolean didDamage = false;
//					for(AttackEffect effect:((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE)){
//						damageTotal += ((DamageEffect)effect).getDamage();
//						didDamage = true;
//					}
//					if(didDamage){
//						attacks++;
//					}
//				}
//				
//				PlayerAction playerTwoAction = turn.getPlayerAction(Player.PLAYER_TWO);
//				if(playerTwoAction != null && playerTwoAction.getType() == ActionType.ATTACK){
//					boolean didDamage = false;
//					for(AttackEffect effect:((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE)){
//						damageTotal += ((DamageEffect)effect).getDamage();
//						didDamage = true;
//					}
//					if(didDamage){
//						attacks++;
//					}
//				}
//			}
//		}
//		return attacks > 0 ? damageTotal/attacks : 0.0;
//	}
//	
//	private double calculateDeviation(Collection<Game> samples, double average) {
//		double totalVar = 0.0;
//		int attacks = 0;
//		
//		for(Game game:samples){
//			for(Turn turn:game.getTurns()){
//				PlayerAction playerOneAction = turn.getPlayerAction(Player.PLAYER_ONE);
//				if(playerOneAction != null && playerOneAction.getType() == ActionType.ATTACK){
//					boolean didDamage = false;
//					double damage = 0.0;
//					for(AttackEffect effect:((AttackAction)playerOneAction).getEffects(EffectType.DAMAGE)){
//						damage += ((DamageEffect)effect).getDamage();
//						didDamage = true;
//					}
//					if(didDamage){
//						totalVar += (average - damage) * (average - damage);
//						attacks++;
//					}
//				}
//				
//				PlayerAction playerTwoAction = turn.getPlayerAction(Player.PLAYER_TWO);
//				if(playerTwoAction != null && playerTwoAction.getType() == ActionType.ATTACK){
//					boolean didDamage = false;
//					double damage = 0.0;
//					for(AttackEffect effect:((AttackAction)playerTwoAction).getEffects(EffectType.DAMAGE)){
//						damage += ((DamageEffect)effect).getDamage();
//						didDamage = true;
//					}
//					if(didDamage){
//						totalVar += (average - damage) * (average - damage);
//						attacks++;
//					}
//				}
//			}
//		}
//		
//		double var = attacks > 0 ? totalVar / attacks : 0.0;
//		double dev = Math.sqrt(var);
//		return dev;
//	}
//
//}
