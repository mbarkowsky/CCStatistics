package builder;

import game.Game;

import java.io.File;
import java.io.IOException;

import ui.MainPanel;

public class GameBuilderWorker implements Runnable {

	private MainPanel mainPanel;
	private GameBuilder builder;
	private File gameFile;
	private Game game;
	
	public GameBuilderWorker(MainPanel mainPanel, GameBuilder builder, File gameFile) {
		this.mainPanel = mainPanel;
		this.builder = builder;
		this.gameFile = gameFile;
	}

	@Override
	public void run() {
		try {
			game = builder.buildGame(gameFile);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			mainPanel.returnGameBuilder(builder);
		}
	}
	
	public Game getResult(){
		return game;
	}

	public File getGameFile() {
		return gameFile;
	}

}
