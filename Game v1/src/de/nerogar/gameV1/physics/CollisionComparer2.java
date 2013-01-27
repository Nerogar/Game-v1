package de.nerogar.gameV1.physics;

import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.Chunk;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityBlock;
import de.nerogar.gameV1.level.Position;

public class CollisionComparer2 {

	private HashMap<Position, ArrayList<Integer>> grid;

	private Game game;

	//private Position shift = new Position(0, 0);
	private Position min = new Position(0, 0);
	private Position max = new Position(0, 0);

	private final int GRIDSIZE = 16;

	public int comparations = 0;

	public CollisionComparer2(Game game) {

		this.game = game;

		//updateGrid();

	}

	public void newGrid() {

		Position mi = new Position(0, 0);
		Position ma = new Position(0, 0);

		for (int i = 0; i < game.world.land.chunks.size(); i++) {
			if (i == 0) {
				mi = new Position(game.world.land.chunks.get(i).chunkPosition.x, game.world.land.chunks.get(i).chunkPosition.z);
				ma = new Position(game.world.land.chunks.get(i).chunkPosition.x, game.world.land.chunks.get(i).chunkPosition.z);
			}
			if (game.world.land.chunks.get(i).chunkPosition.x < mi.x)
				mi.x = game.world.land.chunks.get(i).chunkPosition.x;
			if (game.world.land.chunks.get(i).chunkPosition.z < mi.z)
				mi.z = game.world.land.chunks.get(i).chunkPosition.z;
			if (game.world.land.chunks.get(i).chunkPosition.x > ma.x)
				ma.x = game.world.land.chunks.get(i).chunkPosition.x;
			if (game.world.land.chunks.get(i).chunkPosition.z > ma.z)
				ma.z = game.world.land.chunks.get(i).chunkPosition.z;
		}

		mi.x *= Chunk.CHUNKSIZE;
		mi.z *= Chunk.CHUNKSIZE;
		ma.x++;
		ma.z++;
		ma.x *= Chunk.CHUNKSIZE;
		ma.z *= Chunk.CHUNKSIZE;

		//this.shift = new Position(-mi.x, -mi.z);
		this.min = new Position(mi.x / GRIDSIZE, mi.z / GRIDSIZE);
		this.max = new Position(ma.x / GRIDSIZE, ma.z / GRIDSIZE);

		//emptyGrid();

		updateGrid();

	}

	public void compare() {

		for (int i = 0; i < game.world.entityList.entities.size(); i++) {
			if (game.world.entityList.entities.get(i) instanceof EntityBlock)
				((EntityBlock) game.world.entityList.entities.get(i)).colliding = false;

		}

		comparations = 0;

		for (int i = 0; i < max.x; i++) {
			for (int j = 0; j < max.z; j++) {

				ArrayList<Integer> list = grid.get(new Position(i, j));
				if (list == null)
					System.out.println("NULLL at i" + String.valueOf(i) + "-j" + String.valueOf(j));
				else {
					if (list.size() < 2)
						continue;

					for (int i2 = 0; i2 < list.size(); i2++) {
						for (int j2 = i2 + 1; j2 < list.size(); j2++) {
							if (game.world.entityList.entities.get(list.get(i2)) instanceof EntityBlock && game.world.entityList.entities.get(list.get(j2)) instanceof EntityBlock) {
								boolean col = PhysicHelper.isColliding(((EntityBlock) game.world.entityList.entities.get(list.get(i2))).getBoundingBox(), ((EntityBlock) game.world.entityList.entities.get(list.get(j2))).getBoundingBox());
								comparations++;
								if (col == true) {
									((EntityBlock) game.world.entityList.entities.get(list.get(i2))).colliding = true;
									((EntityBlock) game.world.entityList.entities.get(list.get(j2))).colliding = true;
									PhysicHelper.collisionReply(((EntityBlock) game.world.entityList.entities.get(list.get(i2))), ((EntityBlock) game.world.entityList.entities.get(list.get(j2))));
								}
								//System.out.println(i + ", " + j);
							}
						}

					}
				}

				//}

			}
		}

		//System.out.println("Entities: "+game.world.entityList.entities.size()+", Vergleiche: "+comparations);

	}

