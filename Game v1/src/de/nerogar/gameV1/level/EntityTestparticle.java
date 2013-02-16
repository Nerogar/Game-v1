package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityTestparticle extends EntityParticle {
	
	public EntityTestparticle(Game game, ObjectMatrix matrix) {
		super(game, matrix, 1);
		saveEntity = false;
	}

	@Override
	public void init(World world) {
		setObject("houses/cone", "houses/cone.png");
		//setSprite(1, "houses/test1-1.png");
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
	public void interact() {
		return;
	}

	@Override
	public void click(int key) {
		return;
	}
	
	@Override
	public String getNameTag() {
		return "Testpartikel";
	}
}
