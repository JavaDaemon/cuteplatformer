package com.javadaemon.platformer.model.components;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.javadaemon.platformer.model.JUMPING_STATE;
import com.javadaemon.platformer.model.WALKING_STATE;
import com.javadaemon.platformer.model.actor.Actor;

public class RenderingComponent {
	
	private Actor actor;
	
	private STATE animState;
	private DIRECTION facing;
	
	private float animTime;
	
	private Animation walking;
	private TextureRegion standing;
	private TextureRegion jumping;
	
	public RenderingComponent(Actor actor, AssetManager assetManager) {
		this.actor = actor;
		animTime = 0f;
		animState = STATE.STANDING;
		facing = DIRECTION.RIGHT;
		
		TextureAtlas atlas = assetManager.get("res/textures.atlas", TextureAtlas.class);
		
		walking = new Animation(1/40f, atlas.findRegions("p1_walk"), Animation.PlayMode.LOOP);
		standing = atlas.findRegion("p1_stand");
		jumping = atlas.findRegion("p1_jump");
	}
	
	public void update(float delta) {
		STATE newState = STATE.STANDING;
		if (actor.getWalkingState() == WALKING_STATE.RIGHT || actor.getWalkingState() == WALKING_STATE.LEFT) {
			newState = STATE.WALKING;
		}
		if (actor.getJumpingState() != JUMPING_STATE.STANDING) {
			newState = STATE.JUMPING;
		}
		setState(newState);
		
		switch (actor.getWalkingState()) {
		case LEFT:
			facing = DIRECTION.LEFT;
			break;
		case RIGHT:
			facing = DIRECTION.RIGHT;
			break;
		case NONE:
			break;
		}
		
		animTime += delta;
	}
	
	/**
	 * NOTE: This also resets animation, if the given state is different than the current one
	 */
	private void setState(STATE newState) {
		if (animState != newState) {
			animState = newState;
			animTime = 0f;
		}
	}
	
	public TextureRegion getTexture() {
		
		TextureRegion texture;
		
		switch (animState) {
		case STANDING:
			texture = standing;
			break;
		case WALKING:
			texture = walking.getKeyFrame(animTime);
			break;
		case JUMPING:
		case FALLING:
			texture = jumping;
			break;
		default:
			texture = standing;
			break;
		}
		
		if (facing == DIRECTION.LEFT && !texture.isFlipX()) {
			texture.flip(true, false);
		}
		if (facing == DIRECTION.RIGHT && texture.isFlipX()) {
			texture.flip(true, false);
		}
		
		return texture;
	}
	
	private enum STATE {
		STANDING, WALKING, JUMPING, FALLING;
	}
	
	public enum DIRECTION {
		RIGHT, LEFT;
	}
}
