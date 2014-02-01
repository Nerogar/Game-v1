package de.nerogar.gameV1;

import de.nerogar.gameV1.level.Position;

public class Vector2d {

	public double x;
	public double z;

	public Vector2d(double x, double z) {
		this.x = x;
		this.z = z;
	}

	public double getX() {
		return this.x;
	}

	public double getZ() {
		return this.z;
	}

	public float getXf() {
		return (float) this.getX();
	}

	public float getZf() {
		return (float) this.getZ();
	}

	public Vector2d add(Vector2d v2) {
		x += v2.getX();
		z += v2.getZ();
		return this;
	}

	public static Vector2d add(Vector2d v1, Vector2d v2) {
		return new Vector2d(v1.getX() + v2.getX(), v1.getZ() + v2.getZ());
	}

	public Vector2d subtract(Vector2d v2) {
		x -= v2.getX();
		z -= v2.getZ();
		return this;
	}

	public static Vector2d subtract(Vector2d v1, Vector2d v2) {
		return new Vector2d(v1.getX() - v2.getX(), v1.getZ() - v2.getZ());
	}

	public Vector2d multiply(double v) {
		x *= v;
		z *= v;
		return this;
	}

	public static Vector2d multiply(Vector2d v1, double v) {
		return new Vector2d(v1.getX() * v, v1.getZ() * v);
	}

	public Vector2d multiply(float v) {
		x *= v;
		z *= v;
		return this;
	}

	public static Vector2d multiply(Vector2d v1, float v) {
		return new Vector2d(v1.getX() * v, v1.getZ() * v);
	}

	public Vector2d normalize() {
		double value = getValue();
		this.x /= value;
		this.z /= value;
		return this;
	}

	public static Vector2d normalize(Vector2d v) {
		Vector2d v2 = v.clone();
		v2.normalize();
		return v2;
	}

	public double getValue() {
		return Math.sqrt(x * x + z * z);
	}

	@Override
	public Vector2d clone() {
		return new Vector2d(this.x, this.z);
	}

	public static double dotProduct(Vector2d v1, Vector2d v2) {
		return v1.x * v2.x + v1.z * v2.z;
	}

	public String toString() {
		return "(" + Double.toString(this.getX()) + "," + Double.toString(this.getZ()) + ")";
	}

	public Position toPosition() {
		return new Position((int) x, (int) z);
	}

	public Vector3d toVector3d() {
		return new Vector3d(x, 0, z);
	}
}
