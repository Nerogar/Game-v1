package de.nerogar.gameV1.matrix;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.Line;

public class MatrixHelper {

	public static Vector3d rotateAtR3(Vector3d v, Line rotLine, float alpha) {
		return rotateR3(v.subtract(rotLine.getStart()), rotLine.getDirection(), alpha).add(rotLine.getStart());
	}
	
	public static Vector3d rotateR3(Vector3d v, Vector3d vRot, float alpha) {
		Vector3d vNew = null;
		try {
			vNew = Matrix.multiply(getRotationMatrixR3(vRot, alpha), v.toMatrix()).toVector3d();
		} catch (MatrixMultiplicationException e) {
			e.printStackTrace();
		}
		return vNew;
	}
	
	// gegen den Uhrzeigersinn, wenn v nach "oben" zeigt
	// (im Uhrzeigersinn, wenn Blickrichtung = v)
	public static Matrix getRotationMatrixR3(Vector3d v, float alpha) {
		Matrix m = new Matrix(3, 3);
		
		m.set(0, 0, MathHelper.cos(alpha) + v.getX() * v.getX() * (1 - MathHelper.cos(alpha)));
		m.set(0, 1, v.getY() * v.getX() * (1 - MathHelper.cos(alpha)) - v.getZ() * MathHelper.sin(alpha));
		m.set(0, 2, v.getX() * v.getZ() * (1 - MathHelper.cos(alpha)) + v.getY() * MathHelper.sin(alpha));
		
		m.set(1, 0, v.getY() * v.getX() * (1 - MathHelper.cos(alpha)) + v.getZ() * MathHelper.sin(alpha));
		m.set(1, 1, MathHelper.cos(alpha) + v.getY() * v.getY() * (1 - MathHelper.cos(alpha)));
		m.set(1, 2, v.getY() * v.getZ() * (1 - MathHelper.cos(alpha)) - v.getX() * MathHelper.sin(alpha));
		
		m.set(2, 0, v.getZ() * v.getX() * (1 - MathHelper.cos(alpha)) - v.getY() * MathHelper.sin(alpha));
		m.set(2, 1, v.getZ() * v.getY() * (1 - MathHelper.cos(alpha)) + v.getX() * MathHelper.sin(alpha));
		m.set(2, 2, MathHelper.cos(alpha) + v.getZ() * v.getZ() * (1 - MathHelper.cos(alpha)));
		
		return m;
	}
	
}
