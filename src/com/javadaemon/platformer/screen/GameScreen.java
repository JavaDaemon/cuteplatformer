package com.javadaemon.platformer.screen;

import com.badlogic.gdx.Screen;
import com.javadaemon.platformer.PlatformerGame;

public abstract class GameScreen implements Screen {
	
	private PlatformerGame app;
	
	public GameScreen(PlatformerGame app) {
		this.app = app;
	}
	
	protected PlatformerGame getApp() {
		return app;
	}

}
