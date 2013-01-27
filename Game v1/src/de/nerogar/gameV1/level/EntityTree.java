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
		boundingBox = new BoundingAABB(new Vector3d(-4, 0, -4), new Vector3d(4, 8, 4));
	}

	public EntityTree() {
		setObject("tree", "tree.png");
		boundingBox = new BoundingAABB(new Vector3d(-4, 0, -4), new Vector3d(4, 8, 4));
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
		// TODO Auto-generated method stub

	}

	@Override
	public String getNameTag() {
		return "Tree";
	}

}