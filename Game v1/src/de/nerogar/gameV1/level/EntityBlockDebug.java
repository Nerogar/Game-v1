package de.nerogar.gameV1.level;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.*;

public abstract class EntityBlockDebug extends EntityBlock {

	public EntityBlockDebug(Game game, ObjectMatrix matrix, float mass, float sca) {
		super(game, matrix, mass, sca);

		// Zu Debug-Zwecken eine vorläufige, festkodierte AABB
		boundingBox = new BoundingAABB(new Vector3d(-4, 0, -4), new Vector3d(4, 8, 4));

		//matrix.scaling = new Vector3(0.5F, 0.5F, 0.5F);

		InputHandler.registerGamepadButton("a", "0", 0.25f);
		InputHandler.registerGamepadButton("b", "1", 0.25f);
		InputHandler.registerGamepadButton("x", "2", 0.25f);
		InputHandler.registerGamepadButton("y", "3", 0.25f);
	}

	public EntityBlockDebug() {
		super();
	}

	public void updatePhysics(float time) {

		//matrix.position = p;

		if (InputHandler.isKeyDown(Keyboard.KEY_Y)) addForce(new Vector3d(0, 10000 * time, 0));

		if (InputHandler.isKeyDown(Keyboard.KEY_I)) addForce(new Vector3d(0, 0, -10000 * time));
		if (InputHandler.isKeyDown(Keyboard.KEY_J)) addForce(new Vector3d(-10000 * time, 0, 0));
		if (InputHandler.isKeyDown(Keyboard.KEY_K)) addForce(new Vector3d(0, 0, 10000 * time));
		if (InputHandler.isKeyDown(Keyboard.KEY_L)) addForce(new Vector3d(10000 * time, 0, 0));

		if (InputHandler.isGamepadButtonDown("y")) addForce(new Vector3d(0, 0, -10000 * time));
		if (InputHandler.isGamepadButtonDown("x")) addForce(new Vector3d(-10000 * time, 0, 0));
		if (InputHandler.isGamepadButtonDown("a")) addForce(new Vector3d(0, 0, 10000 * time));
		if (InputHandler.isGamepadButtonDown("b")) addForce(new Vector3d(10000 * time, 0, 0));

		if (InputHandler.isKeyDown(Keyboard.KEY_P)) {
			matrix.position.setX(0);
			matrix.position.setZ(0);
			velocity.setX(0);
			velocity.setZ(0);
			velocity.setY(0);
		}

		updatePosition(time);

	}

	@Override
	public String getNameTag() {
		return "BlockDebug";
	}

}