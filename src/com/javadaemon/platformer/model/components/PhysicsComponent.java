package com.javadaemon.platformer.model.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.javadaemon.platformer.Settings;
import com.javadaemon.platformer.model.JUMPING_STATE;
import com.javadaemon.platformer.model.World;
import com.javadaemon.platformer.model.actor.Actor;

public class PhysicsComponent {
	
	private World world;
	private Actor actor;
	
	private Vector2 acceleration = new Vector2(0f, 0f);
	
	private float mass;
	private Vector2 dimensions;
	
	private Vector2 gravity;
	
	public PhysicsComponent(Actor actor, World world, float mass, Vector2 dimensions) {
		this.actor = actor;
		this.world = world;
		this.mass = mass;
		this.dimensions = dimensions;
		this.gravity = new Vector2(0f, -Settings.GRAVITATIONAL_ACCELERATION * mass);
	}
	
	public void update(float delta) {
		Vector2 velocity = actor.getVelocity();
		Vector2 position = actor.getPosition();
		
		switch (actor.getWalkingState()) {
			case LEFT:
				acceleration.x = -Settings.WALK_SPEED;
				break;
			case RIGHT:
				acceleration.x = Settings.WALK_SPEED;
				break;
			case NONE:
			default:
				acceleration.x = 0f;
				break;
		}
		
		// Calculate gravity with delta accounted for
		Vector2 gravityStep = gravity.cpy().scl(delta);
		
		// Calculate acceleration with delta accounted for
		Vector2 accelerationStep = acceleration.cpy().scl(delta);
		
		// Add the forces to the velocity
		velocity.add(gravityStep);
		velocity.add(accelerationStep);
		
		// Make sure we do not go too fast
		velocity.x = MathUtils.clamp(velocity.x, -7f, 7f);
		velocity.y = MathUtils.clamp(velocity.y, -20f, 20f);
		
		// If we are moving add friction
		if (velocity.x > Settings.STATIC_BODY_LIMIT || velocity.x < -Settings.STATIC_BODY_LIMIT) {
			Vector2 frictionStep = new Vector2(-velocity.x, 0f).nor().scl(gravity.len()).scl(Settings.FRICTION_COEFFICIENT).scl(delta);
			velocity.add(frictionStep);
		} else {
			velocity.x = 0f;
		}
		
		// Calculate our velocity with delta accounted for
		Vector2 velocityStep = velocity.cpy().scl(delta);
		
		// Update the position
		position.add(velocityStep);
		
		if (velocity.y > 0) { 		// Moving upwards
			if (position.y != resolveJumping(position.x, position.y)) {
				position.y = resolveJumping(position.x, position.y);
				velocity.y = 0f;
			}
		} else if (velocity.y < 0) {	// Moving downwards
			actor.setJumpingState(JUMPING_STATE.FALLING);
			if (position.y != resolveFalling(position.x, position.y)) {
				position.y = resolveFalling(position.x, position.y);
				actor.setJumpingState(JUMPING_STATE.STANDING);
				velocity.y = 0f;
				if (position.x != resolveForward(position.x, position.y)
						|| position.x != resolveBackwards(position.x, position.y)) {
					/* This is to ensure that the player will not stand halfway through a block */
					position.x = resolveForward(position.x, position.y);
					position.x = resolveBackwards(position.x, position.y);
					velocity.x = 0f;
				}
			}
		}
		
		if (velocity.x > 0) {		// Moving forwards
			if (position.x != resolveForward(position.x, position.y)) {
				position.x = resolveForward(position.x, position.y);
				velocity.x = 0f;
			}
		} else if (	velocity.x < 0) {	// Moving backwards
			if (position.x != resolveBackwards(position.x, position.y)) {
				position.x = resolveBackwards(position.x, position.y);
				velocity.x = 0f;
			}
		}
	}
	
	private float resolveFalling(float wantedX, float wantedY) {
		if (world.getLevel().insideBrick(			wantedX+dimensions.x*1/4, 	wantedY)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x*1/2,	wantedY)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x*3/4, 	wantedY)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x*1/10, 	wantedY)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x*9/10, 	wantedY)) {
			return MathUtils.ceil(wantedY);
		}
		return wantedY;
	}
	
	private float resolveJumping(float wantedX, float wantedY) {
		if (world.getLevel().insideBrick(			wantedX+dimensions.x*1/4, 	wantedY+dimensions.y)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x*1/2, 	wantedY+dimensions.y)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x*3/4, 	wantedY+dimensions.y)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x*1/10, 	wantedY+dimensions.y)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x*9/10,	wantedY+dimensions.y)) {
			return MathUtils.floor(wantedY+dimensions.y)-dimensions.y;
		}
		return wantedY;
	}
	
	private float resolveForward(float wantedX, float wantedY) {
		if (world.getLevel().insideBrick(			wantedX+dimensions.x, 		wantedY+dimensions.y*1/4)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x, 		wantedY+dimensions.y*1/2)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x, 		wantedY+dimensions.y*3/4)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x, 		wantedY+dimensions.y*1/6)
				|| world.getLevel().insideBrick(	wantedX+dimensions.x, 		wantedY+dimensions.y*5/6)) {
			return MathUtils.floor(wantedX+dimensions.x)-dimensions.x;
		}
		return wantedX;
	}
	
	private float resolveBackwards(float wantedX, float wantedY) {
		if (world.getLevel().insideBrick(			wantedX, 					wantedY+dimensions.y*1/4)
				|| world.getLevel().insideBrick(	wantedX, 					wantedY+dimensions.y*1/2)
				|| world.getLevel().insideBrick(	wantedX, 					wantedY+dimensions.y*3/4)
				|| world.getLevel().insideBrick(	wantedX, 					wantedY+dimensions.y*1/6)
				|| world.getLevel().insideBrick(	wantedX, 					wantedY+dimensions.y*5/6)) {
			return MathUtils.ceil(wantedX);
		}
		return wantedX;
	}
	
	public Vector2 getAcceleration() {
		return acceleration;
	}
	
	public void jump() {
		actor.getVelocity().y = (Settings.PLAYER_JUMP_SPEED);
		actor.setJumpingState(JUMPING_STATE.JUMPING);
	}
}
