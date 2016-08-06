package deprecated.analysis;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deprecated.game.Game;
import util.TableLayout;

public class GameStructureAnalyser extends Analyser {

	private class Result{
		
		private int maxTurns;
		private int minTurns;
		private int avgTurns;		
		
		
		private Result(int maxTurns, int minTurns, int avgTurns){
			this.maxTurns = maxTurns;
			this.minTurns = minTurns;
			this.avgTurns = avgTurns;
		}
		
	}
	
	@Override
	public String getName() {
		return "game structure";
	}

	@Override
	protected JComponent doAnalyse(Collection<Game> games, String playerName) {
		Result r = calculateResult(games);
		
		JPanel result = new JPanel(new TableLayout(2));
		
		result.add(new JLabel("maximum turns"));
		result.add(new JLabel(Integer.toString(r.maxTurns)));
		
		result.add(new JLabel("minimum turns"));
		result.add(new JLabel(Integer.toString(r.minTurns)));
		
		result.add(new JLabel("average turns"));
		result.add(new JLabel(Integer.toString(r.avgTurns)));
		return result;
	}

	private Result calculateResult(Collection<Game> games) {
		int totalTurns = 0;
		int gameNumber = 0;
		int max = 0;
		int min = Integer.MAX_VALUE;
		for(Game game:games){
			int turns = game.getTurns().size();
			if(turns > max){
				max = turns;
			}
			if(turns < min){
				min = turns;
			}
			totalTurns += turns;
			gameNumber++;
		}
		return new Result(max, min, totalTurns / gameNumber);
	}

}
