package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.Vector3d;

public class BoundingAABB extends Bounding {

	public Vector3d a = new Vector3d(0, 0, 0);
	public Vector3d b = new Vector3d(0, 0, 0);

	public BoundingAABB(Vector3d vA, Vector3d vB) {
		setVectors(vA,vB);
	}
	
	public BoundingAABB(BoundingAABB n) {
		setVectors(n.a, n.b);
	}
	
	public BoundingAABB() {
		
	}
	
	public void setVectors(Vector3d vA, Vector3d vB) {
		this.a = vA;
		this.b = vB;
		correctValues();
	}

	private void correctValues() {
		double help;
		if (b.getX() < a.getX()) {
			help = b.getX();
			b.setX(a.getX());
			a.setX(help);
		}
		if (b.getY() < a.getY()) {
			help = b.getY();
			b.setY(a.getY());
			a.setY(help);
		}
		if (b.getZ() < a.getZ()) {
			help = b.getZ();
			b.setZ(a.getZ());
			a.setZ(help);
		}
	}

	@Override
	public BoundingAABB clone() {
		return new BoundingAABB(a, b);
	}
	
	public Vector3d[] getPoints() {
		Vector3d[] points = new Vector3d[8];
		points[0] = new Vector3d(a);
		points[1] = new Vector3d(a.getX(), a.getY(), b.getZ());
		points[2] = new Vector3d(a.getX(), b.getY(), b.getZ());
		points[3] = new Vector3d(a.getX(), b.getY(), a.getZ());
		points[4] = new Vector3d(b.getX(), a.getY(), a.getZ());
		points[5] = new Vector3d(b.getX(), a.getY(), b.getZ());
		points[6] = new Vector3d(b);
		points[7] = new Vector3d(b.getX(), b.getY(), a.getZ());
		/*points[0] = new Vector3d(a.getX(), a.getY(), b.getZ());
		points[1] = new Vector3d(b.getX(), a.getY(), b.getZ());
		points[2] = new Vector3d(b);
		points[3] = new Vector3d(a.getX(), b.getY(), b.getZ());
		points[4] = new Vector3d(a);
		points[5] = new Vector3d(b.getX(), a.getY(), a.getZ());
		points[6] = new Vector3d(b.getX(), b.getY(), a.getZ());
		points[7] = new Vector3d(a.getX(), b.getY(), a.getZ());*/
		return points;
	}
	
	public Vector3d[][] getFaces() {
		Vector3d[] points = getPoints();
		Vector3d[][] faces = new Vector3d[6][4];
		faces[0] = new Vector3d[]{points[0], points[1], points[2], points[3]};
		faces[1] = new Vector3d[]{points[4], points[5], points[6], points[7]};
		faces[2] = new Vector3d[]{points[0], points[1], points[5], points[4]};
		faces[3] = new Vector3d[]{points[1], points[2], points[6], points[5]};
		faces[4] = new Vector3d[]{points[2], points[3], points[7], points[6]};
		faces[5] = new Vector3d[]{points[3], points[0], points[4], points[7]};
		return faces;
	}

}
