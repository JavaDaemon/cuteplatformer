package com.javadaemon.platformer.model.components;

import com.javadaemon.platformer.service.SoundService;
import com.javadaemon.platformer.service.SoundService.SOUND;

public class SoundComponent {
	
	private SoundService service;
	
	public SoundComponent(SoundService service) {
		this.service = service;
	}
	
	public void jump() {
		service.playSound(SOUND.JUMP);
	}

}
