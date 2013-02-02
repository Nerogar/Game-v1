package de.nerogar.gameV1.physics;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;

import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.level.Chunk;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityBlock;
import de.nerogar.gameV1.level.Land;
import de.nerogar.gameV1.level.Position;

public class CollisionComparer {

	private ArrayList<Integer>[][] grid;

	private Game game;

	private Position shift = new Position(0, 0);
	private Position max = new Position(0, 0);

	private final int GRIDSIZE = 16;
	private final int MAX_DISTANCE = 128;

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

		//if(points.length == 3) {
		//	Vector3d OV  = points[0];
		//	Vector3d RV1 = Vector3d.subtract(points[2], OV);
		//	Vector3d RV2 = Vector3d.subtract(points[1], OV);
		//}

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

	public Vector3d getNearestFloorIntersectionWithRay(Ray ray, World world) {

		int minLoadX = world.loadPosition.x - world.land.maxChunkLoadDistance;
		int maxLoadX = world.loadPosition.x + world.land.maxChunkLoadDistance + 1;
		int minLoadZ = world.loadPosition.z - world.land.maxChunkLoadDistance;
		int maxLoadZ = world.loadPosition.z + world.land.maxChunkLoadDistance + 1;

		int minX = (int) ((ray.getDirection().getX() > 0) ? ray.getStart().getX() : ray.getStart().getX() - MAX_DISTANCE);
		int maxX = (int) ((ray.getDirection().getX() > 0) ? ray.getStart().getX() + MAX_DISTANCE : ray.getStart().getX()) + 1;
		minX = Math.min(maxLoadX, minX);
		minX = Math.max(minLoadX, minX);
		maxX = Math.min(maxLoadX, maxX);
		maxX = Math.max(minLoadX, maxX);

		Double lastZ = ray.getZ(new Vector3d(minX, 0, 0));
		if (lastZ == null) lastZ = 0d;
		Double rayYmin, rayYmax, thisZ, spotYmin, spotYmax;
		int minZ, maxZ;

		ArrayList<Position> positions = new ArrayList<Position>();
		HashMap<Position, Double> heightMap = new HashMap<Position, Double>();

		System.out.println("x-Iteration: " + minX + " - " + maxX);

		for (int i = minX; i <= maxX; i++) {
			thisZ = ray.getZ(new Vector3d(i + 1, 0, 0));
			if (thisZ == null) thisZ = lastZ;
			minZ = (lastZ < thisZ) ? lastZ.intValue() : thisZ.intValue();
			maxZ = ((lastZ < thisZ) ? thisZ.intValue() : lastZ.intValue()) + 1;
			minZ = Math.min(maxLoadZ, minZ);
			minZ = Math.max(minLoadZ, minZ);
			maxZ = Math.min(maxLoadZ, maxZ);
			maxZ = Math.max(minLoadZ, maxZ);

			System.out.println("z-Iteration: " + minZ + " - " + maxZ);
			for (int j = minZ; j <= maxZ; j++) {
				Position pos = new Position(i, j);
				Position posX = new Position(i + 1, j);
				Position posZ = new Position(i, j + 1);
				Position posXZ = new Position(i + 1, j + 1);
				if (!heightMap.containsKey(pos)) heightMap.put(pos, (double) world.land.getHeight(pos));
				if (!heightMap.containsKey(posX)) heightMap.put(posX, (double) world.land.getHeight(posX));
				if (!heightMap.containsKey(posZ)) heightMap.put(posZ, (double) world.land.getHeight(posZ));
				if (!heightMap.containsKey(posXZ)) heightMap.put(posXZ, (double) world.land.getHeight(posXZ));
				rayYmin = (ray.getDirection().getY() > 0) ? ray.getY(new Vector3d(i, 0, j)) : ray.getY(new Vector3d(i + 1, 0, j + 1));
				rayYmax = (ray.getDirection().getY() > 0) ? ray.getY(new Vector3d(i + 1, 0, j + 1)) : ray.getY(new Vector3d(i, 0, j));
				spotYmin = MathHelper.getLowest(heightMap.get(pos), heightMap.get(posX), heightMap.get(posZ), heightMap.get(posXZ));
				spotYmax = MathHelper.getHightest(heightMap.get(pos), heightMap.get(posX), heightMap.get(posZ), heightMap.get(posXZ));
				if (rayYmin == null) rayYmin = 0d;
				if (rayYmax == null) rayYmax = 0d;
				if (spotYmin == null) spotYmin = 0d;
				if (spotYmax == null) spotYmax = 0d;

				if (rayYmin > spotYmax || rayYmax < spotYmin) continue;
				positions.add(pos);
			}
			lastZ = thisZ;
		}

		Double distance = Double.MAX_VALUE;
		Double newDistance;
		Vector3d newIntersection = null, intersection = null;
		int comparations = 0;
		for (Position pos : positions) {

			Vector3d[][] polygons = new Vector3d[2][3];
			polygons[0][0] = new Vector3d(pos.x, heightMap.get(pos), pos.z);
			polygons[0][1] = new Vector3d(pos.x, heightMap.get(new Position(pos.x, pos.z + 1)), pos.z + 1);
			polygons[0][2] = new Vector3d(pos.x + 1, heightMap.get(new Position(pos.x + 1, pos.z + 1)), pos.z + 1);
			polygons[1][0] = new Vector3d(pos.x, heightMap.get(pos), pos.z);
			polygons[1][1] = new Vector3d(pos.x + 1, heightMap.get(new Position(pos.x + 1, pos.z)), pos.z);
			polygons[1][2] = new Vector3d(pos.x + 1, heightMap.get(new Position(pos.x + 1, pos.z + 1)), pos.z + 1);

			for (Vector3d[] polygon : polygons) {

				newIntersection = CollisionComparer.getLinePolygonIntersection(ray, polygon);
				drawTriangle(polygon[0], polygon[1], polygon[2]);
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

		System.out.println(comparations + " Bodenvergleiche");

		return intersection;

	}

	private void drawTriangle(Vector3d a, Vector3d b, Vector3d c) {
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_TRIANGLES);
		glColor3f(1f, 0f, 0f);
		glVertex3f(a.getXf(), a.getYf() + .01f, a.getZf());
		glVertex3f(b.getXf(), b.getYf() + .01f, b.getZf());
		glVertex3f(c.getXf(), c.getYf() + .01f, c.getZf());
		glEnd();
	}

	public Vector3d getNearestFloorIntersectionWithRay2(Ray ray, Land land) {

		Vector3d intersection = null;
		ArrayList<Position> gridPositions = getGridPositionsInRay(ray);

		Vector3d[][] polygons = new Vector3d[gridPositions.size() * GRIDSIZE * GRIDSIZE * 2][3];
		int counter = 0;

		for (Position pos : gridPositions) {

			//System.out.println(shift.x + "," + shift.z);
			int x = (pos.x * GRIDSIZE) - shift.x;
			int z = (pos.z * GRIDSIZE) - shift.z;

			//System.out.println("SHIFT: "+shift.x+" "+shift.z);
			// Wenn der höchste/niedrigste Punkt des Gridelements über dem niedrigsten/höchsten
			// Punkt des Strahls liegt, wird dieses Grid übersprungen
			Double minY = ray.getY(new Vector3d(x, 0, z));
			if (minY == null) minY = 0d;
			Double maxY = ray.getY(new Vector3d(x + GRIDSIZE, 0, z + GRIDSIZE));
			if (maxY == null) maxY = Double.MAX_VALUE;
			double minLandY = 0;
			double maxLandY = land.getHighestBetween(new Position(x, z), new Position(x + GRIDSIZE, z + GRIDSIZE)).getY();
			//System.out.println(new Position(x,z).toString()+"   ---   "+new Position(x+GRIDSIZE , z+GRIDSIZE).toString());

			if (minY > maxLandY || maxY < minLandY) continue;
			double[][] heights = new double[GRIDSIZE + 1][GRIDSIZE + 1];
			for (int i = 0; i <= GRIDSIZE; i++) {
				for (int j = 0; j <= GRIDSIZE; j++) {
					heights[i][j] = land.getHeight(x + i, z + j);
					//System.out.println(x + "," + z);
				}
			}

			Double borderX1 = ray.getX(new Vector3d(0, 0, z));
			if (borderX1 == null) borderX1 = 0d;
			Double borderX2 = ray.getX(new Vector3d(0, 0, z + GRIDSIZE));
			if (borderX2 == null) borderX2 = (double) GRIDSIZE;
			int minX = (int) ((borderX1 < borderX2) ? Math.floor(borderX1) : Math.floor(borderX2)) - x;
			int maxX = (int) ((borderX1 < borderX2) ? Math.ceil(borderX2) : Math.ceil(borderX1)) - x;
			System.out.println("min/max: " + minX + ", " + maxX);
			minX = Math.max(0, minX);
			minX = Math.min(GRIDSIZE - 1, minX);
			maxX = Math.max(0, minX);
			maxX = Math.min(GRIDSIZE - 1, minX);

			//System.out.println("X: "+minX+" bis "+maxX);

			for (int i = minX; i <= maxX; i++) {

				Double borderZ1 = ray.getZ(new Vector3d(x, 0, 0));
				if (borderZ1 == null) borderZ1 = 0d;
				Double borderZ2 = ray.getZ(new Vector3d(x + GRIDSIZE, 0, 0));
				if (borderZ2 == null) borderZ2 = (double) GRIDSIZE;

				int minZ = Math.max(0, (int) ((borderZ1 < borderZ2) ? Math.floor(borderZ1) : Math.floor(borderZ2)) - z);
				minZ = Math.min(GRIDSIZE - 1, minZ);
				int maxZ = Math.min(GRIDSIZE - 1, (int) ((borderZ1 < borderZ2) ? Math.ceil(borderZ2) : Math.ceil(borderZ1)) - z);
				maxZ = Math.max(0, minZ);

				//System.out.println("Z: "+minZ+" bis "+maxZ);

				for (int j = minZ; j <= maxZ; j++) {
					polygons[counter][0] = new Vector3d(x + i, heights[i][j], z + j);
					polygons[counter][1] = new Vector3d(x + i, heights[i][j + 1], z + j);
					polygons[counter][2] = new Vector3d(x + i, heights[i + 1][j + 1], z + j);
					counter++;
					polygons[counter][0] = new Vector3d(x + i, heights[i][j + 1], z + j);
					polygons[counter][1] = new Vector3d(x + i, heights[i + 1][j + 1], z + j);
					polygons[counter][2] = new Vector3d(x + i, heights[i + 1][j], z + j);
					counter++;
				}
			}
		}

		Double distance = Double.MAX_VALUE;
		Double newDistance;
		Vector3d newIntersection = null;
		Vector3d[] polygon;
		int comparations = 0;
		for (int i = 0; i < polygons.length; i++) {
			// Das Array ist größer, als wirklich Daten drinne sind. Deshalb ab der ersten "null" aufhören
			if (polygons[i][0] == null) break;
			polygon = polygons[i];
			//System.out.println(polygon[0].toString());
			newIntersection = CollisionComparer.getLinePolygonIntersection(ray, polygon);
			comparations++;
			if (newIntersection != null) {
				newDistance = Vector3d.subtract(ray.getStart(), newIntersection).getSquaredValue();
				if (newDistance < distance) {
					intersection = newIntersection;
					distance = newDistance;
				}
			}
		}
		//System.out.println(comparations+" Vergleiche");

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
