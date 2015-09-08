package ui;

import java.awt.Container;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public static String imagePath = "resource/images/";
	public static String thresholdPath = "resource/thresholds/";
	
	private static boolean debugPrint = false;
	
	public static void debugPrint(String message){
		if(debugPrint){
			System.out.println(message);
		}
	}
	
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
		if(args.length > 0){
			debugPrint = Boolean.parseBoolean(args[0]);
		}
		new MainFrame();
	}

}
