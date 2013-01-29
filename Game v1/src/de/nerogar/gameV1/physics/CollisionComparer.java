package de.nerogar.gameV1.physics;

import java.util.ArrayList;
import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector2d;
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

		//System.out.println("Entities: "+game.world.entityList.entities.size()+", Vergleiche: "+comparations);

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
		if (pos < 0) pos = 0;
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

	// erfundener Wert, ähnlich dem Bogenmaß, aber schneller zu berechnen und mathematische Scheiße.
	// dient nur zum überprüfen, ob ein Winkel größer oder kleiner ist
	// Zwischen 0 und 4.
	// Differenzen: 1 = 90° | 2 = 180°
	public static float getCircleAmount(Vector2d v) {
		Vector2d v2 = Vector2d.normalize(v);
		float value = (float) v2.y;
		if (v.x < 0 && v.y >= 0) {
			value = 2 - value;
		} else if (v.x < 0 && v.y < 0) {
			value = 2 + value * -1;
		} else if (v.x >= 0 && v.y < 0) {
			value = 4 - value * -1;
		}
		//System.out.println(String.valueOf(value));
		return value;
	}

	public static boolean is2dSeparated(Vector2d v1, Vector2d[] vx) {

		int left = 0;
		int right = 0;

		float v1Amount = getCircleAmount(v1);

		for (int i = 0; i < vx.length; i++) {
			float vxAmount = getCircleAmount(vx[i]);
			if (vxAmount - v1Amount < 2 && vxAmount - v1Amount > 0) left++;
			else right++;
			//System.out.println(vxAmount+" vs. "+v1Amount);
		}

		//System.out.println("LR:"+left+"-"+right);
		// Wenn alle Punkte links oder rechts vom Strahl liegen, schneiden sie sich nicht
		if (left == 0 || right == 0) return true;
		return false;
	}

	public static Vector3d getLinePolygonIntersection(Line line, Vector3d[] points) {

		// Ein Polygon braucht mindestens 3 Punkt
		if (points.length < 3) return null;

		//System.out.println("Points("+points.length+"):"+points[0].toString()+points[1].toString()+points[2].toString()+points[3].toString());

		Plane plane = new Plane(points[0], points[1], points[2]);
		Vector3d candidate = PhysicHelper.getLinePlaneIntersection(line, plane);
		if (candidate == null) {
			//System.out.println("Kein Kandidat!");
			return null;
		}

		//System.out.println("Kandidat: " + candidate.toString()+", Punkte: "+points[0].toString()+points[1].toString()+points[2].toString());
		// Per Halbvektoren-Test wird überprüft, ob der Punkt innerhalb der Fläche liegt.
		// Der Punkt soll weit weg liegen, damit die Rechnung genau bleibt
		Vector3d pointInPlane = plane.getRandomPoint(candidate.getX() + 100000);
		//System.out.println("HALBVEKTOR: "+pointInPlane.toString());
		int intersections = 0;
		Vector3d a, b, intersection;
		Line halfVector, edge;
		for (int i = 0; i < points.length; i++) {
			a = points[i];
			if (i == points.length - 1) b = points[0];
			else b = points[i + 1];
			halfVector = new Ray(candidate, Vector3d.subtract(pointInPlane, candidate));
			edge = new LineSegment(a, Vector3d.subtract(b, a));
			intersection = PhysicHelper.getLineLineIntersection(halfVector, edge);
			if (intersection != null) {
				intersections++;
				//System.out.println("Intersection between "+halfVector.toString()+" and "+edge.toString());
			} else {
				//System.out.println("No intersection between "+halfVector.toString()+" and "+edge.toString());
			}
		}
		//System.out.println("Intersections: "+String.valueOf(intersections));
		if (intersections % 2 == 1) {
			//System.out.println("INTERSECTIONS UNGERADE!");
			return candidate;
		}
		return null;
	}

	public static Vector3d getRayAABBIntersection(Ray ray, BoundingAABB aabb) {

		Vector3d intersection = null;
		Vector3d newIntersection = null;
		Vector3d[][] faces = aabb.getFaces();
		double distance = Double.MAX_VALUE;
		double newDistance = 0;

		/*System.out.println("Ray: "+ray.toString());
		for (int i = 0; i < faces.length; i++) {
			System.out.print("Face["+i+"]: ");
			for (Vector3d face : faces[i]) {
				System.out.print(face.toString());
			}
			System.out.println();
		}*/

		for (int i = 0; i < faces.length; i++) {
			newIntersection = getLinePolygonIntersection(ray, faces[i]);
			if (newIntersection != null) {
				//System.out.print("Intersection between: " + ray.toString() + " and ");
				for (Vector3d face : faces[i]) {
					//System.out.print(face.toString());
				}
				//System.out.println();
				newDistance = Vector3d.subtract(newIntersection, ray.getStart()).getValue();
				if (newDistance < distance) {
					distance = newDistance;
					intersection = newIntersection;
				}
			} else {
				//System.out.print("No intersection between: " + ray.toString() + " and ");
				for (Vector3d face : faces[i]) {
					//System.out.print(face.toString());
				}
				//System.out.println();
			}
		}

		return intersection;
	}

	/*public static boolean isRayThroughObject(Ray ray, Vector3d[] points) {

		Vector3d start = ray.getStart();
		Vector3d dir = ray.getDirection();
		
		// Alle Punkte in die Matrix des Strahlstartes verschieben
		// dadurch kann der Richtungsvektor mit den Punkten verglichen werden
		Vector3d[] normPoints = new Vector3d[points.length];
		for (int i = 0; i < points.length; i++) {
			normPoints[i] = Vector3d.subtract(points[i], start);
		}

		// XY-Vergleiche
		Vector2d dirXY = new Vector2d(dir.getX(), dir.getY());
		Vector2d[] pointsXY = new Vector2d[normPoints.length];
		for (int i = 0; i < normPoints.length; i++) {
			pointsXY[i] = new Vector2d(normPoints[i].getX(), normPoints[i].getY());
		}
		boolean separatedXY = is2dSeparated(dirXY, pointsXY);

		// XZ-Vergleiche
		Vector2d dirXZ = new Vector2d(dir.getX(), dir.getZ());
		Vector2d[] pointsXZ = new Vector2d[normPoints.length];
		for (int i = 0; i < normPoints.length; i++) {
			pointsXZ[i] = new Vector2d(normPoints[i].getX(), normPoints[i].getZ());
		}
		boolean separatedXZ = is2dSeparated(dirXZ, pointsXZ);

		// YZ-Vergleiche
		Vector2d dirYZ = new Vector2d(dir.getY(), dir.getZ());
		Vector2d[] pointsYZ = new Vector2d[normPoints.length];
		for (int i = 0; i < normPoints.length; i++) {
			pointsYZ[i] = new Vector2d(normPoints[i].getY(), normPoints[i].getZ());
		}
		boolean separatedYZ = is2dSeparated(dirYZ, pointsYZ);

		//if (!separatedXY || !separatedXZ || !separatedYZ) System.out.println("Separate-Test: "+separatedXY+"-"+separatedXZ+"-"+separatedYZ);
		if (separatedXY || separatedXZ || separatedYZ)
			return false;
		return true;
	}*/

	public ArrayList<Entity> getEntitiesInRay(Ray ray) {

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
						System.out.println("id: "+grid[ri][rj].get(k));
						BoundingRender.renderAABB(new BoundingAABB(new Vector3d(ri * GRIDSIZE - shift.x, 0, rj * GRIDSIZE - shift.z), new Vector3d((ri + 1) * GRIDSIZE - shift.x, 10, (rj + 1) * GRIDSIZE - shift.z)), 0xffff00);
					}
				//}
			}
		}

		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < ids.size(); i++) {
			Entity entity = game.world.entityList.entities.get(ids.get(i));
			BoundingAABB aabb = entity.getAABB();
			//System.out.println("AABB: " + aabb.a.toString() + "-" + aabb.b.toString());
			//System.out.println("Pos: " + entity.matrix.position);
			if (getRayAABBIntersection(ray, entity.getAABB()) != null) entities.add(entity);
		}

		if (entities.size() > 0) System.out.println(entities.size());
		return entities;

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
