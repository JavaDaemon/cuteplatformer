package com.javadaemon.platformer.model;

public class BrickLevel {
	
	private boolean[][] bricks;
	
	public BrickLevel(int length, int height) {
		bricks = new boolean[length][height];
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < height; y++) {
				if (y < 3) {
					bricks[x][y] = true;
				} else if (x == 6) {
					bricks[x][y] = true;
				} else if (x == 0) {
					bricks[x][y] = true;
				} else {
					bricks[x][y] = false;
				}
			}
		}
	}
	
	public int getHeight() {
		return bricks[0].length;
	}
	
	public int getLength() {
		return bricks.length;
	}
	
	public boolean outofBounds(float x, float y) {
		if (x < 0 || x >= getLength()) {
			return true;
		}
		if (y < 0 || y >= getHeight()) {
			return true;
		}
		return false;
	}
	
	public boolean isBrick(int x, int y) {
		if (outofBounds(x, y)) {
			return false;
		}
		return bricks[x][y];
	}
	
	public boolean insideBrick(float x, float y) {
		if (outofBounds(x, y)) {
			return true;
		}
		int arrayx = (int)x;
		int arrayy = (int)y;
		return isBrick(arrayx, arrayy);
	}
	
	public void insertBrick(int x, int y) {
		bricks[x][y] = true;
	}
	
	public void insertSpace(int x, int y) {
		bricks[x][y] = false;
	}
}
