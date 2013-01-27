package de.nerogar.gameV1;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Camera {

	public float scrollX;
	public float scrollY;
	public float scrollZ;
	private float scrollXLoc;
	private float scrollYLoc;
	private float scrollZLoc;
	private float scrollBack;
	public float rotation;
	public float rotationDown;

	private final float MAXSCROLLBACK = 20;
	private final float MINSCROLLUP = 10;
	private final float MINROTDOWN = 35;

	private int mouseX = Mouse.getX();
	private int mouseY = Mouse.getY();
	private boolean dragRotate = false;
	private boolean dragMoving = false;

	private Timer timer = Timer.instance;

	public void init() {
		scrollX = 0;
		scrollY = 10;
		scrollZ = -20;
		scrollXLoc = 0;
		scrollYLoc = 0;
		scrollZLoc = 0;

		scrollBack = MAXSCROLLBACK;
		rotation = 0;
		rotationDown = 35;

		InputHandler.registerGamepadButton("right", "x", 0.25f);
		InputHandler.registerGamepadButton("forw", "y", 0.25f);
		InputHandler.registerGamepadButton("rot", "rx", 0.25f);
		InputHandler.registerGamepadButton("up", "z", 0.25f);
	}

	public void updatePostition() {
		float sL = 0;
		float sR = 0;
		float sU = 0;
		float sD = 0;
		float sY = 0;
		float rY;

		//position mouseGrab

		if (mouseY < Display.getHeight() / 4f) {
			if (InputHandler.isMouseButtonPressed(2)) dragRotate = true;
		} else {
			if (InputHandler.isMouseButtonPressed(2)) dragMoving = true;
		}

		if (InputHandler.isMouseButtonReleased(2)) {
			dragRotate = false;
			dragMoving = false;
		}

		if (dragRotate) rotation += timer.delta / 100f * (Mouse.getX() - mouseX);
		if (dragMoving) {
			sL += (Mouse.getX() - mouseX) / 5f;
			sD += (Mouse.getY() - mouseY) / 5f;
		}

		mouseX = Mouse.getX();
		mouseY = Mouse.getY();

		//////
		if (!(dragRotate || dragMoving) && GameOptions.instance.getBoolOption("fullscreen")) {
			sL = Mouse.getX() == 0 ? 1.0f : 0.0f;
			sR = Mouse.getX() == Display.getWidth() - 1 ? 1.0f : 0.0f;
			sU = Mouse.getY() == Display.getHeight() - 1 ? 1.0f : 0.0f;
			sD = Mouse.getY() == 0 ? 1.0f : 0.0f;
		}

		sL += InputHandler.isKeyDown(Keyboard.KEY_A) ? 1.0f : 0.0f;
		sR += InputHandler.isKeyDown(Keyboard.KEY_D) ? 1.0f : 0.0f;
		sU += InputHandler.isKeyDown(Keyboard.KEY_W) ? 1.0f : 0.0f;
		sD += InputHandler.isKeyDown(Keyboard.KEY_S) ? 1.0f : 0.0f;

		float gamepadX = InputHandler.getGamepadButtonData("right");
		float gamepadZ = InputHandler.getGamepadButtonData("forw");

		sL += gamepadX < 0 ? -gamepadX : 0.0f;
		sR += gamepadX > 0 ? gamepadX : 0.0f;
		sU += gamepadZ < 0 ? -gamepadZ : 0.0f;
		sD += gamepadZ > 0 ? gamepadZ : 0.0f;

		scrollXLoc += Math.cos((rotation / 360) * Math.PI * 2) * timer.delta / 50f * sR;
		scrollZLoc += Math.sin((rotation / 360) * Math.PI * 2) * timer.delta / 50f * sR;

		scrollXLoc -= Math.cos((rotation / 360) * Math.PI * 2) * timer.delta / 50f * sL;
		scrollZLoc -= Math.sin((rotation / 360) * Math.PI * 2) * timer.delta / 50f * sL;

		scrollZLoc += Math.cos((rotation / 360) * Math.PI * 2) * timer.delta / 50f * sD;
		scrollXLoc -= Math.sin((rotation / 360) * Math.PI * 2) * timer.delta / 50f * sD;

		scrollZLoc -= Math.cos((rotation / 360) * Math.PI * 2) * timer.delta / 50f * sU;
		scrollXLoc += Math.sin((rotation / 360) * Math.PI * 2) * timer.delta / 50f * sU;

		rY = InputHandler.getGamepadButtonData("rot");
		rY += InputHandler.isKeyDown(Keyboard.KEY_RIGHT) ? 1.0f : 0;
		rY -= InputHandler.isKeyDown(Keyboard.KEY_LEFT) ? 1.0f : 0;
		rotation += timer.delta / 20f * rY;

		sY = InputHandler.isKeyDown(Keyboard.KEY_DOWN) ? 1.0f : 0;
		sY -= InputHandler.isKeyDown(Keyboard.KEY_UP) ? 1.0f : 0;

		sY = InputHandler.isKeyDown(Keyboard.KEY_DOWN) ? 1.0f : 0;
		sY -= InputHandler.isKeyDown(Keyboard.KEY_UP) ? 1.0f : 0;
		sY -= Mouse.getDWheel() / 30f;
		sY += InputHandler.getGamepadButtonData("up");

		scrollYLoc += timer.delta / 20f * sY;

		scrollYLoc = (scrollYLoc < 0 ? 0 : scrollYLoc);
		scrollY = scrollYLoc + MINSCROLLUP;

		float temp;

		temp = (float) Math.sqrt(scrollYLoc) * 10;
		rotationDown = (temp <= 90 - MINROTDOWN ? temp + MINROTDOWN : 90);

		temp = 90 - rotationDown;

		temp = (temp / (90 - MINROTDOWN)) * MAXSCROLLBACK;
		scrollBack = (temp > MAXSCROLLBACK ? MAXSCROLLBACK : temp);

		scrollX = (float) (scrollXLoc - Math.sin((rotation / 360) * 3.1415927 * 2) * scrollBack);
		scrollZ = (float) (scrollZLoc + Math.cos((rotation / 360) * 3.1415927 * 2) * scrollBack);

	}
}
