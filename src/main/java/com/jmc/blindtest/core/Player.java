package com.jmc.blindtest.core;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class Player {
	
	private AudioMediaPlayerComponent mediaPlayer;
	
	public Player() {
		new NativeDiscovery().discover();
		mediaPlayer = new AudioMediaPlayerComponent();
	}
	
	public void play(String filename) {
		mediaPlayer.getMediaPlayer().playMedia(filename);
	}
	
	public void stop() {
		mediaPlayer.getMediaPlayer().stop();
	}
	
	public void destroy() {
		mediaPlayer.release();
	}

}
