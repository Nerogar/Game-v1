package de.nerogar.gameV1.matrix;

public abstract class Matrix {

	public float[] data;
	protected int rows = 0;
	protected int cols = 0;

	//public static final Matrix uniformMatrix4 = new Matrix(4, 4, new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });

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

	/*public static Matrix multiply(Matrix a, Matrix b) {
		if (a.getCols() != b.getRows()) try {
			throw new MatrixMultiplicationException("A.cols not equal to B.rows");
		} catch (MatrixMultiplicationException e) {
			e.printStackTrace();
		}
		if (a instanceof Matrix44 && b instanceof Matrix44) {
			return new Matrix44(new float[]{(a.data[0]*b.data[0] + a.data[1]*b.data[4] + a.data[2]*b.data[8] + a.data[3]*b.data[12]),
					(a.data[0]*b.data[1] + a.data[1]*b.data[5] + a.data[2]*b.data[9] + a.data[3]*b.data[13]),
					(a.data[0]*b.data[2] + a.data[1]*b.data[6] + a.data[2]*b.data[10] + a.data[3]*b.data[14]),
					(a.data[0]*b.data[3] + a.data[1]*b.data[7] + a.data[2]*b.data[11] + a.data[3]*b.data[15]),
					
					(a.data[4]*b.data[0] + a.data[5]*b.data[4] + a.data[6]*b.data[8] + a.data[7]*b.data[12]),
					(a.data[4]*b.data[1] + a.data[5]*b.data[5] + a.data[6]*b.data[9] + a.data[7]*b.data[13]),
					(a.data[4]*b.data[2] + a.data[5]*b.data[6] + a.data[6]*b.data[10] + a.data[7]*b.data[14]),
					(a.data[4]*b.data[3] + a.data[5]*b.data[7] + a.data[6]*b.data[11] + a.data[7]*b.data[15]),
					
					(a.data[8]*b.data[0] + a.data[9]*b.data[4] + a.data[10]*b.data[8] + a.data[11]*b.data[12]),
					(a.data[8]*b.data[1] + a.data[9]*b.data[5] + a.data[10]*b.data[9] + a.data[11]*b.data[13]),
					(a.data[8]*b.data[2] + a.data[9]*b.data[6] + a.data[10]*b.data[10] + a.data[11]*b.data[14]),
					(a.data[8]*b.data[3] + a.data[9]*b.data[7] + a.data[10]*b.data[11] + a.data[11]*b.data[15]),
					
					(a.data[12]*b.data[0] + a.data[13]*b.data[4] + a.data[14]*b.data[8] + a.data[15]*b.data[12]),
					(a.data[12]*b.data[1] + a.data[13]*b.data[5] + a.data[14]*b.data[9] + a.data[15]*b.data[13]),
					(a.data[12]*b.data[2] + a.data[13]*b.data[6] + a.data[14]*b.data[10] + a.data[15]*b.data[14]),
					(a.data[12]*b.data[3] + a.data[13]*b.data[7] + a.data[14]*b.data[11] + a.data[15]*b.data[15])});
		}
		if (a instanceof Matrix44 && b instanceof Matrix41) {
			return new Matrix41(new float[]{(a.data[0]*b.data[0] + a.data[1]*b.data[1] + a.data[2]*b.data[2] + a.data[3]*b.data[3]),
					(a.data[4]*b.data[0] + a.data[5]*b.data[1] + a.data[6]*b.data[2] + a.data[7]*b.data[3]),
					(a.data[8]*b.data[0] + a.data[9]*b.data[1] + a.data[10]*b.data[2] + a.data[11]*b.data[3]),
					(a.data[12]*b.data[0] + a.data[13]*b.data[1] + a.data[14]*b.data[2] + a.data[15]*b.data[3])});
		}
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
		//return null;
	}*/

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public static Matrix multiply(Matrix m, float r) {
		return m.clone().multiply(r);
	}

	public Matrix multiply(float r) {
		for (int i = 0; i < data.length; i++)
			data[i] *= r;
		return this;
	}

	public abstract Matrix clone();

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
