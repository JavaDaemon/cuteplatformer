package com.javadaemon.platformer;

public class Settings {
	
	public static int SCREEN_HEIGHT = 	15;
	public static int SCREEN_WIDTH = 	25;
	public static float SCREEN_SCALE = 	0.5f;

	public static int PIXELS_PER_BLOCK = 70;
	public static int PIXELS_PER_BLOCK_SCALED = (int)(PIXELS_PER_BLOCK * SCREEN_SCALE);
	public static int PIXELS_PLAYER_HEIGHT = 92;
	public static int PIXELS_PLAYER_WIDTH = 66;
	public static int PIXELS_PLAYER_HEIGHT_SCALED = (int)(PIXELS_PLAYER_HEIGHT*SCREEN_SCALE);
	public static int PIXELS_PLAYER_WIDTH_SCALED = (int)(PIXELS_PLAYER_WIDTH*SCREEN_SCALE);
	
	public static float WALK_SPEED = 80f;
	
	public static float PLAYER_WEIGHT = 10f; 				// Kilos
	public static float PLAYER_JUMP_SPEED = 20f;
	public static float GRAVITATIONAL_ACCELERATION = 4.82f; // block/(sec^2)
	public static float FRICTION_COEFFICIENT = 0.6f;
	public static float STATIC_BODY_LIMIT = 0.5f; // When a velocity is low enough to consider static body
	
}
