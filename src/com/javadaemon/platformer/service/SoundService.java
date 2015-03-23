package com.javadaemon.platformer.service;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundService {
	
	private AssetManager assetManager;
	private HashMap<SOUND, Sound> soundMap = new HashMap<SOUND, Sound>();
	
	public SoundService(AssetManager assetManager) {
		this.assetManager = assetManager;
		
		for (int i = 0; i < SOUND.values().length; i++) {
			soundMap.put(SOUND.values()[i], assetManager.get("res/sound/"+SOUND.values()[i].filename, Sound.class));
		}
	}
	
	public void playSound(SOUND sound) {
		soundMap.get(sound).play();
	}
	
	public enum SOUND {
		JUMP("jump3.wav"),
		;
		
		String filename;
		
		private SOUND(String filename) {
			this.filename = filename;
		}
		
		public String getFilename() {
			return filename;
		}
	}

}
