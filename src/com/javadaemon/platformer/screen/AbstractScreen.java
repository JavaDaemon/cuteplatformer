package com.javadaemon.platformer.screen;

import com.badlogic.gdx.Screen;
import com.javadaemon.platformer.PlatformerGame;

public abstract class AbstractScreen implements Screen {
	
	private PlatformerGame app;
	
	public AbstractScreen(PlatformerGame app) {
		this.app = app;
	}
	
	protected PlatformerGame getApp() {
		return app;
	}

}
