package analysis;

import game.Game;

import java.util.Collection;

import threshold.Threshold;

public interface ThresholdAnalyser {

	public Threshold createThreshold(Collection<Game> samples);
	
}
