package de.nerogar.gameV1.graphics;

import de.nerogar.gameV1.Vector3d;

public class Lightning {
	private static Vector3d halfVector = new Vector3d(0.5, 0.5, 0.5);

	public Vector3d start;
	public Vector3d end;
	public double startTime;
	public double lifeTime;

	public Vector3d[] vertices;

	public boolean dead;

	public Lightning(Vector3d start, Vector3d end, double lifeTime) {
		this.start = start;
		this.end = end;
		this.lifeTime = lifeTime;

		float amplifier = (float) (Math.random() * 1.2f);

		Vector3d direction = Vector3d.subtract(end, start);
		vertices = new Vector3d[(int) direction.getValue()+ 2];
		direction.multiply(1f / vertices.length);

		vertices[0] = start;

		for (int i = 1; i < vertices.length; i++) {
			Vector3d dirMult = Vector3d.multiply(direction, i);
			vertices[i] = dirMult.add(start).add((new Vector3d(Math.random(), Math.random(), Math.random())).subtract(halfVector).multiply(amplifier));
		}
	}

	public void update(double time) {
		if (time > startTime + lifeTime) {
			dead = true;
		}
	}
}
