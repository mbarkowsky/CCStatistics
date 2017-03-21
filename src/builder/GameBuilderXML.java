package builder;

import game.GameXML;
import game.TurnXML;
import game.GameXML.Player;
import game.event.AbilityEventXML;
import game.event.ActivateEventXML;
import game.event.BoostEventXML;
import game.event.CantEventXML;
import game.event.CritEventXML;
import game.event.CureStatusEventXML;
import game.event.CureTeamEvent;
import game.event.DamageEventXML;
import game.event.EffectivenessEventXML;
import game.event.EndAbilityEventXML;
import game.event.EndEventXML;
import game.event.EventXML;
import game.event.FailEventXML;
import game.event.FaintEventXML;
import game.event.FieldEndEventXML;
import game.event.FieldStartEventXML;
import game.event.FormeChangeEvent;
import game.event.HealEventXML;
import game.event.ItemEventXML;
import game.event.MissEventXML;
import game.event.MoveEventXML;
import game.event.NoTargetEvent;
import game.event.PrepareEventXML;
import game.event.RechargeEventXML;
import game.event.SetHPEventXML;
import game.event.SideEndEventXML;
import game.event.SideStartEventXML;
import game.event.SingleTurnEventXML;
import game.event.StartEventXML;
import game.event.StatusEventXML;
import game.event.SwitchEventXML;
import game.event.WeatherEventXML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import util.GameUtil;
import util.IntegerWrapper;
import util.GameUtil.Effectiveness;
import util.GameUtil.Status;

public class GameBuilderXML {

