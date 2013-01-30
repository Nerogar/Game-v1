package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.EntityPhysic;

public final class PhysicHelper {

	public static boolean isColliding(Bounding b1, Bounding b2) {

		if (b1 instanceof BoundingCircle && b2 instanceof BoundingCircle) return isCircleCollidingCircle((BoundingCircle) b1, (BoundingCircle) b2);

		if (b1 instanceof BoundingAABB && b2 instanceof BoundingAABB) return isAABBCollidingAABB((BoundingAABB) b1, (BoundingAABB) b2);

		if (b1 instanceof BoundingCircle && b2 instanceof BoundingAABB) return isCircleCollidingAABB((BoundingCircle) b1, (BoundingAABB) b2);

		if (b1 instanceof BoundingAABB && b2 instanceof BoundingCircle) return isCircleCollidingAABB((BoundingCircle) b2, (BoundingAABB) b1);

		if (b1 instanceof BoundingOBB && b2 instanceof BoundingOBB) return isOBBCollidingOBB((BoundingOBB) b1, (BoundingOBB) b2);

		// isCircleCollidingOBB fehlt
		// isAABBCollidingOBB fehlt

		return false;
	}

	public static Vector3d getLineLineIntersection(Line line1, Line line2) {

		if (line1.isParallelTo(line2)) return null;

		Vector3d s1 = line1.getStart();
		Vector3d s2 = line2.getStart();
		Vector3d d1 = line1.getDirection();
		Vector3d d2 = line2.getDirection();

		double xs1 = s1.getX();
		double xs2 = s2.getX();
		double xd1 = d1.getX();
		double xd2 = d2.getX();
		double ys1 = s1.getY();
		double ys2 = s2.getY();
		double yd1 = d1.getY();
		double yd2 = d2.getY();
		double zs1 = s1.getZ();
		double zs2 = s2.getZ();
		double zd1 = d1.getZ();
		double zd2 = d2.getZ();
		double sxy = 0, sxz = 0, syz = 0;

		double nenner1 = xd2 * yd1 - xd1 * yd2;
		//System.out.println("Nenner1: "+String.valueOf(nenner1));
		if (nenner1 != 0) sxy = -1 * (xd1 * ys1 - xd1 * ys2 - yd1 * xs1 + yd1 * xs2) / nenner1;
		//System.out.println("sxy: "+String.valueOf(sxy));
		if (line2 instanceof Ray) {
			if (sxy < 0) return null;
			if (line2 instanceof LineSegment && sxy >= 1) return null;
		}

		double nenner2 = xd2 * zd1 - xd1 * zd2;
		//System.out.println("Nenner2: "+String.valueOf(nenner2));
		if (nenner2 != 0) sxz = -1 * (xd1 * zs1 - xd1 * zs2 - zd1 * xs1 + zd1 * xs2) / nenner2;
		//System.out.println("sxz: "+String.valueOf(sxz));
		if (line2 instanceof Ray) {
			if (sxz < 0) return null;
			if (line2 instanceof LineSegment && sxz >= 1) return null;
		}

		double nenner3 = yd2 * zd1 - yd1 * zd2;
		//System.out.println("Nenner3: "+String.valueOf(nenner3));
		if (nenner3 != 0) syz = -1 * (yd1 * zs1 - yd1 * zs2 - zd1 * ys1 + zd1 * ys2) / nenner3;
		//System.out.println("syz: "+String.valueOf(syz));
		if (line2 instanceof Ray) {
			if (syz < 0) return null;
			if (line2 instanceof LineSegment && syz >= 1) return null;
		}

		double value = 0;
		value = (nenner1 == 0) ? value : sxy;
		value = (nenner2 == 0) ? value : sxz;
		value = (nenner3 == 0) ? value : syz;
		sxy = (nenner1 == 0) ? value : sxy;
		sxz = (nenner2 == 0) ? value : sxz;
		syz = (nenner3 == 0) ? value : syz;

		//Falls die Geraden identisch sind, keinen Punkt zurückgeben (Justins Vorschlag)
		// Fall sollte nicht mehr auftreten, da Parallelität zu Beginn ausgeschlossen werden sollte
		if (value == 0) return null;

		double tolerance = 0.0000000001;
		//System.out.println("sxy: " + sxy + " sxz: " + sxz + " syz: " + syz);
		if (Math.abs(sxy - sxz) > tolerance || Math.abs(sxz - syz) > tolerance) return null;

		Vector3d candidate = Vector3d.add(s2, Vector3d.multiply(d2, sxy));
		if (line1 instanceof Ray) {
			double r;
			Vector3d dirToCandidate = Vector3d.subtract(candidate, s1);
			if (d1.getX() != 0) r = dirToCandidate.getX() / d1.getX();
			else if (d1.getY() != 0) r = dirToCandidate.getY() / d1.getY();
			else if (d1.getZ() != 0) r = dirToCandidate.getZ() / d1.getZ();
			else return null;

			//System.out.println("r: " + String.valueOf(r));
			if (r < 0) return null;
			if (r >= 1 && line1 instanceof LineSegment) return null;
		}

		return candidate;

	}

	public static Vector3d getLinePlaneIntersection(Line line, Plane plane) {
		Vector3d start = line.getStart();
		Vector3d direction = line.getDirection();
		double a = plane.getA();
		double b = plane.getB();
		double c = plane.getC();
		double d = plane.getD();

		double nenner = a * direction.getX() + b * direction.getY() + c * direction.getZ();
		if (nenner == 0) {
			//System.out.println("RayPlane Parallel!");
			return null;
		}
		double t = (d - a * start.getX() - b * start.getY() - c * start.getZ()) / nenner;
		if (line instanceof Ray) {
			if (t < 0) return null;
			if (line instanceof LineSegment && t >= 1) return null;
		}
		return Vector3d.add(start, Vector3d.multiply(direction, t));
	}

