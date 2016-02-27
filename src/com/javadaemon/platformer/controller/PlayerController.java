package com.javadaemon.platformer.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.javadaemon.platformer.model.WALKING_STATE;
import com.javadaemon.platformer.model.actor.Actor;

public class PlayerController extends InputAdapter {
	
	private Actor actor;
	
	private boolean right, left;
	
	public PlayerController(Actor actor) {
		this.actor = actor;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.RIGHT) {
			right = true;
			actor.walk(WALKING_STATE.RIGHT);
		}
		if (keycode == Input.Keys.LEFT) {
			left = true;
			actor.walk(WALKING_STATE.LEFT);
		}
		if (keycode == Input.Keys.UP) {
			actor.jump();
		}
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.RIGHT) {
			right = false;
			if (left) {
				actor.walk(WALKING_STATE.LEFT);
			} else {
				actor.walk(WALKING_STATE.NONE);
			}
		}
		if (keycode == Input.Keys.LEFT) {
			left = false;
			if (right) {
				actor.walk(WALKING_STATE.RIGHT);
			} else {
				actor.walk(WALKING_STATE.NONE);
			}
		} 
		return false;
	}
}
