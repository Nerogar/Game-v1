package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameResources;
import de.nerogar.gameV1.Vector2d;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityBuilding extends Entity {

	public Position size = new Position(1, 1);
	public GameResources resourceCost = new GameResources(0, 0, 0);

	public EntityBuilding(Game game, ObjectMatrix matrix) {
		//super(game, matrix, data, "houses/cone");
		super(game, matrix);
		boundingBox = new BoundingAABB(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0));
	}
	
	//public abstract Vector2d getSize2();

	@Override
	public abstract void init(World world);
	//setObject("houses/test1", "houses/test1-1.png");

	//public void update(float time) {
	//	matrix.getPosition().y = game.world.land.getHeight(matrix.getPosition().x, matrix.getPosition().z);
	//}

	public abstract void interact();

	@Override
	public abstract String getNameTag();

	@Override
	public abstract void saveProperties(DNFile chunkFile, String folder);

	@Override
	public abstract void loadProperties(DNFile chunkFile, String folder);

	@Override
	public abstract void click(int key);
	//if(key == 1)	markToRemove = true;

}