package de.nerogar.gameV1;

import java.util.ArrayList;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public final class InputHandler {
	private static ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedKeysNext = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedMouseButtons = new ArrayList<Integer>();
	private static ArrayList<Integer> pressedMouseButtonsNext = new ArrayList<Integer>();
	private static ArrayList<Integer> onPressedMouseButtons = new ArrayList<Integer>();
	private static ArrayList<Integer> onReleasedMouseButtons = new ArrayList<Integer>();
	private static ArrayList<String> pressedGamepadButtons = new ArrayList<String>();
	private static ArrayList<String> pressedGamepadButtonsNext = new ArrayList<String>();
	private static Controller joystick = null;
	private static ArrayList<Key> registeredKeys = new ArrayList<Key>();
	private static ArrayList<GamepadButton> registeredGamepadButtons = new ArrayList<GamepadButton>();
	private static Vector3d start, dir;

	public static void update(Game game) {
		updateOns(game);
		pressedKeys.clear();
		pressedKeys.addAll(pressedKeysNext);
		pressedMouseButtons.clear();
		pressedMouseButtons.addAll(pressedMouseButtonsNext);
		pressedGamepadButtons.clear();
		pressedGamepadButtons.addAll(pressedGamepadButtonsNext);
		if (joystick != null) if (!joystick.poll()) joystick = null;
	}

	public static void updateOns(Game game) {
		onPressedMouseButtons.clear();
		for (Integer button : pressedMouseButtonsNext) {
			if (pressedMouseButtons.indexOf(button) == -1) onPressedMouseButtons.add(button);
		}
		onReleasedMouseButtons.clear();
		for (Integer button : pressedMouseButtons) {
			if (pressedMouseButtonsNext.indexOf(button) == -1) onReleasedMouseButtons.add(button);
		}
	}

	public static void updateMousePositions(Game game) {
		Camera camera = game.world.camera;

		float height = (float) Display.getHeight();
		float width = (float) Display.getWidth();
		float fov = (float) GameOptions.instance.getDoubleOption("fov");
		float fovMultY = (float) Math.tan(fov / 2f / 180f * Math.PI);
		float fovMultX = (float) Math.tan(fov / 2f / 180f * Math.PI) / height * width;
		float mouseX = (float) (Mouse.getX() / width * 2 - 1);
		float mouseY = (float) (Mouse.getY() / height * 2 - 1);

		/*float mouseRotSin = (float) Math.sin(mouseX * fov / 180f * Math.PI);
		float mouseRotCos = (float) Math.cos(mouseX * fov / 180f * Math.PI);
		float mouseRotDownSin = (float) Math.sin(mouseY * fov / 180f * Math.PI);
		float mouseRotDownCos = (float) Math.cos(mouseY * fov / 180f * Math.PI);*/

		float camRotSin = (float) Math.sin(camera.rotation / 180f * Math.PI);
		float camRotCos = (float) Math.cos(camera.rotation / 180f * Math.PI);
		float camRotDownSin = (float) Math.sin(camera.rotationDown / 180f * Math.PI);
		float camRotDownCos = (float) Math.cos(camera.rotationDown / 180f * Math.PI);

		Vector3d dirLoc = new Vector3d(0, 0, 0);

		//Camera down Rotation:
		dirLoc.setY(-camRotDownSin);
		dirLoc.setZ(-camRotDownCos);
		
		//Mouse Position Y:
		dirLoc.addY(mouseY * camRotDownCos * fovMultY);
		dirLoc.addZ(-mouseY * camRotDownSin * fovMultY);
		//dirLoc.y += mouseY * camRotDownCos * fovMultY;
		//dirLoc.z -= mouseY * camRotDownSin * fovMultY;

		//Mouse Position X:

		dirLoc.addX(mouseX * fovMultX);
		//dirLoc.x += mouseX * fovMultX;

		//Camera Rotation:

		dir = new Vector3d(0, 0, 0);

		dir.setZ(camRotCos * dirLoc.getZ() + camRotSin * dirLoc.getX());
		dir.setX(camRotCos * dirLoc.getX() - camRotSin * dirLoc.getZ());
		dir.setY(dirLoc.getY());
		//dir.z = camRotCos * dirLoc.z + camRotSin * dirLoc.x;
		//dir.x = camRotCos * dirLoc.x - camRotSin * dirLoc.z;
		//dir.y = dirLoc.y;

		start = new Vector3d(camera.scrollX, camera.scrollY, camera.scrollZ);
	}

	public static void renderMouseRay() {
		if (start == null) return;
		if (!GameOptions.instance.getBoolOption("debug")) return;
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_LINES);
		glColor3f(1.0f, 1.0f, 0.0f);
		//glVertex3f(start.x, start.y, start.z);
		//glVertex3f(start.x + dir.x * 100, start.y + dir.y * 100, start.z + dir.z * 100);
		glVertex3f(start.getXf(), start.getYf(), start.getZf());
		glVertex3f(start.getXf() + dir.getXf() * 10000, start.getYf() + dir.getYf() * 10000, start.getZf() + dir.getZf() * 10000);
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}

	public static Vector3d get3DmousePosition() {
		if (start == null) return new Vector3d(0, 0, 0);
		return start;
	}

	public static Vector3d get3DmouseDirection() {
		if (dir == null) return new Vector3d(0, 0, 0);
		return dir;
	}

	//update gamepad buttons
	public static void loadGamepad() {
		boolean loaded = false;
		for (Controller c : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
			System.out.println(c.getType() + " found: " + c.getName());
			if (c.getType() == Controller.Type.GAMEPAD) {
				joystick = c;

				loaded = true;
			}
		}
		if (!loaded) System.out.println("Could not find any gamepad.");
	}

	public static void printGamepadButtons() {//debug
		if (joystick == null) return;
		for (Component c : joystick.getComponents()) {
			System.out.println(c.getName() + " / " + c.getIdentifier() + " : " + c.getPollData());
		}
	}

	public static void registerGamepadButton(String name, String button, float deadZone) {
		if (registeredGamepadButtons.indexOf(name) == -1) {
			registeredGamepadButtons.add(new GamepadButton(name, button, deadZone));
		}
	}

	public static void unregisterGamepadButton(String name) {
		int index = registeredGamepadButtons.indexOf(name);
		if (index != -1) {
			registeredGamepadButtons.remove(index);
		}
	}

	public static boolean isGamepadButtonDown(String name) {
		if (joystick == null) return false;
		int index = -1;

		for (int i = 0; i < registeredGamepadButtons.size(); i++) {
			if (registeredGamepadButtons.get(i).name.equals(name)) index = i;
		}

		if (index != -1) {
			String button = registeredGamepadButtons.get(index).button;
			float deadZone = registeredGamepadButtons.get(index).deadZone;

			for (Component c : joystick.getComponents()) {
				if (c.getIdentifier().toString().equals(button)) {
					boolean state = c.getPollData() > deadZone ? true : false;
					updateGamepadButton(button, state);
					return state;
				}
			}
		}
		return false;
	}

	public static boolean isGamepadButtonPressed(String name) {
		if (joystick == null) return false;
		int index = -1;

		for (int i = 0; i < registeredGamepadButtons.size(); i++) {
			if (registeredGamepadButtons.get(i).name.equals(name)) index = i;
		}

		if (index != -1) {
			String button = registeredGamepadButtons.get(index).button;
			float deadZone = registeredGamepadButtons.get(index).deadZone;

			for (Component c : joystick.getComponents()) {
				if (c.getIdentifier().toString().equals(button)) {
					boolean state = c.getPollData() > deadZone ? true : false;
					boolean lastState = pressedGamepadButtons.indexOf(button) == -1;
					pressedGamepadButtons.add(button);
					updateGamepadButton(button, state);
					return state && lastState;
				}
			}
		}
		return false;
	}

	public static float getGamepadButtonData(String name) {
		if (joystick == null) return 0.0f;
		int index = -1;

		for (int i = 0; i < registeredGamepadButtons.size(); i++) {
			if (registeredGamepadButtons.get(i).name.equals(name)) index = i;
		}

		if (index != -1) {
			String button = registeredGamepadButtons.get(index).button;

			for (Component c : joystick.getComponents()) {
				if (c.getIdentifier().toString().equals(button)) {
					float pollData = c.getPollData();
					float deadZone = registeredGamepadButtons.get(index).deadZone;

					if (pollData < 0.0f) return pollData < deadZone * -1 ? pollData : 0.0f;
					else return pollData > deadZone ? pollData : 0.0f;
				}
			}
		}
		return 0.0f;
	}

	private static void updateGamepadButton(String button, boolean state) {
		if (state) addGamepadButton(button);
		else removeGamepadButton(button);
	}

	private static void addGamepadButton(String button) {
		if (pressedGamepadButtonsNext.indexOf(button) == -1) pressedGamepadButtonsNext.add(button);
	}

	private static void removeGamepadButton(String button) {
		if (pressedGamepadButtonsNext.indexOf(button) != -1) pressedGamepadButtonsNext.remove(pressedGamepadButtonsNext.indexOf(button));
	}

	//update keyboard buttons

	public static void registerKey(String name, int button) {
		if (registeredKeys.indexOf(name) == -1) {
			registeredKeys.add(new Key(name, button));
		}
	}

	public static boolean isKeyDown(String name) {
		int index = -1;

		for (int i = 0; i < registeredKeys.size(); i++) {
			if (registeredKeys.get(i).name.equals(name)) index = i;
		}
		if (index != -1) return isKeyDown(registeredKeys.get(index).button);
		return false;
	}

	public static boolean isKeyPressed(String name) {
		int index = -1;

		for (int i = 0; i < registeredKeys.size(); i++) {
			if (registeredKeys.get(i).name.equals(name)) index = i;
		}
		if (index != -1) return isKeyPressed(registeredKeys.get(index).button);
		return false;
	}

	public static boolean isKeyDown(int key) {
		boolean state = Keyboard.isKeyDown(key);
		updateKey(key, state);
		return state;
	}

	public static boolean isKeyPressed(int key) {
		boolean state = Keyboard.isKeyDown(key);
		boolean lastState = pressedKeys.indexOf(key) == -1;
		pressedKeys.add(key);
		updateKey(key, state);
		return state && lastState;
	}

	public static char getPressedKey() {
		int key = 0;
		boolean lastState = true;
		while (Keyboard.next()) {
			key = Keyboard.getEventCharacter();
		}
		return (char) (lastState ? key : 0);
	}

	private static void updateKey(int key, boolean state) {
		if (state) addKey(key);
		else removeKey(key);
	}

	private static void addKey(int key) {
		if (pressedKeysNext.indexOf(key) == -1) pressedKeysNext.add(key);
	}

	private static void removeKey(int key) {
		if (pressedKeysNext.indexOf(key) != -1) pressedKeysNext.remove(pressedKeysNext.indexOf(key));
	}

	//update mouse buttons

	public static boolean isMouseButtonDown(int button) {
		boolean state = Mouse.isButtonDown(button);
		updateMouseButton(button, state);
		return state;

	}

	/*public static boolean isMouseButtonPressed(int button) {
	    boolean state = Mouse.isButtonDown(button);
	    boolean lastState = pressedMouseButtons.indexOf(button) != -1;
	    //if (state && !lastState) pressedMouseButtons.add(button);
	    updateMouseButton(button, state);
	    return state && !lastState;
	}

	public static boolean isMouseButtonReleased(int button) {//geht nicht :(
		boolean state = Mouse.isButtonDown(button);
		boolean lastState = pressedMouseButtons.indexOf(button) != -1;
		//if (!state && lastState) pressedMouseButtons.remove(pressedMouseButtons.indexOf(button));
		//updateMouseButton(button, state);
		return !state && lastState;
	}*/

	public static boolean isMouseButtonPressed(int button) {
		boolean state = Mouse.isButtonDown(button);
		//if (state) pressedMouseButtons.add(button);
		updateMouseButton(button, state);
		return onPressedMouseButtons.indexOf(button) != -1;
	}

	public static boolean isMouseButtonReleased(int button) {
		boolean state = Mouse.isButtonDown(button);
		//boolean lastState = pressedMouseButtons.indexOf(button) != -1;
		//if (!state && lastState) pressedMouseButtons.remove(pressedMouseButtons.indexOf(button));
		updateMouseButton(button, state);
		return onReleasedMouseButtons.indexOf(button) != -1;
	}

	private static void updateMouseButton(int key, boolean state) {
		if (state) addMouseButton(key);
		else removeMouseButton(key);
	}

	private static void addMouseButton(int key) {
		if (pressedMouseButtonsNext.indexOf(key) == -1) pressedMouseButtonsNext.add(key);
	}

	private static void removeMouseButton(int key) {
		if (pressedMouseButtonsNext.indexOf(key) != -1) pressedMouseButtonsNext.remove(pressedMouseButtonsNext.indexOf(key));
	}

	public static class Key {
		public String name;
		public int button;

		public Key(String name, int button) {
			this.name = name;
			this.button = button;
		}

		public boolean equals(Object o) {
			if (o instanceof Key) {
				if (((Key) o).name.equals(name)) return true;
			}
			return false;
		}
	}

	public static class GamepadButton {
		public String name;
		public String button;
		public float deadZone;

		public GamepadButton(String name, String button, float deadZone) {
			this.name = name;
			this.button = button;
			this.deadZone = deadZone;
		}

		public boolean equals(GamepadButton o) {
			if (o instanceof GamepadButton) {
				if (((GamepadButton) o).name.equals(name)) return true;
			}
			return false;
		}
	}
}
