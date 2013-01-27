package de.nerogar.gameV1.level;

public class Position {

	public int x;
	public int z;

	public Position(int x, int z) {
		this.x = x;
		this.z = z;
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
		return "Position X: " + x + " | Z: " + z;
	}
}
