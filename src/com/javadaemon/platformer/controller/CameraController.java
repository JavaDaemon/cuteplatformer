package com.javadaemon.platformer.controller;

import com.javadaemon.platformer.Settings;
import com.javadaemon.platformer.model.BrickLevel;
import com.javadaemon.platformer.model.Camera;
import com.javadaemon.platformer.model.actor.Actor;

public class CameraController {
	
	private Actor actor;
	private BrickLevel brickLevel;
	private Camera camera;
	
	public CameraController(Actor actor, BrickLevel level, Camera camera) {
		this.actor = actor;
		this.brickLevel = level;
		this.camera = camera;
	}
	
	public void update(float delta) {
		float actorRelative = actor.getPosition().x - camera.firstVisible();
		float actorScreenRelative = actorRelative/Settings.SCREEN_WIDTH;
		
		/*
		 * NOTE: Følgende tager en procent (actorScreenRelative), og behandler den som et mål i blocks.
		 * Det viser sig at dette giver super flot cameraføring, som er uafhængig af hastighed.
		 * Vi beholder det! 
		 */
		if (actorScreenRelative > 0.60f) {
			float xxx = actorScreenRelative - 0.60f;
			camera.moveCamera(xxx);
		}
		if (actorScreenRelative < 0.40f) {
			float xxx = actorScreenRelative - 0.40f;
			camera.moveCamera(xxx);
		}
		
		if (camera.firstVisible() > (brickLevel.getLength()-Settings.SCREEN_WIDTH)) {
			camera.moveCamera((brickLevel.getLength()-Settings.SCREEN_WIDTH)-camera.firstVisible());
		}
		if (camera.firstVisible() < 0) {
			camera.moveCamera(Math.abs(camera.firstVisible()));
		}
	}

}
