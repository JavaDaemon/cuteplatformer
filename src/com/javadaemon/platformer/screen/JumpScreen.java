package com.javadaemon.platformer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.javadaemon.platformer.PlatformerGame;
import com.javadaemon.platformer.Settings;
import com.javadaemon.platformer.controller.CameraController;
import com.javadaemon.platformer.controller.PlayerController;
import com.javadaemon.platformer.model.BrickLevel;
import com.javadaemon.platformer.model.Camera;
import com.javadaemon.platformer.model.World;
import com.javadaemon.platformer.model.actor.AIActor;
import com.javadaemon.platformer.model.actor.Actor;
import com.javadaemon.platformer.model.actor.Player;

public class JumpScreen extends AbstractScreen {
	
	private BitmapFont font = new BitmapFont();
	
	private BrickLevel level;
	private World world;
	private Camera camera = new Camera();
	private Player actor;
	
	private TextureRegion block;
	private TextureRegion grass;
	
	private SpriteBatch batch = new SpriteBatch();
	
	private CameraController cameraController;
	
	private PlayerController playerController;
	private InputMultiplexer inputMultiplexer;
	
	public JumpScreen(PlatformerGame app) {
		super(app);
		TextureAtlas atlas = app.getAssetManager().get("res/textures.atlas", TextureAtlas.class);
		block = atlas.findRegion("grassCenter");
		grass = atlas.findRegion("grassMid");
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render(float delta) {
		/* No delta spike when moving window causing quantum tunnelling */
		if (delta > 0.1f) {
			delta = 0.1f;
		}
		
		world.update(delta);
		cameraController.update(delta);
		
		batch.begin();
		
		for (int x = 0; x < level.getLength(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				if (level.isBrick(x, y)) {
					if (!level.isBrick(x, y+1)) {
						batch.draw(
								grass, 
								x*Settings.PIXELS_PER_BLOCK_SCALED-(int)(camera.getOffset()*Settings.PIXELS_PER_BLOCK_SCALED), 
								y*Settings.PIXELS_PER_BLOCK_SCALED,
								Settings.PIXELS_PER_BLOCK*Settings.SCREEN_SCALE,
								Settings.PIXELS_PER_BLOCK*Settings.SCREEN_SCALE);
					} else {
						batch.draw(
								block, 
								x*Settings.PIXELS_PER_BLOCK_SCALED-(int)(camera.getOffset()*Settings.PIXELS_PER_BLOCK_SCALED), 
								y*Settings.PIXELS_PER_BLOCK_SCALED,
								Settings.PIXELS_PER_BLOCK*Settings.SCREEN_SCALE,
								Settings.PIXELS_PER_BLOCK*Settings.SCREEN_SCALE);
					}
				}
			}
		}
		
		for (Actor a : world.getActors()) {
			batch.draw(
					a.getTexture(), 
					a.getPosition().x*Settings.PIXELS_PER_BLOCK_SCALED-(int)(camera.getOffset()*Settings.PIXELS_PER_BLOCK_SCALED), 
					a.getPosition().y*Settings.PIXELS_PER_BLOCK_SCALED, 
					Settings.PIXELS_PLAYER_WIDTH_SCALED, 
					Settings.PIXELS_PLAYER_HEIGHT_SCALED);
		}
		
		Vector2 position = actor.getPosition();
		font.draw(batch, "position.x: "+position.x, 10, 40);
		font.draw(batch, "position.y: "+position.y, 10, 20);
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	/**
	 * When JumpScreen is instantiated, it is not safe for use before this method has been called.
	 * @param level
	 * @param startX
	 * @param startY
	 */
	public void setLevel(BrickLevel level, int startX, int startY) {
		this.level = level;
		this.world = new World(level);
		this.actor = new Player(startX, startY, world, this.getApp().getAssetManager(), this.getApp().getSoundService());
		this.world.addActor(actor);
		
		/* Add a dummy AI to the world */
		AIActor dummy = new AIActor(15, 5, world, this.getApp().getAssetManager(), this.getApp().getSoundService());
		this.world.addActor(dummy);
		
		cameraController = new CameraController(actor, level, camera);
		
		playerController = new PlayerController(actor);
		inputMultiplexer = new InputMultiplexer(playerController);
	}
}