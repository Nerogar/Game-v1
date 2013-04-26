package de.nerogar.gameV1.matrix;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.Line;

public class MatrixHelperR3 {
	
	// gegen den Uhrzeigersinn, wenn v nach "oben" zeigt
	// (im Uhrzeigersinn, wenn Blickrichtung = v)
	public static Matrix getRotationMatrix(Vector3d v, float alpha) {
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
	
	public static Matrix vectorToEMatrix(Vector3d v) {
		Matrix m = new Matrix(3, 3);
		m.set(0, 0, v.getX());
		m.set(1, 1, v.getY());
		m.set(2, 2, v.getZ());
		return m;
	}
	
	public static Matrix getScalingMatrix(Vector3d scaling) {
		return vectorToEMatrix(scaling);
	}
	
	public static Vector3d rotateAt(Vector3d v, Line rotLine, float alpha) {
		return rotate(v.subtract(rotLine.getStart()), rotLine.getDirection(), alpha).add(rotLine.getStart());
	}
	
	public static Vector3d rotate(Vector3d v, Vector3d vRot, float alpha) {
		Vector3d vNew = null;
		try {
			vNew = Matrix.multiply(getRotationMatrix(vRot, alpha), v.toMatrix()).toVector3d();
		} catch (MatrixMultiplicationException e) {
			e.printStackTrace();
		}
		return vNew;
	}
	
	public static Vector3d scale(Vector3d v, Vector3d scale) {
		Matrix m = getScalingMatrix(scale);
		try {
			m = Matrix.multiply(v.toMatrix(), m);
		} catch (MatrixMultiplicationException e) {
			e.printStackTrace();
		}
		return m.toVector3d();
	}
	
}
