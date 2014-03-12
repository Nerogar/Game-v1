package de.nerogar.gameV1.level;

import de.nerogar.gameV1.*;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityBuilding extends EntityFighting {

	public Position size;
	public Position centerPosition;

	public GameResources resourceCost = new GameResources(0, 0, 0);

	public EntityBuilding(Game game, World world, ObjectMatrix matrix) {
		//super(game, matrix, data, "houses/cone");
		super(game, world, matrix);
	}

	@Override
	public abstract void init(World world);

	//setObject("houses/test1", "houses/test1-1.png");

	//public void update(float time) {
	//	matrix.getPosition().y = game.world.land.getHeight(matrix.getPosition().x, matrix.getPosition().z);
	//}

	@Override
	public abstract void interact();

	@Override
	public abstract String getNameTag();

	@Override
	public abstract void click(int key);
	//if(key == 1)	markToRemove = true;

}