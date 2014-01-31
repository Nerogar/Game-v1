package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Vector3d;

public class Position {

	public int x;
	public int z;

	public Position() {
		x = 0;
		z = 0;
	}

	public Position(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public Position(Position pos) {
		this.x = pos.x;
		this.z = pos.z;
	}

	public Position(Vector3d vec3) {
		this.x = (int) Math.floor(vec3.getX());
		this.z = (int) Math.floor(vec3.getZ());
	}

	public void add(Position newPosition) {
		x += newPosition.x;
		z += newPosition.z;
	}

	public void subtract(Position newPosition) {
		x -= newPosition.x;
		z -= newPosition.z;
	}

	public void multiply(int mult) {
		x *= mult;
		z *= mult;
	}

	public static Position add(Position pos1, Position pos2) {
		Position retPos = new Position(pos1);
		retPos.add(pos2);
		return retPos;
	}

	public static Position subtract(Position pos1, Position pos2) {
		Position retPos = new Position(pos1);
		retPos.subtract(pos2);
		return retPos;
	}

	public static Position multiply(Position pos1, int mult) {
		Position retPos = new Position(pos1);
		retPos.multiply(mult);
		return retPos;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Position) {
			Position position = (Position) object;
			if (position.x == this.x && position.z == this.z) return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "(X: " + x + "|Z: " + z + ")";
	}

	@Override
	public int hashCode() {
		return (this.x & 0xffff) | ((this.z & 0xffff) << 16);
	}
}
