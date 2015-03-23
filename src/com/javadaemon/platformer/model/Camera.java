package com.javadaemon.platformer.model;

import com.javadaemon.platformer.Settings;

public class Camera {
	
	private float offsetX = 0f;
	
	public float firstVisible() {
		return offsetX;
	}
	
	public float lastVisible() {
		return offsetX+Settings.SCREEN_WIDTH;
	}
	
	public void moveCamera(float delta) {
		offsetX += delta;
	}
	
	public float getOffset() {
		return offsetX;
	}

}
