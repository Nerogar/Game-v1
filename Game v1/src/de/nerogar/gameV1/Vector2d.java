package de.nerogar.gameV1;

public class Vector2d {

	public double x;
	public double y;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d normalize() {
		double value = getValue();
		this.x /= value;
		this.y /= value;
		return this;
	}
	
	public static Vector2d normalize(Vector2d v) {
		Vector2d v2 = v.clone();
		v2.normalize();
		return v2;
	}
	
	private double getValue() {
		return Math.sqrt(x*x+y*y);
	}
	
	@Override
	public Vector2d clone() {
		return new Vector2d(this.x, this.y);
	}
	
	public static double dotProduct(Vector2d v1, Vector2d v2) {
		return v1.x*v2.x + v1.y*v2.y;
	}
	
}