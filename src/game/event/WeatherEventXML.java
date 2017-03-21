package game.event;

import game.GameXML.Player;

import java.util.ArrayList;
import java.util.List;

public class WeatherEventXML extends EventXML implements CompositeEvent{

	private String weather;
	private String from;
	private Player owner;
	private String pokemon;
	private boolean upkeep;
	private List<EventXML> effects;
	
	public WeatherEventXML(){
		upkeep = false;
		effects = new ArrayList<>();
	}
	
	@Override
	public boolean isCompositeEvent() {
		return true;
	}
	
	public String getWeather() {
		return weather;
	}
	
	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public boolean isUpkeep() {
		return upkeep;
	}

	public void setUpkeep(boolean upkeep) {
		this.upkeep = upkeep;
	}
	
	public String getPokemon() {
		return pokemon;
	}
	
	public void setPokemon(String pokemon) {
		this.pokemon = pokemon;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public List<EventXML> getEffects() {
		return effects;
	}

	public void addEffect(EventXML effect){
		effects.add(effect);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if(isUpkeep()){
			sb.append(weather + " continues");
		}
		else if (from != null && owner != null && pokemon != null){
			sb.append(owner + "'s " + pokemon + " started " + weather);
		}
		else{
			sb.append(weather);
		}
		for(EventXML effect:effects){
			sb.append("\n");
			sb.append("\t" + effect.toString());
		}
		return sb.toString();		
	}
	
}
