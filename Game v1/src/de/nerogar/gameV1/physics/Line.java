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
		return this.start.toString()+">"+this.direction.toString();
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

	public Double getX(Vector3d pos) {
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
		if (direction.getX() != 0) r = (pos.getX() - start.getX()) / direction.getX();
		else if (direction.getZ() != 0) r = (pos.getZ() - start.getZ()) / direction.getZ();
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
	}
	
}
