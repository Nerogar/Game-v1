package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.*;

public abstract class EntityBlock extends EntityParticle {

	public boolean colliding = false;

	public EntityBlock(Game game, ObjectMatrix matrix, float mass, float sca) {
		super(game, matrix, mass);

		// Zu Debug-Zwecken eine vorl�ufige, festkodierte AABB
		boundingBox = new BoundingAABB(new Vector3d(-4, 0, -4), new Vector3d(4, 8, 4));

		matrix.scaling = new Vector3d(sca, sca, sca);

	}

	public EntityBlock() {
		super();
	}

	public void updatePhysics(float time) {

		updatePosition(time);

		//BoundingRender.renderAABB(BoundingBox);

	}

	public void render() {
		//BoundingRender.renderAABB((BoundingAABB)getBoundingBox(), 0x00FF00);
		if (GameOptions.instance.getBoolOption("debug")) {
			if (!colliding) displayBoundingBox(getBoundingBox(), 0x00FF00);
			else displayBoundingBox(getBoundingBox(), 0xFF0000);

		}
		object.render(matrix,texture);
	}

	@Override
	public String getNameTag() {
		return "Block";
	}

}