package de.nerogar.gameV1.physics;

import java.util.ArrayList;
import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.Chunk;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityBlock;
import de.nerogar.gameV1.level.Position;

public class CollisionComparer {

	private ArrayList<Integer>[][] grid;

	private Game game;

	private Position shift = new Position(0, 0);
	private Position max = new Position(0, 0);

	private final int GRIDSIZE = 16;

	public int comparations = 0;

	public CollisionComparer(Game game) {

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
			if (game.world.land.chunks.get(i).chunkPosition.x < mi.x) mi.x = game.world.land.chunks.get(i).chunkPosition.x;
			if (game.world.land.chunks.get(i).chunkPosition.z < mi.z) mi.z = game.world.land.chunks.get(i).chunkPosition.z;
			if (game.world.land.chunks.get(i).chunkPosition.x > ma.x) ma.x = game.world.land.chunks.get(i).chunkPosition.x;
			if (game.world.land.chunks.get(i).chunkPosition.z > ma.z) ma.z = game.world.land.chunks.get(i).chunkPosition.z;
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

		for (int i = 0; i < game.world.entityList.entities.size(); i++) {
			if (game.world.entityList.entities.get(i) instanceof EntityBlock) ((EntityBlock) game.world.entityList.entities.get(i)).colliding = false;

		}

		comparations = 0;

		for (int i = 0; i < max.x; i++) {
			for (int j = 0; j < max.z; j++) {

				if (grid[i][j].size() < 2) continue;

				for (int i2 = 0; i2 < grid[i][j].size(); i2++) {
					for (int j2 = i2 + 1; j2 < grid[i][j].size(); j2++) {
						if (game.world.entityList.entities.get(grid[i][j].get(i2)) instanceof EntityBlock && game.world.entityList.entities.get(grid[i][j].get(j2)) instanceof EntityBlock) {
							boolean col = PhysicHelper.isColliding(((EntityBlock) game.world.entityList.entities.get(grid[i][j].get(i2))).getBoundingBox(), ((EntityBlock) game.world.entityList.entities.get(grid[i][j].get(j2))).getBoundingBox());
							comparations++;
							if (col == true) {
								((EntityBlock) game.world.entityList.entities.get(grid[i][j].get(i2))).colliding = true;
								((EntityBlock) game.world.entityList.entities.get(grid[i][j].get(j2))).colliding = true;
								PhysicHelper.collisionReply(((EntityBlock) game.world.entityList.entities.get(grid[i][j].get(i2))), ((EntityBlock) game.world.entityList.entities.get(grid[i][j].get(j2))));
							}
							//System.out.println(i + ", " + j);
						}
					}

				}

				//}

			}
		}

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

		for (int i = 0; i < game.world.entityList.entities.size(); i++) {
			//if (game.world.entityList.entities.get(i) instanceof EntityBlock) {

			BoundingAABB bound = (game.world.entityList.entities.get(i).getAABB());

			for (int j = (int) (bound.a.getX() + shift.x) / GRIDSIZE; j <= (int) (bound.b.getX() + shift.x) / GRIDSIZE; j++) {
				for (int k = (int) (bound.a.getZ() + shift.z) / GRIDSIZE; k <= (int) (bound.b.getZ() + shift.z) / GRIDSIZE; k++) {

					if (j >= 0 && j < max.x && k >= 0 && k < max.z) {
						grid[j][k].add(i);
					}
				}
			}
			//}
		}
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

	public static Vector3d getLinePolygonIntersection(Line line, Vector3d[] points) {

		// Ein Polygon braucht mindestens 3 Punkte
		if (points.length < 3) return null;

		Plane plane = new Plane(points[0], points[1], points[2]);
		Vector3d candidate = PhysicHelper.getLinePlaneIntersection(line, plane);
		if (candidate == null) {
			//System.out.println("Kein Kandidat!");
			return null;
		}

		// Per Halbvektoren-Test wird überprüft, ob der Punkt innerhalb der Fläche liegt.
		// Der Punkt soll weit weg liegen, damit die Rechnung genau bleibt
		Vector3d pointInPlane = plane.getRandomPoint(candidate.getX() + 100000);

		int intersections = 0;
		Vector3d a, b, intersection;
		Line halfVector, edge;
		for (int i = 0; i < points.length; i++) {
			a = points[i];
			if (i == points.length - 1)
				b = points[0];
			else
				b = points[i + 1];
			halfVector = new Ray(candidate, Vector3d.subtract(pointInPlane, candidate));
			edge = new LineSegment(a, Vector3d.subtract(b, a));
			intersection = PhysicHelper.getLineLineIntersection(halfVector, edge);
			if (intersection != null) {
				intersections++;
			}
		}
		if (intersections % 2 == 1) return candidate;
		return null;
	}

	public static Vector3d getRayAABBIntersection(Ray ray, BoundingAABB aabb) {

		Vector3d intersection = null;
		Vector3d newIntersection = null;
		Vector3d[][] faces = aabb.getFaces();
		double distance = Double.MAX_VALUE;
		double newDistance = 0;

		for (int i = 0; i < faces.length; i++) {
			newIntersection = getLinePolygonIntersection(ray, faces[i]);
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

	public Vector3d getFloorIntersectionInRay(Ray ray) {

		Vector3d intersection = null;

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

	public ArrayList<Entity> getEntitiesInRay(Ray ray) {

		ArrayList<Position> positions = getGridPositionsInRay(ray);

		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < positions.size(); i++) {
			Position pos = positions.get(i);
			for (int j = 0; j < grid[pos.x][pos.z].size(); j++) {
				ids.add(grid[pos.x][pos.z].get(j));
				BoundingRender.renderAABB(new BoundingAABB(new Vector3d(pos.x * GRIDSIZE - shift.x, 0, pos.z * GRIDSIZE - shift.z), new Vector3d((pos.x + 1) * GRIDSIZE - shift.x, 10, (pos.z + 1) * GRIDSIZE - shift.z)), 0xffff00);
			}
		}

		ArrayList<Entity> entities = new ArrayList<Entity>();
		ArrayList<Double> distances = new ArrayList<Double>();
		Vector3d intersection;
		for (int i = 0; i < ids.size(); i++) {
			Entity entity = game.world.entityList.entities.get(ids.get(i));
			if (!entities.contains(entity)) {
				intersection = getRayAABBIntersection(ray, entity.getAABB());
				if (intersection != null) {
					entities.add(entity);
					double distance = Vector3d.subtract(ray.getStart(), intersection).getSquaredValue();
					distances.add(distance);
				}
			}
		}
		
		Entity[] entitiesA = (Entity[]) entities.toArray();
		Vector3d[] intersectionsA = (Vector3d[]) distances.toArray();
		
		
		
		return entities;

		/*Vector3d start = ray.getStart();
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

		ArrayList<Integer> ids = new ArrayList<Integer>();

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
				//if (ri >= 0 && rj >= 0 && ri < grid.length && rj < grid[0].length) {
				//System.out.println("ri: " + ri + "  rj: " + rj);
				for (int k = 0; k < grid[ri][rj].size(); k++) {
					ids.add(grid[ri][rj].get(k));
					//System.out.println("id: "+grid[ri][rj].get(k));
					BoundingRender.renderAABB(new BoundingAABB(new Vector3d(ri * GRIDSIZE - shift.x, 0, rj * GRIDSIZE - shift.z), new Vector3d((ri + 1) * GRIDSIZE - shift.x, 10, (rj + 1) * GRIDSIZE - shift.z)), 0xffff00);
				}
				//}
			}*/

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

					Entity entity = game.world.entityList.entities.get(grid[i][j].get(k));
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

					Entity entity = game.world.entityList.entities.get(grid[i][j].get(k));
					if (entity != null) {
						double entityX = entity.matrix.position.getX();
						double entityZ = entity.matrix.position.getZ();

						if (entityX >= gridX && entityX < gridX + GRIDSIZE && entityZ >= gridZ && entityZ < gridZ + GRIDSIZE) {
							game.world.entityList.entities.set(grid[i][j].get(k), null);
						}
					}
				}
			}
		}
		game.world.entityList.removeNullEntities();
		//game.world.entityList.entities.set(grid[i][j].get(k), null);
	}

	public void cleanup() {

		this.max = new Position(0, 0);

	}

}