	public void rebuildGrid() {
		//grid.clear();
		grid = new HashMap<Position, ArrayList<Integer>>();
		for (int i = min.x; i < max.x; i++) {
			for (int j = min.z; j < max.z; j++) {
				grid.put(new Position(i, j), new ArrayList<Integer>());
				//System.out.println("added grid at i"+String.valueOf(i)+"-j"+String.valueOf(j));
			}
		}
	}

	public void updateGrid() {

		rebuildGrid();

		for (int i = 0; i < game.world.entityList.entities.size(); i++) {
			//if (game.world.entityList.entities.get(i) instanceof EntityBlock) {

			Entity entity = game.world.entityList.entities.get(i);
			BoundingAABB bound = entity.getAABB();

			//System.out.println("bound "+i+": a("+bound.a.getX()+"|"+bound.a.getZ()+") b("+bound.b.getX()+"|"+bound.b.getZ()+")");
			
			for (int j = (int) (bound.a.getX() / GRIDSIZE); j <= (int) (bound.b.getX() / GRIDSIZE); j++) {
				for (int k = (int) (bound.a.getZ() / GRIDSIZE); k <= (int) (bound.b.getZ() / GRIDSIZE); k++) {

					if (j >= min.x && j < max.x && k >= min.z && k < max.z) {
						grid.get(new Position(j, k)).add(i);
						//System.out.println("added entity: j"+String.valueOf(j)+"-k"+String.valueOf(k));
					} else {
						//System.out.println("Entity not in grid. Ignored it. j"+String.valueOf(j)+"-k"+String.valueOf(k));
					}
				}
			}
			//}
		}
	}

	public void renderGrid() {
		for (int i = min.x; i < max.x; i++) {
			for (int j = min.z; j < max.z; j++) {
				ArrayList<Integer> list = grid.get(new Position(i, j));
				//System.out.println(list.size());
				if (list.size() > 0) {
					BoundingRender.renderAABB(new BoundingAABB(new Vector3d(i * GRIDSIZE, 0, j * GRIDSIZE), new Vector3d((i + 1) * GRIDSIZE, 10, (j + 1) * GRIDSIZE)), 0x0000FF);
					//renderBox(i, j);
					for (int k = 0; k < list.size(); k++) {
						//BoundingRender.renderLine(new Vector3d((i+.5)*GRIDSIZE, 10, (j+.5)*GRIDSIZE), game.world.entityList.entities.get(k).matrix.position, 0xFF0055);
					}
				}
			}
		}
	}

	/*private int maxMinGridPosX(int pos) {
		if (pos < 0) pos = 0;
		else if (pos > max.x) pos = max.x;
		return pos;
	}*/

	private int maxMinGridPosZ(int pos) {
		if (pos < 0)
			pos = 0;
		else if (pos > max.z)
			pos = max.z;
		return pos;
	}

	@SuppressWarnings("unused")
	private void renderBox(int x, int z) {
		BoundingRender.renderAABB(new BoundingAABB(new Vector3d(x * GRIDSIZE, 0, z * GRIDSIZE), new Vector3d((x + 1) * GRIDSIZE, 10, (z + 1) * GRIDSIZE)), 0xff00ff);
	}

