package de.nerogar.gameV1.matrix;

import de.nerogar.gameV1.Vector3d;

public class Matrix {

	private float[] data;
	private int rows = 0;
	private int cols = 0;

	public static final Matrix uniformMatrix4 = new Matrix(4, 4, new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });

	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		data = new float[rows * cols];
		clear();
	}

	public Matrix(int rows, int cols, float[] data) {
		this.rows = rows;
		this.cols = cols;
		this.data = data;
	}

	private void clear() {
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
	}

	public double get(int row, int col) {
		return data[row * cols + col];
	}

	public void set(int row, int col, float val) {
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
				float sum = 0;
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
		if (getRows() < 3 || getCols() != 1) return null;
		return new Vector3d(get(0, 0), get(1, 0), get(2, 0));
	}
	
	public static Matrix multiply(Matrix m, float r) {
		return m.clone().multiply(r);
	}

	public Matrix multiply(float r) {
		for (int i = 0; i < data.length; i++)
			data[i] *= r;
		return this;
	}

	public Matrix clone() {
		return new Matrix(rows, cols, data);
	}

	@Override
	public String toString() {
		String str = "----- " + rows + "x" + cols + " Matrix -----\n";
		float[] dataRounded = data.clone();
		int maxLength = 0;
		for (int i = 0; i < dataRounded.length; i++) {
			dataRounded[i] = Math.round(dataRounded[i] * 1000) / 1000f;
			maxLength = Math.max(String.valueOf(dataRounded[i]).length(), maxLength);
		}
		for (int i = 0; i < rows; i++) {
			str += "| ";
			for (int j = 0; j < cols; j++) {
				str += String.valueOf(dataRounded[i * cols + j]);
				for (int k = 0; k < maxLength - String.valueOf(dataRounded[i * cols + j]).length() + 1; k++)
					str += " ";
			}
			str += "|\n";
		}
		str += "----- End of Matrix -----";
		return str;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Matrix)) return false;
		Matrix m = (Matrix) o;
		if (m.rows != rows) return false;
		if (m.cols != cols) return false;
		for (int i = 0; i < data.length; i++)
			if (data[i] != m.data[i]) return false;
		return true;
	}

}
