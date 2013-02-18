package de.nerogar.gameV1.level;

public class Position {

	public int x;
	public int z;

	public Position(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public void add(Position newPosition) {
		x += newPosition.x;
		z += newPosition.z;
	}

	public void subtract(Position newPosition) {
		x -= newPosition.x;
		z -= newPosition.z;
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
