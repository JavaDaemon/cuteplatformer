package com.javadaemon.platformer.loader;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.javadaemon.platformer.model.BrickLevel;

public class LevelLoader extends AsynchronousAssetLoader<BrickLevel, LevelLoader.LevelParameter> {
	
	private int width = 0, height = 0;
	private BrickLevel level;
	
	public LevelLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String path, FileHandle handle,
			LevelParameter parameter) {
		try {
			BufferedReader breader = new BufferedReader(handle.reader("UTF-8"));
			
			width = Integer.parseInt(breader.readLine());
			height = Integer.parseInt(breader.readLine());
			level = new BrickLevel(width, height);
			
			String line;
			int lineNumber = 0;
			while ((line = breader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					if (!Character.isWhitespace(line.charAt(i))) {
						level.insertBrick(i, height-lineNumber-1);
					} else {
						level.insertSpace(i, height-lineNumber-1);
					}
				}
				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public BrickLevel loadSync(AssetManager arg0, String arg1, FileHandle arg2,
			LevelParameter arg3) {
		return level;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String arg0, FileHandle arg1,
			LevelParameter arg2) {
		return null;
	}
	
	static public class LevelParameter extends AssetLoaderParameters<BrickLevel> {}
}
