package com.jmc.blindtest.game;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.jmc.blindtest.core.Player;
import com.jmc.blindtest.core.SimplifiedSong;

public class Game {
	
	private static final int TRIES = 3;
	private static final int CORRECT_SCORE = 100;
	private static final int SECONDS = 10;
	private Player player;
	Scanner reader = new Scanner(System.in);
	
	public void startGame(List<SimplifiedSong> songs) {
		player = new Player();
		Collections.shuffle(songs);
		int finalScore = 
		songs.stream().mapToInt(this::testOne).sum();
		
		System.out.println("Final score = " + finalScore);
		
		player.destroy();
		reader.close();
	}
	
	private int testOne(SimplifiedSong song) {
		System.out.println();
		System.out.println();
		int score = CORRECT_SCORE;
		int tries = 0;
		boolean[] currentAnswer = initAnswer(song.title.toCharArray());
		char[] answer = song.title.toLowerCase().toCharArray();
		boolean isCorrect = false;
		boolean hintUsed = false;
		
		System.out.println("You will hear the first " + SECONDS + " seconds of the song.");
		play(song);
		
		do {
			
			System.out.print("Current answer is   ");
			printCurrent(currentAnswer, answer);
			System.out.println("You have " + (TRIES - tries) + " tries");
			System.out.println("Type your answer below, '/hint' to see a hint and '/play' to play the song again");
			System.out.println("if you want to quit, type '/quit'");
			if(!hintUsed) System.out.println("The hint costs " + score/2 + " points");
			String typed = reader.nextLine().toLowerCase();
						
			if(typed.equals("/hint")) {
				System.out.println("The album artist is " + song.albumArtist);
				if(!hintUsed) score = score/2;
			} else if(typed.equals("/play")) {
				play(song);
			} else if(typed.equals("/quit")) {
				tries = 4;
			} else {
				char[] attempt = typed.toCharArray();
				checkAns(currentAnswer, answer, attempt);
				tries++;
				isCorrect = isCorrect(currentAnswer);
				if(!isCorrect) System.out.println("Wrong");
			}
			
		} while(!isCorrect && tries < TRIES);
		
		if(!isCorrect) {
			System.out.println("The answer was " + song.title);
			System.out.println("You get 0 points");
			score = 0;
		} else {
			System.out.println("Bravo!");
			System.out.println("You get " + score + " points");
		}
		return score;
	}
	
	private boolean[] initAnswer(char[] str) {
		boolean[] ans = new boolean[str.length];
		for(int i = 0; i < str.length; i++) {
			ans[i] = true;
			if(Character.isLetter(str[i]) || Character.isDigit(str[i])) {
				ans[i] = false;
			}
		}
		
		return ans;
	}
	
	private boolean isCorrect(boolean[] ans) {
		for(boolean a : ans) {
			if(!a) return false;
		}
		return true;
	}
	
	private void checkAns(boolean ans[], char[] correct, char[] input) {
		int len = Math.min(correct.length, input.length);
		for(int i = 0; i < len; i++) {
			if(input[i] == correct[i]) ans[i] = true;
		}
	}
	
	private void printCurrent(boolean[] ans, char[] correct) {
		for(int i = 0; i < correct.length; i++) {
			if(ans[i]) System.out.print(correct[i] + " ");
			else System.out.print("_ ");
		}
		System.out.println();
	}
	
	private void play(SimplifiedSong song) {
		player.play(song.filename);
		try {
			TimeUnit.SECONDS.sleep(SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			player.stop();
		}
	}

}