	public static boolean isCircleCollidingCircle(BoundingCircle b1, BoundingCircle b2) {

		Vector3d dist = new Vector3d(b1.M);
		dist.subtract(b2.M);

		if (MathHelper.abs(dist.getValue()) < b1.r + b2.r) return true;
		return false;

	}

	public static boolean isAABBCollidingAABB(BoundingAABB b1, BoundingAABB b2) {
		if (b1.b.getX() < b2.a.getX() || b1.b.getY() < b2.a.getY() || b1.b.getZ() < b2.a.getZ() || b1.a.getX() > b2.b.getX() || b1.a.getY() > b2.b.getY() || b1.a.getZ() > b2.b.getZ()) return false;

		else return true;

	}

	public static boolean isCircleCollidingAABB(BoundingCircle b1, BoundingAABB b2) {

		// Scheint verbugt zu sein

		boolean col = true;

		if (!isColliding(b1, AABBtoCircle(b2))) col = false;
		if (col) if (!isColliding(CircletoAABB(b1), b2)) col = false;

		return col;

	}

	public static boolean isOBBCollidingOBB(BoundingOBB b1, BoundingOBB b2) {

		boolean col = false;

		//if (!isColliding(OBBtoCircle(b1), OBBtoCircle(b2))) col = false;

		for (int i = 0; i < 8; i++) {
			if (PointInOBB(b1.p[i], b2)) {
				col = true;
				break;
			}
		}
		if (!col) for (int i = 0; i < 8; i++) {
			if (PointInOBB(b2.p[i], b1)) {
				col = true;
				break;
			}
		}

		return col;

	}

	//public static boolean isColliding(Bounding b1, Bounding b2) {
	//	return false;
	//}

	public static BoundingCircle AABBtoCircle(BoundingAABB b) {

		Vector3d CM = new Vector3d(b.a);
		CM.add(b.b);
		CM.multiply(0.5F);

		Vector3d tmp = new Vector3d(b.b);
		tmp.subtract(b.a);
		float Cr = (float) MathHelper.abs(tmp.getValue()) * 0.5F;

		return new BoundingCircle(CM, Cr);

	}

	public static BoundingCircle OBBtoCircle(BoundingOBB b) {
		return AABBtoCircle(new BoundingAABB(b.p[0], b.p[6]));
	}

	public static BoundingAABB CircletoAABB(BoundingCircle b) {
		Vector3d A = Vector3d.add(b.M, -b.r);
		Vector3d B = Vector3d.add(b.M, +b.r);
		return new BoundingAABB(A, B);
	}

	public static BoundingAABB OBBtoAABB(BoundingOBB b) {

		Vector3d min = new Vector3d(0, 0, 0);
		Vector3d max = new Vector3d(0, 0, 0);

		for (int i = 0; i < 8; i++) {
			if (b.p[i].getX() < min.getX()) min.setX(b.p[i].getX());
			if (b.p[i].getY() < min.getY()) min.setY(b.p[i].getY());
			if (b.p[i].getZ() < min.getZ()) min.setZ(b.p[i].getZ());
			if (b.p[i].getX() > max.getX()) max.setX(b.p[i].getX());
			if (b.p[i].getY() > max.getY()) max.setY(b.p[i].getY());
			if (b.p[i].getZ() > max.getZ()) max.setZ(b.p[i].getZ());
		}

		return new BoundingAABB(min, max);
	}

	public static double PointInOBBHelp(Vector3d p, Vector3d p1, Vector3d p2, Vector3d p3) {
		Vector3d help1 = Vector3d.subtract(p1, p2);
		Vector3d help2 = Vector3d.subtract(p3, p2);
		help1.crossProduct(help2);
		help2 = Vector3d.subtract(p, p2);
		return help1.dotProduct(help2);
	}

	public static boolean PointInOBB(Vector3d p, BoundingOBB b) {

		boolean col = true;

		// Plane unten
		double dot = PointInOBBHelp(p, b.p[2], b.p[1], b.p[3]);
		if (dot >= 0) col = false;

		// Plane oben
		if (col) {
			dot = PointInOBBHelp(p, b.p[7], b.p[5], b.p[6]);
			if (dot >= 0) col = false;
		}

		// Plane vorne
		if (col) {
			dot = PointInOBBHelp(p, b.p[7], b.p[3], b.p[4]);
			if (dot >= 0) col = false;
		}

		// Plane hinten
		if (col) {
			dot = PointInOBBHelp(p, b.p[5], b.p[1], b.p[2]);
			if (dot >= 0) col = false;
		}

		// Plane rechts
		if (col) {
			dot = PointInOBBHelp(p, b.p[6], b.p[2], b.p[3]);
			if (dot >= 0) col = false;
		}

		// Plane hinten
		if (col) {
			dot = PointInOBBHelp(p, b.p[4], b.p[1], b.p[5]);
			if (dot >= 0) col = false;
		}

		return col;

	}

	public static BoundingAABB toAABB(Bounding b) {

		if (b instanceof BoundingAABB) return (BoundingAABB) b;
		if (b instanceof BoundingCircle) return (BoundingAABB) CircletoAABB((BoundingCircle) b);
		if (b instanceof BoundingOBB) return (BoundingAABB) OBBtoAABB((BoundingOBB) b);

		return new BoundingAABB(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0));
	}

	// Circle-OBB fehlt
	// AABB-OBB fehlt

	// Collections fehlen

	public static void collisionReply(EntityPhysic e1, EntityPhysic e2) {

		// nichts...

	}

}
