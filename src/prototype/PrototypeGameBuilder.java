package prototype;

import game.Game;
import game.Game.Player;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import builder.GameBuilder;

public class PrototypeGameBuilder extends GameBuilder{

	@Override
	public Game buildGame(File file){
		String content = readContent(file);
		
		Game game = new Game();
		StringTokenizer tokenizer = new StringTokenizer(content, " ");
		game.setPlayerName(Player.PLAYER_ONE, tokenizer.nextToken());
		game.setPlayerName(Player.PLAYER_TWO, tokenizer.nextToken());
		game.setWinner(Player.PLAYER_ONE);
		
		return game;
	}
	
	private String readContent(File file){
		String content = "";
		
		try {
			FileReader reader = new FileReader(file);
			content = "";
			int letter;
			while((letter = reader.read()) != -1){
				content = content + (char)letter;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
}
