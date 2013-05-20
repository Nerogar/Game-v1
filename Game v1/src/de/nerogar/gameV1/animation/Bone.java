package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.matrix.Matrix;
import de.nerogar.gameV1.matrix.MatrixHelperR3;
import de.nerogar.gameV1.physics.Line;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class Bone {

	public static final int NO_BONE = -1;

	public Bone parent;

	public float length;

	public final ObjectMatrix defaults;
	public ObjectMatrix relative;

	public final Vector3d transPos;

	private boolean updated;

	private Matrix localTransformationMatrix = new Matrix(4, 4);
	private Matrix localTransformationMatrixWithDefault = new Matrix(4, 4);
	private Matrix globalTransformationMatrix = new Matrix(4, 4);
	private Matrix globalTransformationMatrixWithDefault = new Matrix(4, 4);

	public Matrix spaceTransfo;

	public Bone(Bone parent, float length) {
		this(parent, length, null);
	}

	public Bone(Bone parent, float length, Matrix space) {
		this(parent, length, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0), space);
	}

	public Bone(Bone parent, float length, Vector3d pos, Vector3d sca, Vector3d rot) {
		this(parent, length, pos, sca, rot, null);
	}

	public Bone(Bone parent, float length, Vector3d pos, Vector3d sca, Vector3d rot, Matrix space) {
		this.parent = parent;
		this.length = length;
		this.defaults = new ObjectMatrix(pos, rot, sca);
		this.relative = new ObjectMatrix();
		this.spaceTransfo = space;
		update();
		transPos = transform(new Vector3d(0, 0, 0), true);
	}

	private void updateLocalTransformationMatrix(boolean withDefault) {
		ObjectMatrix summed = relative.clone();
		float l = (parent == null) ? 0 : parent.length;

		if (withDefault) {
			summed.position.add(defaults.position);
			summed.rotation.add(defaults.rotation);
			summed.scaling.multiply(defaults.scaling);
		}

		Matrix transformation = MatrixHelperR3.getTransformationMatrix(summed);
		Matrix lengthTranslation = MatrixHelperR3.getTransposeMatrix(l, 0, 0);

		if (withDefault)
			localTransformationMatrixWithDefault = Matrix.multiply(lengthTranslation, transformation);
		else
			localTransformationMatrix = Matrix.multiply(lengthTranslation, transformation);
	}

	public void update() {
		if (isUpdated()) return;
		if (parent != null) if (!parent.isUpdated()) parent.update();
		updateLocalTransformationMatrix(false);
		updateLocalTransformationMatrix(true);
		globalTransformationMatrix = (parent == null) ? Matrix.multiply(spaceTransfo, localTransformationMatrix) : Matrix.multiply(parent.globalTransformationMatrix, localTransformationMatrix);
		globalTransformationMatrixWithDefault = (parent == null) ? Matrix.multiply(spaceTransfo, localTransformationMatrixWithDefault) : Matrix.multiply(parent.globalTransformationMatrixWithDefault, localTransformationMatrixWithDefault);
		setUpdated(true);
	}

	public boolean isUpdated() {
		return updated;
	}

	private void setUpdated(boolean b) {
		updated = b;
	}

	public Matrix getGlobalTransformationMatrix() {
		return Matrix.multiply(globalTransformationMatrix, MatrixHelperR3.getTransposeMatrix(-transPos.getXf(), -transPos.getYf(), -transPos.getZf()));
	}

	public Vector3d transformGlobal(Vector3d v) {
		return transform(Vector3d.subtract(v, transPos));
	}

	public Vector3d transform(Vector3d v) {
		return transform(v, false);
	}

	public Vector3d transform(Vector3d v, boolean withDefault) {
		return Matrix.multiply(withDefault ? globalTransformationMatrixWithDefault : globalTransformationMatrix, v.toMatrix4()).toVector3d();
	}

	public void renderBone() {
		if (parent != null) {
			//RenderHelper.drawLine(locXAxis.getStart(), parent.locXAxis.getStart(), 0xffffffff, 5);
			RenderHelper.drawLine(transform(new Vector3d(length, 0, 0), true), transform(new Vector3d(0, 0, 0), true), 0xffffffff, 5);
		} else {
			RenderHelper.drawPoint(transform(new Vector3d(0, 0, 0)), 0xbbdd44ff, 15);
		}
		Vector3d o = transform(new Vector3d(0, 0, 0), true);
		Vector3d x = transform(new Vector3d(1, 0, 0), true);
		Vector3d y = transform(new Vector3d(0, 1, 0), true);
		Vector3d z = transform(new Vector3d(0, 0, 1), true);
		RenderHelper.drawLine(new Line(o, Vector3d.subtract(x, o)), 0xff0000ff, 1);
		RenderHelper.drawLine(new Line(o, Vector3d.subtract(y, o)), 0x00ff00ff, 1);
		RenderHelper.drawLine(new Line(o, Vector3d.subtract(z, o)), 0x0000ffff, 1);
	}

	public void markDirty() {
		setUpdated(false);
	}

	public void set(ObjectMatrix values) {
		relative.position.set(values.position);
		relative.rotation.set(values.rotation);
		relative.scaling.set(values.scaling);
	}

}
