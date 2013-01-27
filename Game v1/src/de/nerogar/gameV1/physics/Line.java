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
	
}