	public GameXML buildGame(File file){
		GameXML game = null;
		try {
			String[] log = readLog(file);
			game = buildGameFromLog(log);
			game.setName(file.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return game;
	}

	private GameXML buildGameFromLog(String[] log) {
		GameXML game = new GameXML();
		buildPlayers(game, log);
		buildTurns(game, log);
		buildWinner(game, log);
		
		return game;
	}

	private void buildPlayers(GameXML game, String[] log) {
		StringTokenizer st = new StringTokenizer(log[0], "|");
		st.nextToken();
		st.nextToken();
		String playerOneName = st.nextToken();
		game.setPlayerName(Player.PLAYER_ONE, playerOneName);
		
		int i;
		for(i = 1; i < log.length && !log[i].startsWith("|player"); i++);
		st = new StringTokenizer(log[i], "|");
		st.nextToken();
		st.nextToken();
		String playerTwoName = st.nextToken();
		game.setPlayerName(Player.PLAYER_TWO, playerTwoName);
	}
	
	private void buildTurns(GameXML game, String[] log) {
		IntegerWrapper i = new IntegerWrapper();
		for(i.setInt(0); i.getInt() < log.length && !log[i.getInt()].startsWith("|start"); i.increment());
		i.increment();;
		
		TurnXML turn0 = new TurnXML();
		for(; i.getInt() < log.length && log[i.getInt()].startsWith("|turn"); i.increment()){
			EventXML event = buildEvent(log, i);
			turn0.addEvent(event);
		}
		
		for(; i.getInt() < log.length - 1; i.increment()){
			TurnXML turn = new TurnXML();
			while(!log[i.getInt()].startsWith("|turn") && i.getInt() < log.length - 1){
				EventXML event = buildEvent(log, i);
				if(event != null){
					turn.addEvent(event);
				}
				i.increment();;
			}
			game.addTurn(turn);
		}
	}

	private EventXML buildEvent(String[] log, IntegerWrapper i) {
		EventXML event = null;
		String line = log[i.getInt()];
		if(line.startsWith("|move")){
			event = buildMoveEvent(log, i);
		}
		else if(line.startsWith("|switch")){
			event = buildSwitchEvent(log, i);
		}
		else if(line.startsWith("|drag")){
			event = buildSwitchEvent(log, i);
		}
		else if(line.startsWith("|faint")){
			event = buildFaintEvent(log, i);
		}
		else if(line.startsWith("|cant")){
			event = buildCantEvent(log, i);
		}
		else if(line.startsWith("|-activate")){
			event = buildActivateEvent(log, i);
		}
		else if(line.startsWith("|-ability")){
			event = buildAbilityEvent(log, i);
		}
		else if(line.startsWith("|-endability")){
			event = buildEndAbilityEvent(log, i);
		}
		else if(line.startsWith("|-boost")){
			event = buildBoostEvent(log, i);
		}
		else if(line.startsWith("|-unboost")){
			event = buildUnBoostEvent(log, i);
		}
		else if(line.startsWith("|-prepare")){
			event = buildPrepareEvent(log, i);
		}
		else if(line.startsWith("|-miss")){
			event = buildMissEvent(log, i);
		}
		else if(line.startsWith("|-damage")){
			event = buildDamageEvent(log, i);
		}
		else if(line.startsWith("|-supereffective")){
			event = buildEffectivenessEvent(log, i);
		}
		else if(line.startsWith("|-resisted")){
			event = buildEffectivenessEvent(log, i);
		}
		else if(line.startsWith("|-immune")){
			event = buildEffectivenessEvent(log, i);
		}
		else if(line.startsWith("|-crit")){
			event = buildCritEvent(log, i);
		}
		else if(line.startsWith("|-notarget")){
			event = buildNoTargetEvent(log, i);
		}
		else if(line.startsWith("|-start")){
			event = buildStartEvent(log, i);
		}
		else if(line.startsWith("|-end")){
			event = buildEndEvent(log, i);
		}
		else if(line.startsWith("|-status")){
			event = buildStatusEvent(log, i);
		}
		else if(line.startsWith("|-curestatus")){
			event = buildCureStatusEvent(log, i);
		}
		else if(line.startsWith("|-weather")){
			event = buildWeatherEvent(log, i);
		}
		else if(line.startsWith("|-fail")){
			event = buildFailEvent(log, i);
		}
		else if(line.startsWith("|-item")){
			event = buildItemEvent(log, i);
		}
		else if(line.startsWith("|-sidestart")){
			event = buildSideStartEvent(log, i);
		}
		else if(line.startsWith("|-sideend")){
			event = buildSideEndEvent(log, i);
		}
		else if(line.startsWith("|-sethp")){
			event = buildSetHPEvent(log, i);
		}
		else if(line.startsWith("|-heal")){
			event = buildHealEvent(log, i);
		}
		else if(line.startsWith("|-singleturn")){
			event = buildSingleTurnEvent(log, i);
		}
		else if(line.startsWith("|-singlemove")){
			event = buildSingleTurnEvent(log, i);
		}
		else if(line.startsWith("|-cureteam")){
			event = buildCureTeamEvent(log, i);
		}
		else if(line.startsWith("|-fieldstart")){
			event = buildFieldStartEvent(log, i);
		}
		else if(line.startsWith("|-fieldend")){
			event = buildFieldEndEvent(log, i);
		}
		else if(line.startsWith("|-formechange")){
			event = buildFormChangeEvent(log, i);
		}
		else if(line.startsWith("|-mustrecharge")){
			event = buildRechargeEvent(log, i);
		}
		else if(line.startsWith("|") && !line.equals("") && !line.equals("|") && !line.startsWith("|c|") && !line.startsWith("|raw|") && !line.startsWith("|choice")  && !line.startsWith("|join") && !line.startsWith("|J|") && !line.startsWith("|L|") && !line.startsWith("|player|") && !line.startsWith("|-message|") && !line.startsWith("|message|") && !line.startsWith("|-hint|") && !line.startsWith("|-hitcount") && !line.startsWith("|inactive") && !line.startsWith("|-anim") && !line.startsWith("|leave")){
			System.out.println("unrecognised event: " + log[i.getInt()]);
		}
		return event;
	}

	private EventXML buildRechargeEvent(String[] log, IntegerWrapper i) {
		RechargeEventXML event = new RechargeEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String[] source = st.nextToken().split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		return event;
	}

	private EventXML buildFormChangeEvent(String[] log, IntegerWrapper i) {
		FormeChangeEvent event = new FormeChangeEvent();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String[] source = st.nextToken().split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String oldForm = source[1];
		String newForm = st.nextToken();
		
		event.setOwner(owner);
		event.setOldForm(oldForm);
		event.setNewForm(newForm);
		return event;
	}

	private EventXML buildFieldEndEvent(String[] log, IntegerWrapper i) {
		FieldEndEventXML event = new FieldEndEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String effect = st.nextToken();
		
		event.setEffect(effect);
		
		return event;
	}
	
	private EventXML buildFieldStartEvent(String[] log, IntegerWrapper i) {
		FieldStartEventXML event = new FieldStartEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String effect = st.nextToken();
		
		event.setEffect(effect);
		
		return event;
	}

	private EventXML buildCureTeamEvent(String[] log, IntegerWrapper i) {
		return new CureTeamEvent();
	}

	private EventXML buildSingleTurnEvent(String[] log, IntegerWrapper i) {
		SingleTurnEventXML event = new SingleTurnEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		String effect = st.nextToken();
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setEffect(effect);
		
		return event;
	}

	private EventXML buildHealEvent(String[] log, IntegerWrapper i) {
		HealEventXML event = new HealEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		String statusString[] = st.nextToken().split(" ");
		String healthString = statusString[0];
		int health;
		if(healthString.endsWith("fnt")){
			health = 0;
		}
		else{
			String[] fraction = healthString.split(Pattern.quote("\\/"));
			health = Math.round(((float)Integer.parseInt(fraction[0]) * 100) / (fraction.length > 1 ? Integer.parseInt(fraction[1]) : 1));
		}
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setHealth(health);
		
		if(statusString.length > 1){
			String conditionString = statusString[1];
			GameUtil.Status condition = GameUtil.getStatusForString(conditionString);
			event.setStatus(condition);
		}
		
		return event;
	}

	private EventXML buildSetHPEvent(String[] log, IntegerWrapper i) {
		SetHPEventXML event = new SetHPEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		
		String p1Pokemon = null;
		String p2Pokemon = null;
		int p1Health = 0;
		int p2Health = 0;
		
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		if(source[0].startsWith("p1")){
			p1Pokemon = source[1];
			String statusString[] = st.nextToken().split(" ");
			String healthString = statusString[0];
			if(healthString.endsWith("fnt")){
				p1Health = 0;
			}
			else{
				String[] fraction = healthString.split(Pattern.quote("\\/"));
				p1Health = (Integer.parseInt(fraction[0]) * 100) / (fraction.length > 1 ? Integer.parseInt(fraction[1]) : 1);
			}
		}
		else{
			p2Pokemon = source[1];
			String statusString[] = st.nextToken().split(" ");
			String healthString = statusString[0];
			if(healthString.endsWith("fnt")){
				p1Health = 0;
			}
			else{
				String[] fraction = healthString.split(Pattern.quote("\\/"));
				p2Health = (Integer.parseInt(fraction[0]) * 100) / (fraction.length > 1 ? Integer.parseInt(fraction[1]) : 1);
			}
		}
		
		sourceString = st.nextToken();
		source = sourceString.split(": ");
		if(source[0].startsWith("p1")){
			p1Pokemon = source[1];
			String statusString[] = st.nextToken().split(" ");
			String healthString = statusString[0];
			if(healthString.endsWith("fnt")){
				p1Health = 0;
			}
			else{
				String[] fraction = healthString.split(Pattern.quote("\\/"));
				p1Health = (Integer.parseInt(fraction[0]) * 100) / (fraction.length > 1 ? Integer.parseInt(fraction[1]) : 1);
			}
		}
		else{
			p2Pokemon = source[1];
			String statusString[] = st.nextToken().split(" ");
			String healthString = statusString[0];
			if(healthString.endsWith("fnt")){
				p1Health = 0;
			}
			else{
				String[] fraction = healthString.split(Pattern.quote("\\/"));
				p2Health = (Integer.parseInt(fraction[0]) * 100) / (fraction.length > 1 ? Integer.parseInt(fraction[1]) : 1);
			}
		}
		
		event.setP1Pokemon(p1Pokemon);
		event.setP2Pokemon(p2Pokemon);
		event.setP1Health(p1Health);
		event.setP2Health(p2Health);
		
		return event;
	}

	private EventXML buildSideEndEvent(String[] log, IntegerWrapper i) {
		SideEndEventXML event = new SideEndEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player side;
		if(source[0].startsWith("p1")){
			side = Player.PLAYER_ONE;
		}
		else{
			side = Player.PLAYER_TWO;
		}
		String effect = st.nextToken();
		
		event.setSide(side);
		event.setEffect(effect);
		
		return event;
	}
	
	private EventXML buildSideStartEvent(String[] log, IntegerWrapper i) {
		SideStartEventXML event = new SideStartEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player side;
		if(source[0].startsWith("p1")){
			side = Player.PLAYER_ONE;
		}
		else{
			side = Player.PLAYER_TWO;
		}
		String effect = st.nextToken();
		
		event.setSide(side);
		event.setEffect(effect);
		
		return event;
	}

	private EventXML buildEndAbilityEvent(String[] log, IntegerWrapper i) {
		EndAbilityEventXML event = new EndAbilityEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		
		return event;
	}

	private EventXML buildItemEvent(String[] log, IntegerWrapper i) {
		ItemEventXML event = new ItemEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		
		String item = st.nextToken();
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setItem(item);
		
		return event;
	}
	
	private EventXML buildFailEvent(String[] log, IntegerWrapper i) {
		FailEventXML event = new FailEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		
		return event;
	}

	private EventXML buildWeatherEvent(String[] log, IntegerWrapper i) {
		WeatherEventXML event = new WeatherEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String weather = st.nextToken();
		event.setWeather(weather);
		
		if(st.hasMoreTokens()){
			String extra = st.nextToken();
			if(extra.equals("[upkeep]")){
				event.setUpkeep(true);
			}
			else if(extra.startsWith("[from]")){
				String from = extra.substring(7);
				event.setFrom(from);
				if(st.hasMoreTokens()){
					String sourceString = st.nextToken();
					String[] source = sourceString.split(": ");
					Player owner;
					if(source[0].startsWith("p1")){
						owner = Player.PLAYER_ONE;
					}
					else{
						owner = Player.PLAYER_TWO;
					}
					String pokemon = source[1];
					
					event.setPokemon(pokemon);
					event.setOwner(owner);
				}
			}
		}
		
		while(log[i.getInt()].equals("") || log[i.getInt()+1].startsWith("|-")){
			i.increment();
			EventXML effect = buildEvent(log, i);
			if(effect != null){
				event.addEffect(effect);
			}
		}
		
		return event;
	}

	private EventXML buildCureStatusEvent(String[] log, IntegerWrapper i) {
		CureStatusEventXML event = new CureStatusEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		String statusString = st.nextToken();
		Status status = GameUtil.getStatusForString(statusString);
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setStatus(status);
		
		return event;
	}

	private EventXML buildEndEvent(String[] log, IntegerWrapper i) {
		EndEventXML event = new EndEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		String end = st.nextToken();
				
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setEnd(end);
		
		return event;
	}

	private EventXML buildStatusEvent(String[] log, IntegerWrapper i) {
		StatusEventXML event = new StatusEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		String statusString = st.nextToken();
		Status status = GameUtil.getStatusForString(statusString);
		
		if(status == null){
			return null;
		}
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setStatus(status);
		
		return event;
	}

	private EventXML buildStartEvent(String[] log, IntegerWrapper i) {
		StartEventXML event = new StartEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		String start = st.nextToken();
		
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setStart(start);
		
		if(st.hasMoreTokens()){
			String from = st.nextToken();
			event.setFrom(from);
		}
		
		return event;
	}

	private EventXML buildNoTargetEvent(String[] log, IntegerWrapper i) {
		return new NoTargetEvent();
	}

	private EventXML buildCritEvent(String[] log, IntegerWrapper i) {
		CritEventXML event = new CritEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		
		return event;
	}

	private EventXML buildEffectivenessEvent(String[] log, IntegerWrapper i) {
		EffectivenessEventXML event = new EffectivenessEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		String effectivenessString = st.nextToken().substring(1);
		Effectiveness effectiveness = GameUtil.getEffectivenessForString(effectivenessString);
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setEffectiveness(effectiveness);
		
		return event;
	}

	private EventXML buildDamageEvent(String[] log, IntegerWrapper i) {
		DamageEventXML event = new DamageEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		String statusString[] = st.nextToken().split(" ");
		String healthString = statusString[0];
		int health;
		if(healthString.endsWith("fnt")){
			health = 0;
		}
		else{
			String[] fraction = healthString.split(Pattern.quote("\\/"));
			health = (Integer.parseInt(fraction[0]) * 100) / (fraction.length > 1 ? Integer.parseInt(fraction[1]) : 1);
		}
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setHealth(health);
		
		if(statusString.length > 1){
			String conditionString = statusString[1];
			GameUtil.Status condition = GameUtil.getStatusForString(conditionString);
			event.setStatus(condition);
		}
		
		while(st.hasMoreTokens()){
			String extra = st.nextToken();
			event.addExtra(extra);
		}
		
		return event;
	}

	private EventXML buildMissEvent(String[] log, IntegerWrapper i) {
		MissEventXML event = new MissEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];	
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		
		return event;
	}

	private EventXML buildPrepareEvent(String[] log, IntegerWrapper i) {
		PrepareEventXML event = new PrepareEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		
		String preparation = st.nextToken();		
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setPreparation(preparation);
		
		return event;
	}

	private EventXML buildAbilityEvent(String[] log, IntegerWrapper i) {
		AbilityEventXML event = new AbilityEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		
		String ability = st.nextToken();		
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setAbility(ability);
		
		while(log[i.getInt()].equals("") || log[i.getInt()+1].startsWith("|-")){
			i.increment();
			EventXML effect = buildEvent(log, i);
			if(effect != null){
				event.addEffect(effect);
			}
		}
		
		return event;
	}

	private EventXML buildActivateEvent(String[] log, IntegerWrapper i) {
		ActivateEventXML event = new ActivateEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		
		String activation = st.nextToken();		
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setActivation(activation);
		
		while(st.hasMoreTokens()){
			String extra = st.nextToken();
			if(!extra.equals("[still]")){
				event.addExtra(extra);
			}
		}
		
		while(log[i.getInt()].equals("") || log[i.getInt()+1].startsWith("|-")){
			i.increment();
			EventXML effect = buildEvent(log, i);
			if(effect != null){
				event.addEffect(effect);
			}
		}
		
		return event;
	}

	private EventXML buildCantEvent(String[] log, IntegerWrapper i) {
		CantEventXML event = new CantEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		
		Object reason = st.nextToken();
		if(GameUtil.getStatusForString((String)reason) != null){
			reason = GameUtil.getStatusForString((String)reason);
		}
		
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setReason(reason);
		
		return event;
	}

	private EventXML buildFaintEvent(String[] log, IntegerWrapper i) {
		FaintEventXML event = new FaintEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		
		return event;
	}

	private EventXML buildMoveEvent(String[] log, IntegerWrapper i) {
		MoveEventXML event = new MoveEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		String move = st.nextToken();
		
		st.nextToken(); //Target
		
		event.setPokemon(pokemon);
		event.setOwner(owner);
		event.setMove(move);
		
		while(st.hasMoreTokens()){
			String extra = st.nextToken();
			if(extra.equals("[miss]")){
				event.setMissed(true);
			}
			else if(extra.equals("[notarget]")){
				event.setNoTarget(true);
			}
			else if(extra.startsWith("[from]")){
				event.setFrom(extra.substring(6));
			}
			else if(!extra.equals("[still]")){
				System.out.println("unrecognized extra: " + extra);
			}
		}
		
		while(log[i.getInt()+1].equals("") || log[i.getInt()+1].startsWith("|-")){
			i.increment();
			EventXML effect = buildEvent(log, i);
			if(effect != null){
				event.addEffect(effect);
			}
		}
		
		return event;
	}
	
	private EventXML buildSwitchEvent(String[] log, IntegerWrapper i) {
		SwitchEventXML event = new SwitchEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		st.nextToken();
		String statusString[] = st.nextToken().split(" ");
		String healthString = statusString[0];
		int health;
		if(healthString.endsWith("fnt")){
			health = 0;
		}
		else{
			String[] fraction = healthString.split(Pattern.quote("\\/"));
			health = (Integer.parseInt(fraction[0]) * 100) / (fraction.length > 1 ? Integer.parseInt(fraction[1]) : 1);
		}
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setHealth(health);
		
		
		return event;
	}
	
	private EventXML buildBoostEvent(String[] log, IntegerWrapper i) {
		BoostEventXML event = new BoostEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		String statString = st.nextToken();
		GameUtil.Stat stat = GameUtil.getStatForString(statString);
		int levels = Integer.parseInt(st.nextToken());
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setStat(stat);
		event.setLevels(levels);
		
		return event;
	}
	
	private EventXML buildUnBoostEvent(String[] log, IntegerWrapper i) {
		BoostEventXML event = new BoostEventXML();
		
		StringTokenizer st = new StringTokenizer(log[i.getInt()], "|");
		st.nextToken();
		String sourceString = st.nextToken();
		String[] source = sourceString.split(": ");
		Player owner;
		if(source[0].startsWith("p1")){
			owner = Player.PLAYER_ONE;
		}
		else{
			owner = Player.PLAYER_TWO;
		}
		String pokemon = source[1];
		String statString = st.nextToken();
		GameUtil.Stat stat = GameUtil.getStatForString(statString);
		int levels = (-1) * Integer.parseInt(st.nextToken());
		
		event.setOwner(owner);
		event.setPokemon(pokemon);
		event.setStat(stat);
		event.setLevels(levels);
		
		return event;
	}

	private void buildWinner(GameXML game, String[] log) {
		StringTokenizer st = new StringTokenizer(log[log.length - 1], "|");
		st.nextToken();
		String winnerName = st.nextToken();
		
		Player winner = game.getPlayer(winnerName);
		game.setWinner(winner);
	}
	
	private String[] readLog(File file) throws IOException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		List<String> logList = new ArrayList<>();
		boolean add = false;
		String line;
		while((line = br.readLine()) != null){
			add = add || line.startsWith("|player");
			if(add){
				logList.add(line);	
			}
			if(line.startsWith("|win")){
				break;
			}
		}
		fr.close();
		String[] log = new String[logList.size()];
		return logList.toArray(log);
	}
	
}
