package builder;

import game.GameXML;

import java.io.File;

public class GameBuilderWorker implements Runnable {

	private GameLoader gameLoader;
	private GameBuilderXML builder;
	private File gameFile;
	private GameXML game;
	
	public GameBuilderWorker(GameLoader gameLoader, GameBuilderXML builder, File gameFile) {
		this.gameLoader = gameLoader;
		this.builder = builder;
		this.gameFile = gameFile;
	}

	@Override
	public void run() {
		game = builder.buildGame(gameFile);
		gameLoader.returnGameBuilder(builder);
	}
	
	public GameXML getResult(){
		return game;
	}

	public File getGameFile() {
		return gameFile;
	}

}
