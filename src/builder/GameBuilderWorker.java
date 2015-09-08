package builder;

import game.Game;

import java.io.File;
import java.io.IOException;

public class GameBuilderWorker implements Runnable {

	private GameLoader gameLoader;
	private GameBuilder builder;
	private File gameFile;
	private Game game;
	
	public GameBuilderWorker(GameLoader gameLoader, GameBuilder builder, File gameFile) {
		this.gameLoader = gameLoader;
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
			gameLoader.returnGameBuilder(builder);
		}
	}
	
	public Game getResult(){
		return game;
	}

	public File getGameFile() {
		return gameFile;
	}

}
