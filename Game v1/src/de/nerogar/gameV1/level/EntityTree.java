package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityTree extends Entity {

	//private int size = 3;

	public EntityTree(Game game, ObjectMatrix matrix) {
		super(game, matrix, "tree");
		texture = "tree.png";/*Textur name*/
		boundingBox = new BoundingAABB(new Vector3d(-1, 0, -1), new Vector3d(1, 4, 1));
	}

	@Override
	public void saveProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadProperties() {
		// TODO Auto-generated method stub

	}

	public void interact() {
		//droppe Holz
		//reduziere Holz
	}

	@Override
	public void click(int key) {
		if (key == 0) {
			matrix.addPosition(new Vector3d(0, 1, 0));
		} else if (key == 1) {
			matrix.addPosition(new Vector3d(0, -1, 0));
		}

	}

	@Override
	public String getNameTag() {
		return "Tree";
	}

}