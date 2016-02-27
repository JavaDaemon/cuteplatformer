package com.javadaemon.platformer.model.components;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javadaemon.platformer.Settings;
import com.javadaemon.platformer.model.JUMPING_STATE;
import com.javadaemon.platformer.model.World;
import com.javadaemon.platformer.model.actor.Actor;

public class PhysicsComponent {
	
	private Actor actor;
	private World world;
	
	private Rectangle boundingBox;
	private Vector2 velocity;
	
	private Vector2 acceleration = new Vector2(0f, 0f);
	private Vector2 gravity;
	
	public PhysicsComponent(Actor actor, World world, Rectangle boundingBox, Vector2 velocity, float mass) {
		this.actor = actor;
		this.world = world;
		this.boundingBox = boundingBox;
		this.velocity = velocity;
		this.gravity = new Vector2(0f, -Settings.GRAVITATIONAL_ACCELERATION * mass);
	}
	
	public void update(float delta) {
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
		boundingBox.x += velocityStep.x;
		boundingBox.y += velocityStep.y;
		
		if (velocity.y > 0) { 		// Moving upwards
			if (boundingBox.y != resolveJumping(boundingBox.x, boundingBox.y)) {
				boundingBox.y = resolveJumping(boundingBox.x, boundingBox.y);
				velocity.y = 0f;
			}
		} else if (velocity.y < 0) {	// Moving downwards
			actor.setJumpingState(JUMPING_STATE.FALLING);
			if (boundingBox.y != resolveFalling(boundingBox.x, boundingBox.y)) {
				boundingBox.y = resolveFalling(boundingBox.x, boundingBox.y);
				actor.setJumpingState(JUMPING_STATE.STANDING);
				velocity.y = 0f;
				if (boundingBox.x != resolveForward(boundingBox.x, boundingBox.y)
						|| boundingBox.x != resolveBackwards(boundingBox.x, boundingBox.y)) {
					/* This is to ensure that the player will not stand halfway through a block */
					boundingBox.x = resolveForward(boundingBox.x, boundingBox.y);
					boundingBox.x = resolveBackwards(boundingBox.x, boundingBox.y);
					velocity.x = 0f;
				}
			}
		}
		
		if (velocity.x > 0) {		// Moving forwards
			if (boundingBox.x != resolveForward(boundingBox.x, boundingBox.y)) {
				boundingBox.x = resolveForward(boundingBox.x, boundingBox.y);
				velocity.x = 0f;
			}
		} else if (	velocity.x < 0) {	// Moving backwards
			if (boundingBox.x != resolveBackwards(boundingBox.x, boundingBox.y)) {
				boundingBox.x = resolveBackwards(boundingBox.x, boundingBox.y);
				velocity.x = 0f;
			}
		}
		checkActorCollision();
	}
	
	private void checkActorCollision() {
		for (Actor a : world.getActors()) {
			if (a == actor) {
				continue;
			}
			if (a.getBounds().overlaps(actor.getBounds())) {
				actor.collides(a);
			}
		}
	}
	
	private float resolveFalling(float wantedX, float wantedY) {
		if (world.getLevel().insideBrick(			wantedX+boundingBox.width*1/4, 	wantedY)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width*1/2,	wantedY)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width*3/4, 	wantedY)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width*1/10, 	wantedY)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width*9/10, 	wantedY)) {
			return MathUtils.ceil(wantedY);
		}
		return wantedY;
	}
	
	private float resolveJumping(float wantedX, float wantedY) {
		if (world.getLevel().insideBrick(			wantedX+boundingBox.width*1/4, 	wantedY+boundingBox.height)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width*1/2, 	wantedY+boundingBox.height)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width*3/4, 	wantedY+boundingBox.height)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width*1/10, 	wantedY+boundingBox.height)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width*9/10,	wantedY+boundingBox.height)) {
			return MathUtils.floor(wantedY+boundingBox.height)-boundingBox.height;
		}
		return wantedY;
	}
	
	private float resolveForward(float wantedX, float wantedY) {
		if (world.getLevel().insideBrick(			wantedX+boundingBox.width, 		wantedY+boundingBox.height*1/4)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width, 		wantedY+boundingBox.height*1/2)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width, 		wantedY+boundingBox.height*3/4)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width, 		wantedY+boundingBox.height*1/6)
				|| world.getLevel().insideBrick(	wantedX+boundingBox.width, 		wantedY+boundingBox.height*5/6)) {
			return MathUtils.floor(wantedX+boundingBox.width)-boundingBox.width;
		}
		return wantedX;
	}
	
	private float resolveBackwards(float wantedX, float wantedY) {
		if (world.getLevel().insideBrick(			wantedX, 					wantedY+boundingBox.height*1/4)
				|| world.getLevel().insideBrick(	wantedX, 					wantedY+boundingBox.height*1/2)
				|| world.getLevel().insideBrick(	wantedX, 					wantedY+boundingBox.height*3/4)
				|| world.getLevel().insideBrick(	wantedX, 					wantedY+boundingBox.height*1/6)
				|| world.getLevel().insideBrick(	wantedX, 					wantedY+boundingBox.height*5/6)) {
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
