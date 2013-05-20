package de.nerogar.gameV1.matrix;

import de.nerogar.gameV1.Vector3d;

public class Matrix41 extends Matrix {

	public Matrix41(float[] data) {
		super(4, 1, data);
	}
	
	public Matrix41() {
		super(4, 1);
	}

	@Override
	public Matrix clone() {
		return new Matrix41(data);
	}
	
	public Vector3d toVector3d() {
		return new Vector3d(data[0], data[1], data[2]);
	}

}
