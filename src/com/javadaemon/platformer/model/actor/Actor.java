package com.javadaemon.platformer.model.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.javadaemon.platformer.Settings;
import com.javadaemon.platformer.model.JUMPING_STATE;
import com.javadaemon.platformer.model.WALKING_STATE;
import com.javadaemon.platformer.model.World;
import com.javadaemon.platformer.model.components.PhysicsComponent;
import com.javadaemon.platformer.model.components.RenderingComponent;
import com.javadaemon.platformer.model.components.SoundComponent;
import com.javadaemon.platformer.service.SoundService;

public abstract class Actor {
	
	private World world;
	
	private Vector2 position;
	private Vector2 velocity  = new Vector2(0f, 0f);
	private WALKING_STATE walkingState = WALKING_STATE.NONE;
	private JUMPING_STATE jumpingState = JUMPING_STATE.STANDING;
	
	private PhysicsComponent physics;
	private RenderingComponent rendering;
	private SoundComponent sound;
	
	public Actor(int x, int y, World world, AssetManager assetManager, SoundService soundService) {
		position = new Vector2(x, y);
		this.world = world;
		physics = new PhysicsComponent(this, world, new Float(Settings.PLAYER_WEIGHT), new Vector2(1f, 1.4f));
		rendering = new RenderingComponent(this, assetManager);
		sound = new SoundComponent(soundService);
	}
	
	public void update(float delta) {
		physics.update(delta);
		rendering.update(delta);
	}
	
	public void jump() {
		if (jumpingState == JUMPING_STATE.STANDING) {
			physics.jump();
			sound.jump();
		}
	}
	
	public void walk(WALKING_STATE newState) {
		this.walkingState = newState;
	}
	
	public WALKING_STATE getWalkingState() {
		return walkingState;
	}
	
	public void setJumpingState(JUMPING_STATE newState) {
		this.jumpingState = newState;
	}
	
	public JUMPING_STATE getJumpingState() {
		return jumpingState;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public TextureRegion getTexture() {
		return rendering.getTexture();
	}
}
