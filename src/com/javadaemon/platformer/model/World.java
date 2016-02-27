package com.javadaemon.platformer.model;

import java.util.ArrayList;
import java.util.List;

import com.javadaemon.platformer.model.actor.Actor;

/**
 * Data model for the entire game world. This is objective, and does therefore not distinguish between the player and other Actors.
 * 
 * @author Mads Peter Horndurp <madshorndrup@gmail.com>
 */
public class World {
	
	private BrickLevel level;
	private ArrayList<Actor> actors;
	
	public World(BrickLevel level) {
		this.level = level;
		this.actors = new ArrayList<Actor>();
	}
	
	public void update(float delta) {
		for (Actor a : actors) {
			a.update(delta);
		}
	}
	
	public void addActor(Actor actor) {
		actors.add(actor);
	}
	
	public List<Actor> getActors() {
		return actors;
	}
	
	public BrickLevel getLevel() {
		return level;
	}
}