	public ArrayList<Entity> getEntitiesInRay(Vector3d start, Vector3d dir) { //unfertig!!!

		double startX = start.getX();
		//double startZ = start.getZ() + shift.z;
		//System.out.println(startZ);
		int startGridX = (int) (startX / GRIDSIZE);
		int endGridX = dir.getX() > 0 ? max.x : 0;

		for (int i = startGridX; (startGridX < endGridX) ? (i < endGridX) : (i >= endGridX); i += (startGridX < endGridX) ? 1 : -1) {

			int startGridZ = (int) (((startX + i * GRIDSIZE) * (dir.getZ() / dir.getX())) / GRIDSIZE);
			//if(i == startGridX)System.out.println(startGridZ);
			//int startGridZ = (int) (startZ + (i - startGridX) * (dir.z / dir.x));
			startGridZ = maxMinGridPosZ(startGridZ);

			int endGridZ = (int) (((startX + (i + 1) * GRIDSIZE) * (dir.getZ() / dir.getX())) / GRIDSIZE);
			endGridZ = maxMinGridPosZ(endGridZ);

			for (int j = startGridZ; (startGridZ < endGridZ) ? (j < endGridZ) : (j > endGridZ); j += (startGridZ < endGridZ) ? 1 : -1) {

				BoundingRender.renderAABB(new BoundingAABB(new Vector3d(i * GRIDSIZE, 0, j * GRIDSIZE), new Vector3d((i + 1) * GRIDSIZE, 10, (j + 1) * GRIDSIZE)), 0xff00ff);

			}

		}

		//System.out.println((time2 - time1) / 1000000.0);

		/*
		int startX = (int) ((start.x + shift.x) / GRIDSIZE);
		int startZ = (int) ((start.z + shift.z) / GRIDSIZE);

		int startGridX = startX;
		int endGridX = dir.x > 0 ? max.x : 0;

		for (int i = startGridX; (startGridX < endGridX) ? (i < endGridX) : (i >= endGridX); i += (startGridX < endGridX) ? 1 : -1) {

			//int startGridZ = (int) (startZ + (i - startGridX) * (dir.z / dir.x));
			int startGridZ = (int) (startZ + (i - startGridX) * (dir.z / dir.x));
			startGridZ = maxMinGridPosZ(startGridZ);

			int endGridZ = (int) (startZ + (i - startGridX + (dir.z > 0 ? 1 : -1)) * (dir.z / dir.x)) + (dir.x > 0 ? 1 : -1);
			endGridZ = maxMinGridPosZ(endGridZ);

			for (int j = startGridZ; (startGridZ < endGridZ) ? (j < endGridZ) : (j > endGridZ); j += (startGridZ < endGridZ) ? 1 : -1) {

				BoundingRender.renderAABB(new BoundingAABB(new Vector3(i * GRIDSIZE - shift.x, 0, j * GRIDSIZE - shift.z), new Vector3((i + 1) * GRIDSIZE - shift.x, 10, (j + 1) * GRIDSIZE - shift.z)), 0xff00ff);

			}

		} 
		  
		 
		 */

		return new ArrayList<Entity>();
	}

	public ArrayList<Integer> getEntityIdsInChunk(Chunk chunk) {
		Position chunkPosition = chunk.chunkPosition;
		int xMin = chunkPosition.x * Chunk.CHUNKSIZE;
		int zMin = chunkPosition.z * Chunk.CHUNKSIZE;
		int xMax = (chunkPosition.x + 1) * Chunk.CHUNKSIZE;
		int zMax = (chunkPosition.z + 1) * Chunk.CHUNKSIZE;

		ArrayList<Integer> list = new ArrayList<Integer>();

		for (int i = (xMin / GRIDSIZE); i < (xMax / GRIDSIZE); i++) {
			for (int j = (zMin / GRIDSIZE); j < (zMax / GRIDSIZE); j++) {
				ArrayList<Integer> ids = grid.get(new Position(i, j));
				if (ids == null) {
					System.out.println("NULL at i" + String.valueOf(i) + "-j" + String.valueOf(j));
					System.out.println(grid.toString());
				} else
					for (int k = 0; k < ids.size(); k++) {
						//if (!list.contains(ids.get(k)))
						list.add(ids.get(k));
					}
			}
		}
		return list;
	}

	public ArrayList<Entity> getEntitiesInChunk(Chunk chunk) {
		ArrayList<Integer> list = getEntityIdsInChunk(chunk);
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < list.size(); i++) {
			Entity entity = game.world.entityList.entities.get(list.get(i));
			entities.add(entity);
		}
		return entities;
	}

	public void removeEntitiesInChunk(Chunk chunk) {
		ArrayList<Integer> list = getEntityIdsInChunk(chunk);
		for (int i = 0; i < list.size(); i++) {
			game.world.entityList.entities.set(list.get(i), null);
		}
		game.world.entityList.removeNullEntities();
	}

	public void cleanup() {
		min = new Position(0, 0);
		max = new Position(0, 0);
	}

}
