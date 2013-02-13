package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector2d;
import de.nerogar.gameV1.Vector3d;

public class GeometryHelper {

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

		double tolerance = 0.0000000001;
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
	
	public static Vector3d getLinePolygonIntersection(Line line, Vector3d[] points) {

		// Ein Polygon braucht mindestens 3 Punkte
		if (points.length < 3) return null;

		Plane plane = new Plane(points[0], points[1], points[2]);
		Vector3d candidate = GeometryHelper.getLinePlaneIntersection(line, plane);
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
			intersection = GeometryHelper.getLineLineIntersection(halfVector, edge);
			if (intersection != null) {
				intersections++;
			}
		}
		if (intersections % 2 == 1) return candidate;
		return null;
	}
	
	public static Vector2d getDirVector(float rot) {
		//Vector2d vector = null;
		//Vector2d zeroDegrees = new Vector2d(0, -1);
		//cos(rotation) = vector.x*0 + vector.z*-1;
		// Grenzwerte aus Faulheit eliminieren
		if (rot == 90 || rot == 270) rot += .00001f;
		float newY = (float) (-1*MathHelper.cos(rot));	
		return new Vector2d(0, newY);
	}
	
}
