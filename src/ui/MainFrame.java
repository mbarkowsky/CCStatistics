package ui;

import java.awt.Container;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 6184125870247422351L;

	public MainFrame(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initializeUI();
		
		setVisible(true);
	}
	
	private void initializeUI() {
		setTitle("CCStatistics");
		setSize(500, 500);
		
		Container cp = getContentPane();
		
		MainPanel selectionPanel = new MainPanel();
		cp.add(selectionPanel);
	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
