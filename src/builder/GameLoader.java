package builder;

import game.GameXML;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

public class GameLoader {
	
	private Semaphore builderSemaphore;
	private Vector<GameBuilderXML> idleBuilders;
	private Map<File, GameXML> games;
	
	public GameLoader(int builderNumber){
		games = new HashMap<>();
		initializeBuilders(builderNumber);
	}
	
	private void initializeBuilders(int builderNumber) {
		idleBuilders = new Vector<>();
		for(int i = 0; i < builderNumber; i++){
			idleBuilders.add(new GameBuilderXML());
		}
		builderSemaphore = new Semaphore(builderNumber);
	}
	
	private GameBuilderXML getIdleGameBuilder() throws InterruptedException {
		builderSemaphore.acquire();
		return idleBuilders.remove(0);
	}
	
	public void returnGameBuilder(GameBuilderXML builder){
		idleBuilders.add(builder);
		builderSemaphore.release();
	}
	
	public Map<File, GameXML> loadGames(String gameDirectoryPath){
		File gameDirectory = new File(gameDirectoryPath);
		File[] gameFiles = gameDirectory.listFiles();

		Map<Thread, GameBuilderWorker> workerThreads = startBuilders(gameFiles);
		joinBuilders(workerThreads);
		return games;
	}
	
	private Map<Thread, GameBuilderWorker> startBuilders(File[] gameFiles) {
		Map<Thread, GameBuilderWorker> workerThreads = new HashMap<>();
		for(File gameFile:gameFiles){
			if(!games.containsKey(gameFile)){
				try {
					GameBuilderXML builder = getIdleGameBuilder();
					GameBuilderWorker builderWorker = new GameBuilderWorker(this, builder, gameFile);
					Thread worker = new Thread(builderWorker);
					worker.start();
					workerThreads.put(worker, builderWorker);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return workerThreads;
	}

	private void joinBuilders(Map<Thread, GameBuilderWorker> workerThreads) {
		for(Entry<Thread, GameBuilderWorker> entry:workerThreads.entrySet()){
			try {
				entry.getKey().join();
				GameBuilderWorker builderWorker = entry.getValue();
				games.put(builderWorker.getGameFile(), builderWorker.getResult());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<File, GameXML> getGames() {
		return games;
	}
}
