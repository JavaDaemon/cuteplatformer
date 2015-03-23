package com.javadaemon.platformer;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Platformer";
		config.width = (int)(Settings.SCREEN_WIDTH*Settings.SCREEN_SCALE*Settings.PIXELS_PER_BLOCK);
		config.height = (int)(Settings.SCREEN_HEIGHT*Settings.SCREEN_SCALE*Settings.PIXELS_PER_BLOCK);
		config.vSyncEnabled = true;
		
		new LwjglApplication(new PlatformerGame(), config);
	}
}
