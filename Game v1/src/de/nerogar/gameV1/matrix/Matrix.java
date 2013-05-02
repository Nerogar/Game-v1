package de.nerogar.gameV1.matrix;

import de.nerogar.gameV1.Vector3d;

public class Matrix {

	private double[] data;
	private int rows = 0;
	private int cols = 0;

	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		data = new double[rows * cols];
		clear();
	}

	private void clear() {
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
	}

	public double get(int row, int col) {
		return data[row * cols + col];
	}

	public void set(int row, int col, double val) {
		data[row * cols + col] = val;
	}

	public static Matrix multiply(Matrix a, Matrix b) {
		if (a.getCols() != b.getRows()) try {
			throw new MatrixMultiplicationException("A.cols not equal to B.rows");
		} catch (MatrixMultiplicationException e) {
			e.printStackTrace();
		}
		//if (a.getCols() != b.getRows()) return null;
		Matrix m = new Matrix(a.getRows(), b.getCols());
		for (int i = 0; i < a.getRows(); i++) {
			for (int j = 0; j < b.getCols(); j++) {
				double sum = 0;
				for (int k = 0; k < a.getCols(); k++) {
					sum += a.get(i, k) * b.get(k, j);
				}
				m.set(i, j, sum);
			}
		}
		return m;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public Vector3d toVector3d() {
		if (getRows() != 3 || getCols() != 1) return null;
		return new Vector3d(get(0, 0), get(1, 0), get(2, 0));
	}

}
