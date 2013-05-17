package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameResources;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityHouseBlue extends EntityBuilding {

	public EntityHouseBlue(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		size = new Position(2, 2);
		centerPosition = new Position(1, 1);
		resourceCost = new GameResources(200, 100, 100);
		boundingBox = new BoundingAABB(new Vector3d(-size.x / 2, 0, -size.z / 2), new Vector3d(size.x / 2, 1, size.z / 2));
	}

	@Override
	public void init(World world) {
		setObject("houses/house", "houses/house_blue.png");
	}

	@Override
	public void update(float time) {

		ObjectMatrix particleMatrix = new ObjectMatrix(new Vector3d(matrix.position.getX() + Math.random() * 2 - 1, matrix.position.getY() + 1, matrix.position.getZ() + Math.random() * 2 - 1));
		world.spawnEntity(new EntityTestparticle(game, world, particleMatrix));

	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getNameTag() {
		return "HouseBlue";
	}

	@Override
	public void saveProperties(DNFile chunkFile, String folder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void loadProperties(DNFile chunkFile, String folder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void click(int key) {
		// TODO Auto-generated method stub
		if (key == 1) markToRemove = true;
	}
}
