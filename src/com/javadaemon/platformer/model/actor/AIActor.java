package com.javadaemon.platformer.model.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.javadaemon.platformer.model.WALKING_STATE;
import com.javadaemon.platformer.model.World;
import com.javadaemon.platformer.service.SoundService;

/**
 * An actor controlled by the CPU. This is controlled primarily using actor#jump() and actor#walk(WALKING_STATE).
 * 
 * @author Mads Peter Horndrup <madshorndrup@gmail.com>
 */
public class AIActor extends Actor {
	
	private final float minimumDecisionTime = 0.1f;
	private final float maximumDecisonTime = 0.5f;
	private float untilDecision = 0f;

	public AIActor(int x, int y, World world, AssetManager assetManager, SoundService soundService) {
		super(x, y, world, assetManager, soundService);
	}
	
	public void update(float delta) {
		untilDecision -= delta;
		if (untilDecision < 0f) {
			int decision = MathUtils.random(3);
			switch (decision) {
				case 0:
					this.jump();
					break;
				case 1:
					this.walk(WALKING_STATE.NONE);
					break;
				case 2:
					this.walk(WALKING_STATE.RIGHT);
					break;
				case 3:
					this.walk(WALKING_STATE.LEFT);
					break;
			}
			untilDecision = MathUtils.random(minimumDecisionTime, maximumDecisonTime);
		}
		
		super.update(delta);
	}
}
