package de.nerogar.gameV1.object;

import java.util.HashMap;

public class Object3DBank {
	private HashMap<String, Object3D> objects;

	public static Object3DBank instance = new Object3DBank();

	public Object3DBank() {
		objects = new HashMap<String, Object3D>();
	}

	public void loadObject(String filename) {
		WaveFrontLoader objLoader = new WaveFrontLoader();
		if (objects.get(filename) == null) {

			objects.put(filename, objLoader.loadObject("res/" + filename + ".obj"));
		}
	}

	public Object3D getObject(String filename) {
		Object3D retObject = objects.get(filename);
		if (retObject != null) { return retObject; }

		loadObject(filename);

		retObject = objects.get(filename);

		return retObject;
	}
}
