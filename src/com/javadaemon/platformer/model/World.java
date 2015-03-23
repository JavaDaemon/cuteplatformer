package com.javadaemon.platformer.model;

public class World {
	
	private BrickLevel level;
	
	public World(BrickLevel level) {
		this.level = level;
	}
	
	public BrickLevel getLevel() {
		return level;
	}
}
