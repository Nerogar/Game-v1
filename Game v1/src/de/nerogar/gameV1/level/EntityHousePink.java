package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameResources;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityHousePink extends EntityBuilding {

	public Position size = new Position(2, 2);
	public GameResources resourceCost = new GameResources(200, 100, 100);

	public EntityHousePink(Game game, ObjectMatrix matrix) {
		super(game, matrix);
		boundingBox = new BoundingAABB(new Vector3d(-size.x/2, 0, -size.z/2), new Vector3d(size.x/2, 1, size.z/2));
	}

	@Override
	public void init(World world) {
		setObject("houses/house", "houses/house_pink.png");
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getNameTag() {
		return "HousePink";
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
		if(key == 1)	markToRemove = true;
	}

	@Override
	public Position getSize() {
		return new Position(2, 2);
	}

	@Override
	public GameResources getResourceCosts() {
		return new GameResources(300, 200, 200);
	}

}
