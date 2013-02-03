package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.Vector3d;

public class Line {

	private Vector3d start;
	private Vector3d direction;

	public Line(Vector3d start, Vector3d direction) {
		this.setStart(start);
		this.setDirection(direction);
	}

	public Vector3d getDirection() {
		return direction;
	}

	public void setDirection(Vector3d direction) {
		this.direction = direction;
	}

	public Vector3d getStart() {
		return start;
	}

	public void setStart(Vector3d start) {
		this.start = start;
	}

	public boolean isParallelTo(Line ray) {
		if (this.getDirection().isParallelTo(ray.getDirection())) return true;
		return false;
	}

	public String toString() {
		return this.start.toString() + ">" + this.direction.toString();
	}

	public Line getLine() {
		return new Line(this.getStart(), this.getDirection());
	}

	public Ray getRay() {
		return new Ray(this.getStart(), this.getDirection());
	}

	public LineSegment getLineSegment() {
		return new LineSegment(this.getStart(), this.getDirection());
	}

	// isxyz: 0=x 1=y 2=z
	public Double getR(double xyz, byte isxyz) {
		// y = y1 + r*y2
		// r = (y - y1) / y2
		double r = 0;
		if (direction.getX() != 0 && isxyz == 0)
			r = (xyz - start.getX()) / direction.getX();
		else if (direction.getY() != 0 && isxyz == 1)
			r = (xyz - start.getY()) / direction.getY();
		else if (direction.getZ() != 0 && isxyz == 2)
			r = (xyz - start.getZ()) / direction.getZ();
		else
			return null;
		if (r < 0) {
			//System.out.println("r<0; xyz:" + xyz + "; isxyz:" + isxyz);
		}
		return r;
	}

	public Double getXatY(double xyz) {
		Double r = getR(xyz, (byte) 1);
		if (r == null) return null;
		if (this instanceof Ray) {
			if (r < 0) return null;
			if (this instanceof LineSegment && r >= 1) return null;
		}
		return start.getX() + direction.getX() * r;
	}

	public Double getXatZ(double xyz) {
		Double r = getR(xyz, (byte) 2);
		if (r == null) return null;
		if (this instanceof Ray) {
			if (r < 0) return null;
			if (this instanceof LineSegment && r >= 1) return null;
		}
		return start.getX() + direction.getX() * r;
	}

	public Double getYatX(double xyz) {
		Double r = getR(xyz, (byte) 0);
		if (r == null) return null;
		if (this instanceof Ray) {
			if (r < 0) return null;
			if (this instanceof LineSegment && r >= 1) return null;
		}
		return start.getY() + direction.getY() * r;
	}

	public Double getYatZ(double xyz) {
		Double r = getR(xyz, (byte) 2);
		if (r == null) return null;
		if (this instanceof Ray) {
			if (r < 0) {
				//System.out.println("r="+r+" at z" + xyz);
				return null;
			}
			if (this instanceof LineSegment && r >= 1) return null;
		}
		return start.getY() + direction.getY() * r;
	}

	public Double getZatX(double xyz) {
		Double r = getR(xyz, (byte) 0);
		if (r == null) return null;
		if (this instanceof Ray) {
			if (r < 0) return null;
			if (this instanceof LineSegment && r >= 1) return null;
		}
		return start.getZ() + direction.getZ() * r;
	}

	public Double getZatY(double xyz) {
		Double r = getR(xyz, (byte) 1);
		if (r == null) return null;
		if (this instanceof Ray) {
			if (r < 0) return null;
			if (this instanceof LineSegment && r >= 1) return null;
		}
		return start.getZ() + direction.getZ() * r;
	}

	/*public Double getX(Vector3d pos) {
		// y = y1 + r*y2
		// r = (y - y1) / y2
		double r = 0;
		if (direction.getY() != 0) r = (pos.getY() - start.getY()) / direction.getY();
		else if (direction.getZ() != 0) r = (pos.getZ() - start.getZ()) / direction.getZ();
		else return null;
		// Keinen Schnittpunkt zurückgeben in Sonderfällen von Strahl und Strecke
		if (this instanceof Ray) {
			if (r < 0) return null;
			if (this instanceof LineSegment && r >= 1) return null;
		}
		// x = x1 + r*x2
		return start.getX() + direction.getX()*r;
	}
	
	public Double getY(Vector3d pos) {
		double r = 0;
		if (direction.getX() != 0 && pos.getX() != 0) r = (pos.getX() - start.getX()) / direction.getX();
		else if (direction.getZ() != 0 && pos.getZ() != 0) r = (pos.getZ() - start.getZ()) / direction.getZ();
		else return null;
		if (this instanceof Ray) {
			if (r < 0) return null;
			if (this instanceof LineSegment && r >= 1) return null;
		}
		return start.getY() + direction.getY()*r;
	}
	
	public Double getZ(Vector3d pos) {
		double r = 0;
		if (direction.getX() != 0) r = (pos.getX() - start.getX()) / direction.getX();
		else if (direction.getY() != 0) r = (pos.getY() - start.getY()) / direction.getY();
		else return null;
		if (this instanceof Ray) {
			if (r < 0) return null;
			if (this instanceof LineSegment && r >= 1) return null;
		}
		return start.getZ() + direction.getZ()*r;
	}*/

}
