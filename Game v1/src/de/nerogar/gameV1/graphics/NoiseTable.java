package de.nerogar.gameV1.graphics;

public class NoiseTable {
	public int[] valuesX, valuesY;
	public double[] values;
	public int size;

	public NoiseTable(int size, int range) {
		this.size = size;

		values = new double[range];
		valuesX = new int[size];
		valuesY = new int[size];
	}
}
