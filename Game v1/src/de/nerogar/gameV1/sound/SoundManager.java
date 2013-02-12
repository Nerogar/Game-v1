package de.nerogar.gameV1.sound;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.OpenALException;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;

public class SoundManager {

	public static SoundManager instance = new SoundManager();
	private final int maxSources = 16;
	private ALSource[] sourceSpots;
	private String[] preLoadedFiles = new String[] { "forecast.ogg", "forecast_elevator.ogg" };

	public SoundManager() {
		sourceSpots = new ALSource[maxSources];
	}

	public ALSource create(String filename, int priority, Vector3d position, Vector3d velocity, boolean looping, float gain, float pitch) {
		int spot = getFreeSpot(priority);
		if (spot == -1) {
			System.out.println("No free source spot for playing "+filename);
		} else {
			int sourceID = ALHelper.genSources();
			try {
				sourceSpots[spot] = new ALSource(sourceID, ALBufferBank.instance.getSound(filename), position, velocity, looping, gain, pitch);
			} catch (OpenALException | IOException | LWJGLException e) {
				sourceSpots[spot] = null;
				e.printStackTrace();
			}
		}
		if (spot != -1) return sourceSpots[spot];
		return null;
	}

	public int getFreeSpot(int priority) {
		int spot = -1;
		for (int i = 0; i < sourceSpots.length; i++) {
			if (sourceSpots[i] == null) {
				spot = i;
				break;
			} else {
				if (sourceSpots[i].priority < priority) {
					if (spot == -1) spot = i;
					else if (sourceSpots[i].priority < sourceSpots[spot].priority) spot = i;
				}
			}
		}
		return spot;
	}

	public void preLoadSounds() {
		for (String filename : preLoadedFiles) {
			try {
				RenderHelper.updateLoadingScreen("Lade " + filename);
				ALBufferBank.instance.addSound(filename);
			} catch (OpenALException | IOException | LWJGLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setListener(Vector3d position, Vector3d velocity, Vector3d orientationAt, Vector3d orientationUp) {
		ALHelper.setListener(position, velocity, orientationAt, orientationUp);
	}
	
	public void update() {
		for (int i = 0; i < sourceSpots.length; i++) {
			if (sourceSpots[i] != null) {
				sourceSpots[i].update();
			}
		}
	}
	
	public void clear() {
		for (int i = 0; i < sourceSpots.length; i++) {
			if (sourceSpots[i] != null) {
				sourceSpots[i].destroy();
				sourceSpots[i] = null;
			}
		}
	}

}
