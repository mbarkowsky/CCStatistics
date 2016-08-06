package deprecated.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import deprecated.builder.attackbuilder.AttackBuilder;
import deprecated.builder.attackbuilder.FlingAttackBuilder;
import deprecated.builder.attackbuilder.GenericAttackBuilder;
import deprecated.builder.attackbuilder.KnockOffAttackBuilder;
import deprecated.builder.attackbuilder.SwitchAttackBuilder;
import deprecated.game.Game;
import deprecated.game.Turn;
import deprecated.game.Game.Player;
import deprecated.game.playeraction.AttackAction;
import deprecated.game.playeraction.AttackEffect;
import deprecated.game.playeraction.PlayerAction;
import deprecated.game.playeraction.SwitchAction;
import deprecated.game.playeraction.PlayerAction.ActionType;

public class GameBuilder {

	private Map<String, ActionType> actionKeyWords;
	private Map<String, AttackBuilder> attackBuilders;
	
	private String playerOneName;
	private String playerTwoName;
	private String[] log;
	private Integer lastIndex;
	
	public GameBuilder(){
		initializeKeyWords();
		initializeAttackBuilders();
	}

	private void initializeKeyWords() {
		actionKeyWords = new HashMap<>();
		actionKeyWords.put("used", ActionType.ATTACK);
		actionKeyWords.put("withdrew", ActionType.SWITCH);
		actionKeyWords.put("back!", ActionType.SWITCH);
	}

	private void initializeAttackBuilders() {
		attackBuilders = new HashMap<>();
		
		AttackBuilder genericBuilder = new GenericAttackBuilder();
		AttackBuilder flingBuilder = new FlingAttackBuilder();
		AttackBuilder switchBuilder = new SwitchAttackBuilder();
		AttackBuilder knockOffBuilder = new KnockOffAttackBuilder();
		
		attackBuilders.put("generic", genericBuilder);
		attackBuilders.put("Fling", flingBuilder);
		attackBuilders.put("U-turn", switchBuilder);
		attackBuilders.put("Volt Switch", switchBuilder);
		attackBuilders.put("Baton Pass", switchBuilder);
		attackBuilders.put("Parting Shot", switchBuilder);
		attackBuilders.put("Knock Off", knockOffBuilder);
	}
	
	public Game buildGame(File file) throws IOException{
		log = readLog(file);
		
		Game game = new Game();
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf("."));
		game.setName(name);
		lastIndex = 0;
		buildHeader(game);
		buildTurns(game);
		buildTail(game);
	
