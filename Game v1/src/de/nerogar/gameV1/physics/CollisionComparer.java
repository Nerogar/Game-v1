package de.nerogar.gameV1.physics;

import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.level.Chunk;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityBlock;
import de.nerogar.gameV1.level.Position;

public class CollisionComparer {

	private static final Double MIN_HEIGHT = 0d;
	private static final Double MAX_HEIGHT = 5d;

	private ArrayList<Integer>[][] grid;

	private World world;

	// shift addieren: reale Koordinate -> grid-Index
	// shift abziehen: grid-Index -> reale Koordinate
	private Position shift = new Position(0, 0);
	private Position max = new Position(0, 0);

	private final int GRIDSIZE = 16;
	private final int MAX_DISTANCE = 256;

	public int comparations = 0;

	public CollisionComparer(World world) {
		this.world = world;

		//updateGrid();
	}

	public void newGrid() {

		Position mi = new Position(0, 0);
		Position ma = new Position(0, 0);

		for (int i = 0; i < world.land.chunks.size(); i++) {
			if (i == 0) {
				mi = new Position(world.land.chunks.get(i).chunkPosition.x, world.land.chunks.get(i).chunkPosition.z);
				ma = new Position(world.land.chunks.get(i).chunkPosition.x, world.land.chunks.get(i).chunkPosition.z);
			}
			if (world.land.chunks.get(i).chunkPosition.x < mi.x) mi.x = world.land.chunks.get(i).chunkPosition.x;
			if (world.land.chunks.get(i).chunkPosition.z < mi.z) mi.z = world.land.chunks.get(i).chunkPosition.z;
			if (world.land.chunks.get(i).chunkPosition.x > ma.x) ma.x = world.land.chunks.get(i).chunkPosition.x;
			if (world.land.chunks.get(i).chunkPosition.z > ma.z) ma.z = world.land.chunks.get(i).chunkPosition.z;
			//if (game.world.land.chunkPositions.get(i).z == 0) System.out.println("DRAMATISCHER FEHLER!");
		}

		//System.out.println(mi.x);

		mi.x *= Chunk.CHUNKSIZE;
		mi.z *= Chunk.CHUNKSIZE;
		ma.x++;
		ma.z++;
		ma.x *= Chunk.CHUNKSIZE;
		ma.z *= Chunk.CHUNKSIZE;

		this.shift = new Position(-mi.x, -mi.z);
		this.max = new Position((ma.x + shift.x) / GRIDSIZE, (ma.z + shift.z) / GRIDSIZE);

		//emptyGrid();

		updateGrid();

	}

	public void compare() {

		for (int i = 0; i < world.entityList.entities.size(); i++) {
			if (world.entityList.entities.get(i) instanceof EntityBlock) ((EntityBlock) world.entityList.entities.get(i)).colliding = false;

		}

		comparations = 0;

		for (int i = 0; i < max.x; i++) {
			for (int j = 0; j < max.z; j++) {

				if (grid[i][j].size() < 2) continue;

				for (int i2 = 0; i2 < grid[i][j].size(); i2++) {
					for (int j2 = i2 + 1; j2 < grid[i][j].size(); j2++) {
						if (world.entityList.entities.get(grid[i][j].get(i2)) instanceof EntityBlock && world.entityList.entities.get(grid[i][j].get(j2)) instanceof EntityBlock) {
							boolean col = PhysicHelper.isColliding(((EntityBlock) world.entityList.entities.get(grid[i][j].get(i2))).getBoundingBox(), ((EntityBlock) world.entityList.entities.get(grid[i][j].get(j2))).getBoundingBox());
							comparations++;
							if (col == true) {
								((EntityBlock) world.entityList.entities.get(grid[i][j].get(i2))).colliding = true;
								((EntityBlock) world.entityList.entities.get(grid[i][j].get(j2))).colliding = true;
								PhysicHelper.collisionReply(((EntityBlock) world.entityList.entities.get(grid[i][j].get(i2))), ((EntityBlock) world.entityList.entities.get(grid[i][j].get(j2))));
							}
							//System.out.println(i + ", " + j);
						}
					}

				}

				//}

			}
		}

	}

