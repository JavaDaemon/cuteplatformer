package com.javadaemon.platformer.model.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.javadaemon.platformer.model.World;
import com.javadaemon.platformer.service.SoundService;

public abstract class AIActor extends Actor {

	public AIActor(int x, int y, World world, AssetManager assetManager,
			SoundService soundService) {
		super(x, y, world, assetManager, soundService);
	}
	
	public void update(float delta) {
		this.update(delta);
	}

}
