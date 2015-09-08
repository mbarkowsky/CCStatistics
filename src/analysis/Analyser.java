package analysis;

import game.Game;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JCheckBox;

public abstract class Analyser{
	
	protected JCheckBox checkBox;
	
	public Analyser(){
		checkBox = new JCheckBox();
		checkBox.setText(getName());
	}
	
	public abstract String getName();
	
	public JComponent analyse(Collection<Game> games){
		if(checkBox.isSelected()){
			return doAnalyse(games);
		}
		else{
			return null;
		}
	}
	
	protected abstract JComponent doAnalyse(Collection<Game> games);
	
	public JComponent getUI(){
		return checkBox;
	}
}
