package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.network.PacketClickEntity;
import de.nerogar.gameV1.network.PacketEntity;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityTree extends Entity {

	//private int size = 3;
	//private ALSource sound = null;
	//private float elapsedTime;

	public EntityTree(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		boundingBox = new BoundingAABB(new Vector3d(-1, 0, -1), new Vector3d(1, 4, 1));
	}

	@Override
	public void init(World world) {
		setObject("tree", "tree.png");
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

	public void interact() {
		//droppe Holz
		//reduziere Holz
	}

	@Override
	public void click(int key) {
		if (key == 0) {
			matrix.getPosition().add(new Vector3d(0, 1, 0));
			//ALSource sound2 = SoundManager.instance.create("forecast_elevator.ogg", ALSource.PRIORITY_LOW, matrix.position, new Vector3d(0, 0, 0), false, true, 1, 1);
			//if (sound2 != null) sound2.play();
		} else if (key == 1) {
			matrix.getPosition().add(new Vector3d(0, -1, 0));
		}

	}

	@Override
	public String getNameTag() {
		return "Tree";
	}

	@Override
	public void update(float time, ArrayList<PacketEntity> packets) {
		for (PacketEntity packet : packets) {
			if (packet instanceof PacketClickEntity) {
				System.out.println(((PacketClickEntity) packet).mouseButton);
			}
		}

		/*elapsedTime += time;
		int rand = 1;
		if (elapsedTime > 1) {
			Random random = new Random();
			rand = random.nextInt(5);
			elapsedTime -= 1;
		}
		if (rand == 0) {
			if (sound != null) if (sound.isDeleted()) sound = null;
			if (sound == null) {
				if (Vector3d.subtract(matrix.position, world.camera.getCamPosition()).getSquaredValue() < 500) {
					sound = SoundManager.instance.create("tree_crack.wav", ALSource.PRIORITY_LOW, matrix.position, new Vector3d(0, 0, 0), false, true, 5, 1);
					// Weil Justin genervt ist
					//if (sound != null) sound.play();
				}
			}
		}*/
	}

}