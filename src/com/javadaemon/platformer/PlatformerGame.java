package com.javadaemon.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.javadaemon.platformer.loader.LevelLoader;
import com.javadaemon.platformer.model.BrickLevel;
import com.javadaemon.platformer.screen.JumpScreen;
import com.javadaemon.platformer.screen.TitleScreen;
import com.javadaemon.platformer.service.SoundService;
import com.javadaemon.platformer.service.SoundService.SOUND;

public class PlatformerGame extends Game {
	
	private AssetManager assetManager;
	private SoundService soundService;

	private JumpScreen jumpScreen;
	private TitleScreen titleScreen;
	
	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.setLoader(BrickLevel.class, new LevelLoader(new InternalFileHandleResolver()));
		assetManager.load("levels/test.level", BrickLevel.class);
		assetManager.load("res/textures.atlas", TextureAtlas.class);
		
		for (int i = 0; i < SOUND.values().length; i++) {
			assetManager.load("res/sound/"+SOUND.values()[i].getFilename(), Sound.class);
		}
		
		assetManager.finishLoading();
		
		soundService = new SoundService(assetManager);
		
		jumpScreen = new JumpScreen(this);
		jumpScreen.setLevel(
				getAssetManager().get("levels/test.level", BrickLevel.class), 
				3, 
				3);
		this.setScreen(jumpScreen);
	}
	
	public void render() {
		Gdx.gl.glClearColor(0.815f, 0.956f, 0.968f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	public SoundService getSoundService() {
		return soundService;
	}
}
