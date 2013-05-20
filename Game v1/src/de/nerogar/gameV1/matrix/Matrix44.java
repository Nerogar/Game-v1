package de.nerogar.gameV1.matrix;

public class Matrix44 extends Matrix {

	public Matrix44(float[] data) {
		super(4, 4, data);
	}
	
	public Matrix44() {
		super(4, 4);
	}
	
	public Matrix44 multiply(Matrix44 m) {
		return new Matrix44(new float[]{(data[0]*m.data[0] + data[1]*m.data[4] + data[2]*m.data[8] + data[3]*m.data[12]),
				(data[0]*m.data[1] + data[1]*m.data[5] + data[2]*m.data[9] + data[3]*m.data[13]),
				(data[0]*m.data[2] + data[1]*m.data[6] + data[2]*m.data[10] + data[3]*m.data[14]),
				(data[0]*m.data[3] + data[1]*m.data[7] + data[2]*m.data[11] + data[3]*m.data[15]),
				
				(data[4]*m.data[0] + data[5]*m.data[4] + data[6]*m.data[8] + data[7]*m.data[12]),
				(data[4]*m.data[1] + data[5]*m.data[5] + data[6]*m.data[9] + data[7]*m.data[13]),
				(data[4]*m.data[2] + data[5]*m.data[6] + data[6]*m.data[10] + data[7]*m.data[14]),
				(data[4]*m.data[3] + data[5]*m.data[7] + data[6]*m.data[11] + data[7]*m.data[15]),
				
				(data[8]*m.data[0] + data[9]*m.data[4] + data[10]*m.data[8] + data[11]*m.data[12]),
				(data[8]*m.data[1] + data[9]*m.data[5] + data[10]*m.data[9] + data[11]*m.data[13]),
				(data[8]*m.data[2] + data[9]*m.data[6] + data[10]*m.data[10] + data[11]*m.data[14]),
				(data[8]*m.data[3] + data[9]*m.data[7] + data[10]*m.data[11] + data[11]*m.data[15]),
				
				(data[12]*m.data[0] + data[13]*m.data[4] + data[14]*m.data[8] + data[15]*m.data[12]),
				(data[12]*m.data[1] + data[13]*m.data[5] + data[14]*m.data[9] + data[15]*m.data[13]),
				(data[12]*m.data[2] + data[13]*m.data[6] + data[14]*m.data[10] + data[15]*m.data[14]),
				(data[12]*m.data[3] + data[13]*m.data[7] + data[14]*m.data[11] + data[15]*m.data[15])});
	}
	
	public Matrix41 multiply(Matrix41 m) {
		return new Matrix41(new float[]{(data[0]*m.data[0] + data[1]*m.data[1] + data[2]*m.data[2] + data[3]*m.data[3]),
				(data[4]*m.data[0] + data[5]*m.data[1] + data[6]*m.data[2] + data[7]*m.data[3]),
				(data[8]*m.data[0] + data[9]*m.data[1] + data[10]*m.data[2] + data[11]*m.data[3]),
				(data[12]*m.data[0] + data[13]*m.data[1] + data[14]*m.data[2] + data[15]*m.data[3])});
	}

	@Override
	public Matrix clone() {
		return new Matrix44(data);
	}

}
