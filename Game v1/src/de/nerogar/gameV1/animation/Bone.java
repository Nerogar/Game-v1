package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.matrix.Matrix;
import de.nerogar.gameV1.matrix.MatrixHelperR3;
import de.nerogar.gameV1.physics.Line;

public class Bone {

	public static final int NO_BONE = -1;

	public Bone parent;

	public double length;

	public final Vector3d defPosition, defScaling, defRotation;
	public Vector3d relPosition, relScaling, relRotation;

	public final Vector3d transPos;

	private Line locXAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(1, 0, 0));
	private Line locYAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(0, 1, 0));
	private Line locZAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(0, 0, 1));

	private boolean updated;
	private Matrix rotationMatrixX = new Matrix(3, 3);
	private Matrix rotationMatrixY = new Matrix(3, 3);
	private Matrix rotationMatrixZ = new Matrix(3, 3);
	private Matrix rotationMatrixXwithDefault = new Matrix(3, 3);
	private Matrix rotationMatrixYwithDefault = new Matrix(3, 3);
	private Matrix rotationMatrixZwithDefault = new Matrix(3, 3);

	public Bone(Bone parent, double length, Vector3d pos, Vector3d sca, Vector3d rot) {
		this.parent = parent;
		this.length = length;
		this.defPosition = pos;
		this.defScaling = sca;
		this.defRotation = rot;
		this.relPosition = new Vector3d(0, 0, 0);
		this.relScaling = new Vector3d(1, 1, 1);
		this.relRotation = new Vector3d(0, 0, 0);
		updateBone();
		transPos = (parent == null) ? locXAxis.getStart().clone() : parent.locXAxis.getStart().clone();
	}

	public void updateBone() {
		if (parent != null) if (!parent.isUpdated()) parent.updateBone();
		if (parent == null) {
			updateLocalAxes();
			updateRotationMatrices();
		} else {
			updateRotationMatrices();
			updateLocalAxes();
		}
		setUpdated(true);
	}

	private void updateRotationMatrices() {
		rotationMatrixX = MatrixHelperR3.getRotationMatrix((parent == null) ? locXAxis.getDirection() : parent.locXAxis.getDirection(), relRotation.getXf());
		rotationMatrixY = MatrixHelperR3.getRotationMatrix((parent == null) ? locYAxis.getDirection() : parent.locYAxis.getDirection(), relRotation.getYf());
		rotationMatrixZ = MatrixHelperR3.getRotationMatrix((parent == null) ? locZAxis.getDirection() : parent.locZAxis.getDirection(), relRotation.getZf());

		rotationMatrixXwithDefault = MatrixHelperR3.getRotationMatrix((parent == null) ? locXAxis.getDirection() : parent.locXAxis.getDirection(), defRotation.getXf() + relRotation.getXf());
		rotationMatrixYwithDefault = MatrixHelperR3.getRotationMatrix((parent == null) ? locYAxis.getDirection() : parent.locYAxis.getDirection(), defRotation.getYf() + relRotation.getYf());
		rotationMatrixZwithDefault = MatrixHelperR3.getRotationMatrix((parent == null) ? locZAxis.getDirection() : parent.locZAxis.getDirection(), defRotation.getZf() + relRotation.getZf());
	}

	public boolean isUpdated() {
		return updated;
	}

	private void setUpdated(boolean b) {
		updated = b;
	}

	public void updateLocalAxes() {
		if (parent == null) {
			Vector3d x = new Vector3d(1, 0, 0);
			Vector3d y = new Vector3d(0, 1, 0);
			Vector3d z = new Vector3d(0, 0, 1);

			locXAxis = new Line(Vector3d.add(defPosition, relPosition), x);
			locYAxis = new Line(Vector3d.add(defPosition, relPosition), y);
			locZAxis = new Line(Vector3d.add(defPosition, relPosition), z);
		} else {
			Vector3d o = translate(new Vector3d(length, 0, 0), true);
			Vector3d x = translate(new Vector3d(length + 1, 0, 0), true);
			Vector3d y = translate(new Vector3d(length, 1, 0), true);
			//Vector3d z = translate(new Vector3d(0, 0, 1));
			Vector3d ox = Vector3d.subtract(x, o).normalize();
			Vector3d oy = Vector3d.subtract(y, o).normalize();
			Vector3d oz = Vector3d.crossProduct(ox, oy);
			//Vector3d oz = Vector3d.subtract(z, o).normalize();
			locXAxis = new Line(o, ox);
			locYAxis = new Line(o, oy);
			locZAxis = new Line(o, oz);
		}
	}

	public Vector3d translate(Vector3d v) {
		return translate(v, false);
	}

	public Vector3d translate(Vector3d v, boolean withDefault) {
		Vector3d vNew = v.clone();
		Vector3d position = withDefault ? Vector3d.add(relPosition, defPosition) : relPosition;
		Vector3d scaling = withDefault ? Vector3d.multiply(relScaling, defScaling) : relScaling;
		if (parent != null) vNew = parent.translate(vNew, true);
		if (parent != null) {
			vNew.add(Vector3d.multiply(parent.locXAxis.getDirection(), position.getX() * scaling.getX() + parent.length * (parent.relScaling.getX() * (withDefault ? parent.defScaling.getX() : 1))));
			vNew.add(Vector3d.multiply(parent.locYAxis.getDirection(), position.getY() * scaling.getY()));
			vNew.add(Vector3d.multiply(parent.locZAxis.getDirection(), position.getZ() * scaling.getZ()));
		} else {
			vNew.addX(position.getX() * scaling.getX());
			vNew.addY(position.getY() * scaling.getY());
			vNew.addZ(position.getZ() * scaling.getZ());
		}
		vNew = MatrixHelperR3.applyRotationAt(withDefault ? rotationMatrixXwithDefault : rotationMatrixX, vNew, (parent == null) ? locXAxis.getStart() : parent.locXAxis.getStart());
		vNew = MatrixHelperR3.applyRotationAt(withDefault ? rotationMatrixZwithDefault : rotationMatrixZ, vNew, (parent == null) ? locZAxis.getStart() : parent.locZAxis.getStart());
		vNew = MatrixHelperR3.applyRotationAt(withDefault ? rotationMatrixYwithDefault : rotationMatrixY, vNew, (parent == null) ? locYAxis.getStart() : parent.locYAxis.getStart());
		return vNew;
	}

	public void renderBone() {
		if (parent != null) {
			RenderHelper.drawLine(locXAxis.getStart(), parent.locXAxis.getStart(), 0xffffffff, 5);
			//RenderHelper.drawLine(translate(new Vector3d(0, 0, 0)), parent.translate(new Vector3d(0, 0, 0)), 0xffffffff, 5);
		} else {
			RenderHelper.drawPoint(Vector3d.add(relPosition, defPosition), 0xbbdd44ff, 15);
		}
		RenderHelper.drawLine(locXAxis, 0xff0000ff, 1);
		RenderHelper.drawLine(locYAxis, 0x00ff00ff, 1);
		RenderHelper.drawLine(locZAxis, 0x0000ffff, 1);
	}

	public void markDirty() {
		setUpdated(false);
	}

	public void setKeyframe(Keyframe keyframe) {
		relRotation.set(keyframe.rotation);
		relScaling.set(keyframe.scaling);
	}

}
