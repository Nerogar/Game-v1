package de.nerogar.gameV1.sound;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.OpenALException;

import de.nerogar.gameV1.Camera;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;

public class SoundManager {

	public static SoundManager instance = new SoundManager();
	private final int maxSources = 16;
	private ALSource[] sourceSpots;
	private String[] preLoadedFiles = new String[] { "forecast.ogg", "forecast_elevator.ogg" };

	private static Vector3d lastListenerPosition = new Vector3d(0, 0, 0);
	private static double lastUpdateTime = System.nanoTime() / 1000000000;

	public SoundManager() {
		sourceSpots = new ALSource[maxSources];
	}

	public ALSource create(String filename, int priority, Vector3d position, Vector3d velocity, boolean looping, boolean destroyWhenDone, float gain, float pitch) {
		int spot = getFreeSpot(priority);
		if (spot == -1) {
			System.out.println("No free source spot for playing " + filename);
		} else {
			int sourceID = ALHelper.genSources();
			try {
				sourceSpots[spot] = new ALSource(sourceID, ALBufferBank.instance.getSound(filename), position, velocity, looping, destroyWhenDone, gain, pitch);
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
					if (spot == -1)
						spot = i;
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
				if (sourceSpots[i].isStopped() && sourceSpots[i].destroyWhenDone) {
					System.out.println("removing a source");
					sourceSpots[i].markDeleted();
					sourceSpots[i].destroy();
					sourceSpots[i] = null;
				} else {
					sourceSpots[i].update();
				}
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

	public static void setListenerLazy(Camera camera) {
		float camRotSin = (float) Math.sin(camera.rotation / 180f * Math.PI);
		float camRotCos = (float) Math.cos(camera.rotation / 180f * Math.PI);
		float camRotDownSin1 = (float) Math.sin(camera.rotationDown / 180f * Math.PI);
		float camRotDownCos1 = (float) Math.cos(camera.rotationDown / 180f * Math.PI);
		float camRotDownSin2 = (float) Math.sin((camera.rotationDown - 90) / 180f * Math.PI);
		float camRotDownCos2 = (float) Math.cos((camera.rotationDown - 90) / 180f * Math.PI);

		Vector3d dirLocAt = new Vector3d(0, 0, 0);
		Vector3d dirLocUp = new Vector3d(0, 0, 0);

		//Camera down Rotation:
		dirLocAt.setY(-camRotDownSin1);
		dirLocAt.setZ(-camRotDownCos1);
		dirLocUp.setY(-camRotDownSin2);
		dirLocUp.setZ(-camRotDownCos2);

		Vector3d vectorAt = new Vector3d(0, 0, 0);
		Vector3d vectorUp = new Vector3d(0, 0, 0);

		vectorAt.setZ(camRotCos * dirLocAt.getZ() + camRotSin * dirLocAt.getX());
		vectorAt.setX(camRotCos * dirLocAt.getX() - camRotSin * dirLocAt.getZ());
		vectorAt.setY(dirLocAt.getY());
		vectorUp.setZ(camRotCos * dirLocUp.getZ() + camRotSin * dirLocUp.getX());
		vectorUp.setX(camRotCos * dirLocUp.getX() - camRotSin * dirLocUp.getZ());
		vectorUp.setY(dirLocUp.getY());

		double time = (float) System.nanoTime() / 1000000000;
		double elapsedTime = time - lastUpdateTime;
		if (elapsedTime > 0) {
			Vector3d elapsedPosition = Vector3d.subtract(camera.getCamPosition(), lastListenerPosition);
			Vector3d velocity = new Vector3d(elapsedPosition.getX() / elapsedTime, elapsedPosition.getY() / elapsedTime, elapsedPosition.getZ() / elapsedTime);
			lastUpdateTime = time;
			lastListenerPosition.set(camera.getCamPosition());
			SoundManager.setListener(camera.getCamPosition(), velocity, vectorAt, vectorUp);
		}

	}
	
	public ALSource getSource(int i) {
		if (i < 0 || i >= maxSources) return null;
		return sourceSpots[i];
	}

}
