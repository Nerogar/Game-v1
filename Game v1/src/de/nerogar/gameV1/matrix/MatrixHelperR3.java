package de.nerogar.gameV1.matrix;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.Line;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class MatrixHelperR3 {

	// gegen den Uhrzeigersinn, wenn v nach "oben" zeigt
	// (im Uhrzeigersinn, wenn Blickrichtung = v)
	public static Matrix getRotationMatrixAtVector(Vector3d v, float alpha) {
		Matrix m = new Matrix(3, 3);

		m.set(0, 0, MathHelper.cos(alpha) + v.getXf() * v.getXf() * (1 - MathHelper.cos(alpha)));
		m.set(0, 1, v.getYf() * v.getXf() * (1 - MathHelper.cos(alpha)) - v.getZf() * MathHelper.sin(alpha));
		m.set(0, 2, v.getXf() * v.getZf() * (1 - MathHelper.cos(alpha)) + v.getYf() * MathHelper.sin(alpha));

		m.set(1, 0, v.getYf() * v.getXf() * (1 - MathHelper.cos(alpha)) + v.getZf() * MathHelper.sin(alpha));
		m.set(1, 1, MathHelper.cos(alpha) + v.getYf() * v.getYf() * (1 - MathHelper.cos(alpha)));
		m.set(1, 2, v.getYf() * v.getZf() * (1 - MathHelper.cos(alpha)) - v.getXf() * MathHelper.sin(alpha));

		m.set(2, 0, v.getZf() * v.getXf() * (1 - MathHelper.cos(alpha)) - v.getYf() * MathHelper.sin(alpha));
		m.set(2, 1, v.getZf() * v.getYf() * (1 - MathHelper.cos(alpha)) + v.getXf() * MathHelper.sin(alpha));
		m.set(2, 2, MathHelper.cos(alpha) + v.getZf() * v.getZf() * (1 - MathHelper.cos(alpha)));

		return m;
	}
	
	public static Matrix getTransformationMatrix(ObjectMatrix o) {
		return getTransformationMatrix(o.scaling, o.rotation, o.position);
	}
	
	public static Matrix getTransformationMatrix(Vector3d scale, Vector3d rot, Vector3d pos) {
		return getTransformationMatrix(scale.getXf(), scale.getYf(), scale.getZf(), rot.getXf(), rot.getYf(), rot.getZf(), pos.getXf(), pos.getYf(), pos.getZf());
	}
	
	public static Matrix getTransformationMatrix(float scaleX, float scaleY, float scaleZ, float rotX, float rotY, float rotZ, float posX, float posY, float posZ) {
		Matrix scale = MatrixHelperR3.getScaleMatrix(scaleX, scaleY, scaleZ);
		Matrix rotation = MatrixHelperR3.getRotationMatrix(rotX, rotY, rotZ);
		Matrix translation = MatrixHelperR3.getTransposeMatrix(posX, posY, posZ);
		return Matrix.multiply(translation, Matrix.multiply(rotation, scale));
	}

	public static Matrix getScaleMatrix(float sx, float sy, float sz) {
		Matrix m = new Matrix(4, 4);
		m.set(0, 0, sx);
		m.set(1, 1, sy);
		m.set(2, 2, sz);
		m.set(3, 3, 1);
		return m;
	}

	public static Matrix getTransposeMatrix(float tx, float ty, float tz) {
		Matrix m = new Matrix(4, 4);
		m.set(0, 0, 1);
		m.set(1, 1, 1);
		m.set(2, 2, 1);
		m.set(3, 3, 1);
		m.set(0, 3, tx);
		m.set(1, 3, ty);
		m.set(2, 3, tz);
		return m;
	}

	// Euler XYZ = XZY here (because OpenGL has Y upwards)
	public static Matrix getRotationMatrix(float rx, float ry, float rz) {
		return Matrix.multiply(getRotationMatrixY(ry), Matrix.multiply(getRotationMatrixZ(rz), getRotationMatrixX(rx)));
	}

	public static Matrix getRotationMatrixX(float alpha) {
		Matrix m = new Matrix(4, 4);
		m.set(0, 0, 1);
		m.set(1, 1, MathHelper.cos(alpha));
		m.set(1, 2, -MathHelper.sin(alpha));
		m.set(2, 1, MathHelper.sin(alpha));
		m.set(2, 2, MathHelper.cos(alpha));
		m.set(3, 3, 1);
		return m;
	}

	public static Matrix getRotationMatrixY(float alpha) {
		Matrix m = new Matrix(4, 4);
		m.set(0, 0, MathHelper.cos(alpha));
		m.set(0, 2, MathHelper.sin(alpha));
		m.set(1, 1, 1);
		m.set(2, 0, -MathHelper.sin(alpha));
		m.set(2, 2, MathHelper.cos(alpha));
		m.set(3, 3, 1);
		return m;
	}

	public static Matrix getRotationMatrixZ(float alpha) {
		Matrix m = new Matrix(4, 4);
		m.set(0, 0, MathHelper.cos(alpha));
		m.set(0, 1, -MathHelper.sin(alpha));
		m.set(1, 0, MathHelper.sin(alpha));
		m.set(1, 1, MathHelper.cos(alpha));
		m.set(2, 2, 1);
		m.set(3, 3, 1);
		return m;
	}

	public static Vector3d applyRotation(Matrix matrix, Vector3d v) {
		return Matrix.multiply(matrix, v.toMatrix()).toVector3d();
	}

	public static Vector3d applyRotationAt(Matrix matrix, Vector3d v, Vector3d offset) {
		return applyRotation(matrix, Vector3d.subtract(v, offset)).add(offset);
	}

	public static Vector3d rotateAt(Vector3d v, Line rotLine, float alpha) {
		//System.out.println("rotating "+v.toString()+" at "+rotLine.toString()+" by "+alpha);
		return rotate(Vector3d.subtract(v, rotLine.getStart()), rotLine.getDirection(), alpha).add(rotLine.getStart());
	}

	public static Vector3d rotate(Vector3d v, Vector3d vRot, float alpha) {
		Vector3d vNew = null;
		vNew = Matrix.multiply(getRotationMatrixAtVector(vRot, alpha), v.toMatrix()).toVector3d();
		return vNew;
	}

}