		return game;
	}

	private String[] readLog(File file) throws IOException{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		List<String> lines = new ArrayList<String>();
		String line;
		while((line = br.readLine()) != null){
			lines.add(line);
		}
		br.close();
		fr.close();
		String[] log = new String[lines.size()];
		return lines.toArray(log);
	}

	private void buildHeader(Game game){
		String line;
		
		while(!(line = log[lastIndex++]).startsWith("Battle between"));
		
		line = line.substring(15, line.length() - 9);
		String[]names = line.split(" and ");
		
		
		while(!(line = log[lastIndex++]).contains("sent out"));
		
		playerOneName = line.startsWith(names[1]) ? names[0] : names[1];
		playerTwoName = line.startsWith(names[1]) ? names[1] : names[0];
		
		game.setPlayerName(Player.PLAYER_ONE, playerOneName);
		game.setPlayerName(Player.PLAYER_TWO, playerTwoName);
		
		while(!(line = log[lastIndex++]).equals("Turn 1"));
		lastIndex--;
	}
	
	private void buildTurns(Game game){
		Turn turn;
		while((turn = buildTurn()) != null){
			game.addTurn(turn);
		}
	}

	private void buildTail(Game game) {
		String line = log[lastIndex];
		String winner = line.substring(0, line.length() - 16);
		if(winner.equals(playerOneName)){
			game.setWinner(Player.PLAYER_ONE);	
		}
		else{
			game.setWinner(Player.PLAYER_TWO);
		}
	}
	
	private Turn buildTurn() {
		if(!log[lastIndex].matches("Turn (\\d)*")){
			return null;
		}
		Turn turn = new Turn();
		lastIndex++;
		while(!log[lastIndex].matches("Turn (\\d)*")){
			String line = log[lastIndex];
			if(log[lastIndex].endsWith("won the battle!")){
				return turn;
			}
			else {
				ActionType actionType = getActionType(line);
				if(actionType != null){
					Player player = identifyPlayer(line);
					PlayerAction action = buildPlayerAction(actionType, player, line);
					turn.addPlayerAction(player, action);	
				}
				else{
					lastIndex++;
				}
			}
		}
		return turn;
	}

	private Player identifyPlayer(String line) {
		if(line.contains("The opposing") || line.startsWith(playerTwoName)){
			return Player.PLAYER_TWO;
		}
		else{
			return Player.PLAYER_ONE;	
		}
	}
	
	private ActionType getActionType(String line){
		StringTokenizer st = new StringTokenizer(line);
		while(st.hasMoreTokens()){
			String token = st.nextToken();
			if(actionKeyWords.containsKey(token)){
				return actionKeyWords.get(token);
			}
		}
		return null;
	}
	
	private PlayerAction buildPlayerAction(ActionType actionType, Player player, String line) {
		PlayerAction action = null;
		
		switch(actionType){
		case ATTACK:
			switch(player){
			case PLAYER_ONE:
				action = buildPlayerOneAttack(line);
				break;
			case PLAYER_TWO:
				action = buildPlayerTwoAttack(line);
				break;
			default:
				break;
			}
			break;
			

		case SWITCH:
			switch(player){
			case PLAYER_ONE:
				action = buildPlayerOneSwitch(line);
				break;
			case PLAYER_TWO:
				action = buildPlayerTwoSwitch(line);
				break;
			default:
				break;
			
			}
			break;
			
			
		default:
			break;
		}
		return action;
	}

	private PlayerAction buildPlayerOneAttack(String line) {
		line = line.substring(0, line.length() - 1);
		String[] strings = line.split(" used ");
		AttackAction action = new AttackAction(strings[0], strings[1]);
		lastIndex++;
		AttackBuilder builder = getAttackBuilder(strings[1]);
		List<AttackEffect> effects = builder.buildEffects(Player.PLAYER_ONE, action, log, lastIndex);
		action.addEffects(effects);
		return action;
	}

	private PlayerAction buildPlayerTwoAttack(String line) {
		line = line.substring(13, line.length() - 1);
		String[] strings = line.split(" used ");
		AttackAction action = new AttackAction(strings[0], strings[1]);
		lastIndex++;
		AttackBuilder builder = getAttackBuilder(strings[1]);
		List<AttackEffect> effects = builder.buildEffects(Player.PLAYER_TWO, action, log, lastIndex);
		action.addEffects(effects);
		return action;
	}
	
	private AttackBuilder getAttackBuilder(String attackName){
		AttackBuilder builder = attackBuilders.get(attackName);
		if(builder == null){
			builder = attackBuilders.get("generic");
		}
		return builder;
	}
	
	private PlayerAction buildPlayerOneSwitch(String line) {
		String switchOut = line.substring(0, line.length() - 12);
		lastIndex++;
		line = log[lastIndex];
		String switchIn = line.substring(4, line.length() - 1);
		SwitchAction action = new SwitchAction(switchOut, switchIn);
		lastIndex++;
		return action;
	}
	
	private PlayerAction buildPlayerTwoSwitch(String line) {
		String switchOut = line.substring(playerTwoName.length() + 10, line.length() - 1);
		lastIndex++;
		line = log[lastIndex];
		String switchIn = line.substring(playerTwoName.length() + 10, line.length() - 1);
		SwitchAction action = new SwitchAction(switchOut, switchIn);
		lastIndex++;
		return action;
	}
	
}
