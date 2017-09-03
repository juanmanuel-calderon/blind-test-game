package com.jmc.blindtest;

import java.util.List;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jmc.blindtest.core.SimplifiedSong;
import com.jmc.blindtest.core.Utils;
import com.jmc.blindtest.game.Game;

public class Main {
	
	public static void main(String args[]) {
		LogManager.getLogManager().reset();
		if(args.length == 0) {
			System.err.println("Usage: java -jar blindtest.jar <FILENAME>");
			System.exit(1);
		}
		String filename = args[0];
		String content = Utils.getFileContent(filename);
		
		List<SimplifiedSong> songs = 
		Stream.of(content.split(System.lineSeparator()))
			  .map(SimplifiedSong::ofFile)
			  .filter(Optional::isPresent)
			  .map(Optional::get)
			  .collect(Collectors.toList());
		
		new Game().startGame(songs);
	}

}
