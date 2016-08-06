package analysis;

import game.GameXML;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import ui.MainFrame;

public abstract class AnalyserXML {

	protected JCheckBox checkBox;
	
	public AnalyserXML(){
		checkBox = new JCheckBox();
		checkBox.setText(getName());
		checkBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				checkBoxToggled();
			}
			
		});
	}
	
	protected void checkBoxToggled(){
		
	}

	public abstract String getName();
	
	public JComponent analyse(Collection<GameXML> games){
		return analyse(games, "");
	}
	
	public JComponent analyse(Collection<GameXML> games, String playerName){
		if(checkBox.isSelected()){
			long t1 = System.currentTimeMillis();
			JComponent result = doAnalyse(games, playerName);
			long t2 = System.currentTimeMillis();
			MainFrame.debugPrint(getName() + " analysis took " + (t2 - t1) + " milliseconds");
			result.setName(getName());
			return result;
		}
		else{
			return null;
		}
	}
	
	protected abstract JComponent doAnalyse(Collection<GameXML> games, String playerName);
	
	public JComponent getUI(){
		return checkBox;
	}
	
}
