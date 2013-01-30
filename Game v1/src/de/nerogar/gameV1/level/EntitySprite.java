package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.object.Object3D;
import de.nerogar.gameV1.object.Object3DBank;
import de.nerogar.gameV1.physics.*;

public abstract class EntitySprite extends Entity {

	public Object3D object;

	public EntitySprite(Game game, ObjectMatrix matrix, String objectName) {
		this.game = game;
		this.matrix = matrix;
		setObject(objectName, null);
	}

	public void setObject(String objectName, String textureName) {
		object = Object3DBank.instance.getObject(objectName);
	}

	public void update(float time) {

	}

	public void load(DNFile chunkFile, String folder) {
		matrix.position.setX(chunkFile.getFloat(folder + ".position.x"));
		matrix.position.setY(chunkFile.getFloat(folder + ".position.y"));
		matrix.position.setZ(chunkFile.getFloat(folder + ".position.z"));

		loadProperties();
	}

	public void save(DNFile chunkFile, String folder) {
		chunkFile.addNode(folder + ".type", getNameTag());
		chunkFile.addNode(folder + ".position.x", matrix.position.getX());
		chunkFile.addNode(folder + ".position.y", matrix.position.getY());
		chunkFile.addNode(folder + ".position.z", matrix.position.getZ());

		saveProperties();
	}

	public abstract void saveProperties();

	public abstract void loadProperties();

	public abstract void interact(); // wenn andere entities oder objekte mit dieser entity interagieren

	public abstract void click(int key);// klick mit der maus, macht sinn, oder?

	public String getNameTag() {
		return "DefaultEntity";
	}
}