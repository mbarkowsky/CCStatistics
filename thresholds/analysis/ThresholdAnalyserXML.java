package analysis;

import game.GameXML;

import java.util.Collection;

import threshold.Threshold;

public interface ThresholdAnalyserXML {

	public Threshold createThreshold(Collection<GameXML> samples);
	
}
