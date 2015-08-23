package anaylsis;

import game.Game;

import java.util.Collection;

import javax.swing.JComponent;

public interface Analyser{

	public abstract String getName();
	
	public abstract JComponent analyse(Collection<Game> games);
	
}