	public boolean isColliding(Entity entity, Class<?> entitySample) {
		BoundingAABB bound = entity.getAABB();
		int startX = (int) Math.floor((bound.a.getX() + shift.x) / GRIDSIZE);
		int startZ = (int) Math.floor((bound.a.getZ() + shift.z) / GRIDSIZE);
		int endX = (int) Math.ceil((bound.b.getX() + shift.x) / GRIDSIZE);
		int endZ = (int) Math.ceil((bound.b.getZ() + shift.z) / GRIDSIZE);
		startX = Math.max(0, startX);
		startZ = Math.max(0, startZ);
		endX = Math.min(max.x, endX);
		endZ = Math.min(max.z, endZ);
		for (int i = startX; i < endX; i++) {
			for (int j = startZ; j < endZ; j++) {
				for (int k = 0; k < grid[i][j].size(); k++) {
					Entity entity2 = world.entityList.entities.get(grid[i][j].get(k));
					if (entity2.equals(entity)) continue;
					//if (!(entitySample.isInstance(entity2))) continue;
					if (PhysicHelper.isColliding(entity.getBoundingBox(), entity2.getBoundingBox())) return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void emptyGrid() {

		grid = (ArrayList<Integer>[][]) new ArrayList[this.max.x][this.max.z];

		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				grid[i][j] = new ArrayList<Integer>();
	}

	public void updateGrid() {

		emptyGrid();

		for (Entity e: world.entityList.entities.values()) {
			BoundingAABB bound = (world.entityList.entities.get(e.id).getAABB());

			for (int j = (int) (bound.a.getX() + shift.x) / GRIDSIZE; j <= (int) (bound.b.getX() + shift.x) / GRIDSIZE; j++) {
				for (int k = (int) (bound.a.getZ() + shift.z) / GRIDSIZE; k <= (int) (bound.b.getZ() + shift.z) / GRIDSIZE; k++) {

					if (j >= 0 && j < max.x && k >= 0 && k < max.z) {
						grid[j][k].add(e.id);
					}
				}
			}
		}

		/*for (int i = 0; i < world.entityList.entities.size(); i++) {
			//if (game.world.entityList.entities.get(i) instanceof EntityBlock) {

			BoundingAABB bound = (world.entityList.entities.get(i).getAABB());

			for (int j = (int) (bound.a.getX() + shift.x) / GRIDSIZE; j <= (int) (bound.b.getX() + shift.x) / GRIDSIZE; j++) {
				for (int k = (int) (bound.a.getZ() + shift.z) / GRIDSIZE; k <= (int) (bound.b.getZ() + shift.z) / GRIDSIZE; k++) {

					if (j >= 0 && j < max.x && k >= 0 && k < max.z) {
						grid[j][k].add(i);
					}
				}
			}
			//}
		}*/
	}

	public void renderGrid() {
		for (int i = 0; i < max.x; i++) {
			for (int j = 0; j < max.z; j++) {
				if (grid[i][j].size() > 0) {
					BoundingRender.renderAABB(new BoundingAABB(new Vector3d(i * GRIDSIZE - shift.x, 0, j * GRIDSIZE - shift.z), new Vector3d((i + 1) * GRIDSIZE - shift.x, 10, (j + 1) * GRIDSIZE - shift.z)), 0x0000FF);
				}
			}
		}
	}

	private int maxMinGridPosX(int pos) {
		if (pos < 0)
			pos = 0;
		else if (pos > max.x) pos = max.x;
		return pos;
	}

	@SuppressWarnings("unused")
	private void renderBox(int x, int z) {
		BoundingRender.renderAABB(new BoundingAABB(new Vector3d(x * GRIDSIZE, 0, z * GRIDSIZE), new Vector3d((x + 1) * GRIDSIZE, 10, (z + 1) * GRIDSIZE)), 0xff00ff);
	}

	public static double getIntersectionZ(Vector3d s, Vector3d d, int xa) {
		double r = (xa - s.getX()) / d.getX();
		if (r < 0) return s.getZ();
		return s.getZ() + r * d.getZ();
	}

	public static Vector3d getRayAABBIntersection(Ray ray, BoundingAABB aabb) {

		Vector3d intersection = null;
		Vector3d newIntersection = null;
		Vector3d[][] faces = aabb.getFaces();
		double distance = Double.MAX_VALUE;
		double newDistance = 0;

		for (int i = 0; i < faces.length; i++) {
			newIntersection = GeometryHelper.getLinePolygonIntersection(ray, faces[i]);
			if (newIntersection != null) {
				newDistance = Vector3d.subtract(newIntersection, ray.getStart()).getValue();
				if (newDistance < distance) {
					distance = newDistance;
					intersection = newIntersection;
				}
			}
		}

		return intersection;
	}

	public Vector3d getNearestFloorIntersectionWithRay(Ray ray, World world) {

		if (ray.getDirection().getX() == 0) {
			ray.getDirection().add(new Vector3d(.000000001, 0, 0));
		}

		double minLoadX = world.loadPosition.x - (world.land.maxChunkLoadDistance + 1) * Chunk.CHUNKSIZE;
		double maxLoadX = world.loadPosition.x + (world.land.maxChunkLoadDistance + 1) * Chunk.CHUNKSIZE + 1;
		double minLoadZ = world.loadPosition.z - (world.land.maxChunkLoadDistance + 1) * Chunk.CHUNKSIZE;
		double maxLoadZ = world.loadPosition.z + (world.land.maxChunkLoadDistance + 1) * Chunk.CHUNKSIZE + 1;
		minLoadX -= minLoadX % Chunk.CHUNKSIZE;
		maxLoadX -= maxLoadX % Chunk.CHUNKSIZE;
		minLoadZ -= minLoadZ % Chunk.CHUNKSIZE;
		maxLoadZ -= maxLoadZ % Chunk.CHUNKSIZE;
		if (ray.getStart().getX() - MAX_DISTANCE > minLoadX) minLoadX = ray.getStart().getX() - MAX_DISTANCE;
		if (ray.getStart().getX() + MAX_DISTANCE < maxLoadX) maxLoadX = ray.getStart().getX() + MAX_DISTANCE;
		if (ray.getStart().getZ() - MAX_DISTANCE > minLoadZ) minLoadZ = ray.getStart().getZ() - MAX_DISTANCE;
		if (ray.getStart().getZ() + MAX_DISTANCE < maxLoadZ) maxLoadZ = ray.getStart().getZ() + MAX_DISTANCE;

		int minX = (int) Math.floor(((ray.getDirection().getX() >= 0) ? ray.getStart().getX() : minLoadX));
		int maxX = (int) Math.floor(((ray.getDirection().getX() > 0) ? maxLoadX : ray.getStart().getX()));
		minX = (int) Math.floor(Math.min(maxLoadX, minX));
		minX = (int) Math.floor(Math.max(minLoadX, minX));
		maxX = (int) Math.floor(Math.min(maxLoadX, maxX));
		maxX = (int) Math.floor(Math.max(minLoadX, maxX));

		Double lastZ = ray.getZatX(minX);
		if (lastZ == null) lastZ = ray.getStart().getZ();

		Double thisZ, ymin, ymax;
		int minZ, maxZ;

		ArrayList<Position> positions = new ArrayList<Position>();
		HashMap<Position, Double> heightMap = new HashMap<Position, Double>();

		//System.out.println("x-Iteration: "+minX+" bis "+maxX);
		for (int i = minX; i <= maxX; i++) {

			thisZ = ray.getLine().getZatX(i + 1);
			if (thisZ == null) thisZ = lastZ;
			Double lastZnow = lastZ;
			if (i == minX) {
				lastZnow = Math.max(minLoadZ, lastZnow);
				lastZnow = Math.min(maxLoadZ, lastZnow);
			}
			Double thisZnow = thisZ;
			if (thisZ < minLoadZ && lastZ < minLoadZ) continue;
			if (thisZ > maxLoadZ && lastZ > maxLoadZ) continue;
			lastZ = thisZ;

			ymin = (ray.getDirection().getX() > 0) ? ray.getYatX(i) : ray.getYatX(i + 1);
			ymax = (ray.getDirection().getX() > 0) ? ray.getYatX(i + 1) : ray.getYatX(i);
			if (ymin == null && ymax == null) continue;
			if (ymin != null && ymax != null) {
				if (ray.getDirection().getX() > 0 && ray.getDirection().getY() < 0 && ymin < MIN_HEIGHT) break;
				if (ray.getDirection().getX() < 0 && ray.getDirection().getY() < 0 && ymax > MAX_HEIGHT) break;
				if (ymin < MIN_HEIGHT || ymax > MAX_HEIGHT) continue;
			}

			thisZnow = Math.max(minLoadZ, thisZ);
			thisZnow = Math.min(maxLoadZ, thisZ);

			minZ = (lastZnow < thisZnow) ? ((int) Math.floor(lastZnow)) : ((int) Math.floor(thisZnow));
			maxZ = ((lastZnow < thisZnow) ? ((int) Math.floor(thisZnow)) : ((int) Math.floor(lastZnow)));
			minZ = (int) Math.floor(Math.min(maxLoadZ, minZ));
			minZ = (int) Math.floor(Math.max(minLoadZ, minZ));
			maxZ = (int) Math.floor(Math.min(maxLoadZ, maxZ));
			maxZ = (int) Math.floor(Math.max(minLoadZ, maxZ));

			for (int j = minZ; j <= maxZ; j++) {

				ymin = (ray.getDirection().getZ() > 0) ? ray.getYatZ(j) : ray.getYatZ(j + 1);
				ymax = (ray.getDirection().getZ() > 0) ? ray.getYatZ(j + 1) : ray.getYatZ(j);
				if (ymin == null && ymax == null) continue;
				if (ymin != null && ymax != null) {
					if (ray.getDirection().getZ() > 0 && ray.getDirection().getY() < 0 && ymin < MIN_HEIGHT) break;
					if (ray.getDirection().getZ() < 0 && ray.getDirection().getY() < 0 && ymax > MAX_HEIGHT) break;
					if (ymin < MIN_HEIGHT || ymax > MAX_HEIGHT) continue;
				}

				Position pos = new Position(i, j);
				Position posX = new Position(i + 1, j);
				Position posZ = new Position(i, j + 1);
				Position posXZ = new Position(i + 1, j + 1);
				if (!heightMap.containsKey(pos)) heightMap.put(pos, (double) world.land.getHeight(pos));
				if (!heightMap.containsKey(posX)) heightMap.put(posX, (double) world.land.getHeight(posX));
				if (!heightMap.containsKey(posZ)) heightMap.put(posZ, (double) world.land.getHeight(posZ));
				if (!heightMap.containsKey(posXZ)) heightMap.put(posXZ, (double) world.land.getHeight(posXZ));
				positions.add(pos);
			}
			//System.out.println("highest: " + world.land.getHighestBetween(new Position(0, 0), new Position(-64, -64)));
			//System.out.println("Höhenmaplänge: " + heightMap.size());
		}
		Double distance = Double.MAX_VALUE;
		Double newDistance;
		Vector3d newIntersection = null, intersection = null;
		for (Position pos : positions) {

			Vector3d[][] polygons = new Vector3d[2][3];
			polygons[0][0] = new Vector3d(pos.x, heightMap.get(pos), pos.z);
			polygons[0][1] = new Vector3d(pos.x, heightMap.get(new Position(pos.x, pos.z + 1)), pos.z + 1);
			polygons[0][2] = new Vector3d(pos.x + 1, heightMap.get(new Position(pos.x + 1, pos.z + 1)), pos.z + 1);
			polygons[1][0] = new Vector3d(pos.x, heightMap.get(pos), pos.z);
			polygons[1][1] = new Vector3d(pos.x + 1, heightMap.get(new Position(pos.x + 1, pos.z + 1)), pos.z + 1);
			polygons[1][2] = new Vector3d(pos.x + 1, heightMap.get(new Position(pos.x + 1, pos.z)), pos.z);

			for (Vector3d[] polygon : polygons) {

				newIntersection = GeometryHelper.getLinePolygonIntersection(ray, polygon);
				//RenderHelper.drawTriangle(polygon[0], polygon[1], polygon[2], 1,0,0);
				comparations++;
				if (newIntersection != null) {
					newDistance = Vector3d.subtract(ray.getStart(), newIntersection).getSquaredValue();
					if (newDistance < distance) {
						intersection = newIntersection;
						distance = newDistance;
					}
				}
			}

		}

		//if (Timer.instance.getFramecount() % 60 == 0 && GameOptions.instance.getBoolOption("debug")) System.out.println(comparations + " Bodenvergleiche letzten Frame.");

		return intersection;

	}

	public ArrayList<Position> getGridPositionsInRay(Ray ray) {
		Vector3d start = ray.getStart();
		Vector3d dir = ray.getDirection();

		// Schleifenanfang x-Richtung
		double startX = start.getX();
		startX += shift.x;

		// Schleifenende x-Richtung
		int endXGrid = dir.getX() > 0 ? max.x : 0;

		int startXGrid = ((startX / GRIDSIZE) < endXGrid) ? MathHelper.roundDownToInt(startX / GRIDSIZE, 1) : MathHelper.roundUpToInt(startX / GRIDSIZE, 1);
		startXGrid = maxMinGridPosX(startXGrid); // zwischen 0 und max

		// Array der Schnittpunkt-Z-Werte
		double[] za = new double[(startXGrid <= endXGrid) ? endXGrid + 2 : startXGrid + 2];

		za[startXGrid] = (getIntersectionZ(start, dir, (startXGrid * GRIDSIZE) - shift.x) + shift.z) / GRIDSIZE;

		ArrayList<Position> positions = new ArrayList<Position>();

		// Schleife x-Richtung
		for (int i = startXGrid; (startXGrid < endXGrid) ? (i < endXGrid) : (i > endXGrid); i += (startXGrid < endXGrid) ? 1 : -1) {

			// Der Strahl in Parameterform angegeben: g: a-> = s-> + r*d->
			// 
			// Nur die X-Werte: xa = xs + r*xd
			// einzige Unbekannte: r
			// nach r aufgelöst: r = (xa - xs) / xd
			//
			// Nur die Z-Werte: za = zs + r*zd
			// r eingesetzt: za = zs + ((xa - xs) / xd)*zd
			// einzige Unbekannte: za (Der z-Wert des Schnittpunktes der Geraden mit dem Grid)

			int nexti = (startXGrid < endXGrid) ? i + 1 : i - 1;
			za[nexti] = (getIntersectionZ(start, dir, (nexti * GRIDSIZE) - shift.x) + shift.z) / GRIDSIZE;

			int startZGrid = (za[i] < za[nexti]) ? MathHelper.roundDownToInt(za[i], 1) : MathHelper.roundUpToInt(za[i], 1);
			int endZGrid = (za[i] < za[nexti]) ? MathHelper.roundUpToInt(za[nexti], 1) : MathHelper.roundDownToInt(za[nexti], 1);

			if (startZGrid < 0) startZGrid = 0;
			if (startZGrid > max.z) startZGrid = max.z;
			if (endZGrid < 0) endZGrid = 0;
			if (endZGrid > max.z) endZGrid = max.z;

			for (int j = startZGrid; (startZGrid < endZGrid) ? (j < endZGrid) : (j > endZGrid); j += (startZGrid < endZGrid) ? 1 : -1) {
				int ri = i;
				int rj = j;
				if (dir.getX() < 0) ri -= 1;
				if (dir.getZ() < 0) rj -= 1;
				BoundingRender.renderAABB(new BoundingAABB(new Vector3d(ri * GRIDSIZE - shift.x, 0, rj * GRIDSIZE - shift.z), new Vector3d((ri + 1) * GRIDSIZE - shift.x, 10, (rj + 1) * GRIDSIZE - shift.z)), 0xff00ff);
				positions.add(new Position(ri, rj));

			}
		}

		return positions;
	}

	public Entity[] getEntitiesInRay(Ray ray) {

		ArrayList<Position> positions = getGridPositionsInRay(ray);

		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < positions.size(); i++) {
			Position pos = positions.get(i);
			if (pos.x < 0 || pos.z < 0 || pos.x >= grid.length || pos.z >= grid[0].length) {
				System.out.println("getEntitiesInRay(" + ray.toString() + ") error: pos.x = " + pos.x + ", " + pos.z + " = " + pos.z);
				continue;
			}
			for (int j = 0; j < grid[pos.x][pos.z].size(); j++) {
				ids.add(grid[pos.x][pos.z].get(j));
				BoundingRender.renderAABB(new BoundingAABB(new Vector3d(pos.x * GRIDSIZE - shift.x, 0, pos.z * GRIDSIZE - shift.z), new Vector3d((pos.x + 1) * GRIDSIZE - shift.x, 10, (pos.z + 1) * GRIDSIZE - shift.z)), 0xffff00);
			}
		}

		ArrayList<Entity> entities = new ArrayList<Entity>();
		ArrayList<Double> distances = new ArrayList<Double>();
		Vector3d intersection;
		for (int i = 0; i < ids.size(); i++) {
			Entity entity = world.entityList.entities.get(ids.get(i));
			if (!entities.contains(entity)) {
				intersection = getRayAABBIntersection(ray, entity.getAABB());
				if (intersection != null) {
					entities.add(entity);
					double distance = Vector3d.subtract(ray.getStart(), intersection).getSquaredValue();
					distances.add(distance);
				}
			}
		}

		Entity[] entitiesA = new Entity[entities.size()];
		entities.toArray(entitiesA);
		Double[] distancesA = new Double[distances.size()];
		distances.toArray(distancesA);

		// vorläufig bubble-sort!
		for (int i = entitiesA.length - 1; i > 0; i--) {

			for (int j = i; j > 0; j--) {
				if (distancesA[j] < distancesA[j - 1]) {
					Double helpD = distancesA[j];
					distancesA[j] = distancesA[j - 1];
					distancesA[j - 1] = helpD;
					Entity helpE = entitiesA[j];
					entitiesA[j] = entitiesA[j - 1];
					entitiesA[j - 1] = helpE;
				}
			}

		}

		return entitiesA;

	}

	public ArrayList<Entity> getEntitiesInChunk(Chunk chunk) {
		Position chunkPosition = chunk.chunkPosition;
		int xMin = (chunkPosition.x * Chunk.CHUNKSIZE) + shift.x;
		int zMin = (chunkPosition.z * Chunk.CHUNKSIZE) + shift.z;
		int xMax = ((chunkPosition.x + 1) * Chunk.CHUNKSIZE) + shift.x;
		int zMax = ((chunkPosition.z + 1) * Chunk.CHUNKSIZE) + shift.z;

		ArrayList<Entity> entities = new ArrayList<Entity>();

		for (int i = (xMin / GRIDSIZE); i < (xMax / GRIDSIZE); i++) {
			for (int j = (zMin / GRIDSIZE); j < (zMax / GRIDSIZE); j++) {
				int gridX = (i * GRIDSIZE) - shift.x;
				int gridZ = (j * GRIDSIZE) - shift.z;

				for (int k = 0; k < grid[i][j].size(); k++) {

					Entity entity = world.entityList.entities.get(grid[i][j].get(k));
					double entityX = entity.matrix.position.getX();
					double entityZ = entity.matrix.position.getZ();

					//System.out.println("grid: " + gridX + "|" + gridZ + " entity: " + entityX + "|" + entityZ);
					if (entityX >= gridX && entityX < gridX + GRIDSIZE && entityZ >= gridZ && entityZ < gridZ + GRIDSIZE) {
						entities.add(entity);
					}
				}
			}
		}
		return entities;
	}

	public void removeEntitiesInChunk(Chunk chunk) {
		Position chunkPosition = chunk.chunkPosition;
		int xMin = (chunkPosition.x * Chunk.CHUNKSIZE) + shift.x;
		int zMin = (chunkPosition.z * Chunk.CHUNKSIZE) + shift.z;
		int xMax = ((chunkPosition.x + 1) * Chunk.CHUNKSIZE) + shift.x;
		int zMax = ((chunkPosition.z + 1) * Chunk.CHUNKSIZE) + shift.z;

		for (int i = (xMin / GRIDSIZE); i < (xMax / GRIDSIZE); i++) {
			for (int j = (zMin / GRIDSIZE); j < (zMax / GRIDSIZE); j++) {
				int gridX = (i * GRIDSIZE) - shift.x;
				int gridZ = (j * GRIDSIZE) - shift.z;

				for (int k = 0; k < grid[i][j].size(); k++) {

					Entity entity = world.entityList.entities.get(grid[i][j].get(k));
					if (entity != null) {
						double entityX = entity.matrix.position.getX();
						double entityZ = entity.matrix.position.getZ();

						if (entityX >= gridX && entityX < gridX + GRIDSIZE && entityZ >= gridZ && entityZ < gridZ + GRIDSIZE) {
							world.entityList.entities.remove(grid[i][j].get(k));
						}
					}
				}
			}
		}
		world.entityList.removeNullEntities();
		//game.world.entityList.entities.set(grid[i][j].get(k), null);
	}

	public void cleanup() {

		this.max = new Position(0, 0);

	}

}
