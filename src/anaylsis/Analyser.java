package anaylsis;

import game.Game;

import java.util.List;

import javax.swing.JPanel;

public abstract class Analyser{

	public abstract String getName();
	
	public abstract JPanel analyse(List<Game> games);
	
}
