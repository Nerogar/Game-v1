package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityTestparticle extends EntityParticle {
	
	public EntityTestparticle(Game game, ObjectMatrix matrix) {
		super(game, matrix, "houses/cone", 1);
		texture = "houses/cone.png";/* Textur name */
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadProperties() {
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
