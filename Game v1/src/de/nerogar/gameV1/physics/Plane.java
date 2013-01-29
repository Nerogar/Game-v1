package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.Vector3d;

public class Plane {

	// Koordinatenform: ax + by + cz = d
	private double a;
	private double b;
	private double c;
	private double d;
	
	public Plane(double a, double b, double c, double d) {
		this.setA(a);
		this.setB(b);
		this.setC(c);
		this.setD(d);
	}
	
	public Plane(Vector3d A, Vector3d n) {
		this(n.getX(), n.getY(), n.getZ(), Vector3d.dotProduct(A, n));
	}
	
	public Plane(Vector3d A, Vector3d B, Vector3d C) {
		this(B, Vector3d.crossProduct(Vector3d.subtract(C, B), Vector3d.subtract(A, B)));
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}
	
	public Vector3d getRandomPoint() {
		return getRandomPoint(1);
	}
	
	public Vector3d getRandomPoint(double start) {
		double x = start;
		double y = start;
		double z = start;
		if (a != 0) {
			x = (d - b*y - c*z) / a;
		} else if (b != 0) {
			y = (d - a*x - c*z) / b; 
		} else if (c != 0) {
			z = (d - a*x - b*y) / c;
		} else return null;
		return new Vector3d(x, y, z);
	}
	
	public Vector3d getN() {
		return new Vector3d(a, b, c);
	}
	
}
