package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.matrix.Matrix;
import de.nerogar.gameV1.matrix.MatrixHelperR3;
import de.nerogar.gameV1.physics.Line;

public class Bone {

	public Bone parent;

	public double length;
	
	public Vector3d defPosition, defScaling, defRotation;
	public Vector3d relPosition, relScaling, relRotation;

	private Line locXAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(1, 0, 0));
	private Line locYAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(0, 1, 0));
	private Line locZAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(0, 0, 1));

	private boolean updated;
	private Matrix rotationMatrixX = new Matrix(3, 3);
	private Matrix rotationMatrixY = new Matrix(3, 3);
	private Matrix rotationMatrixZ = new Matrix(3, 3);

	public Bone(Bone parent, double length, Vector3d pos, Vector3d sca, Vector3d rot) {
		this.parent = parent;
		this.length = length;
		this.defPosition = pos;
		this.defScaling = sca;
		this.defRotation = rot;
		setToDefault();
	}

	private void setToDefault() {
		relPosition = defPosition;
		relScaling = defScaling;
		relRotation = defRotation;
	}

	public void updateBone() {
		if (parent != null) if (!parent.isUpdated()) parent.updateBone();
		updateLocalAxes();
		updateRotationMatrix();
		setUpdated(true);
	}

	private void updateRotationMatrix() {
		rotationMatrixX = MatrixHelperR3.getRotationMatrix((parent == null) ? locXAxis.getDirection() : parent.locXAxis.getDirection(), relRotation.getXf());
		rotationMatrixY = MatrixHelperR3.getRotationMatrix((parent == null) ? locYAxis.getDirection() : parent.locYAxis.getDirection(), relRotation.getYf());
		rotationMatrixZ = MatrixHelperR3.getRotationMatrix((parent == null) ? locZAxis.getDirection() : parent.locZAxis.getDirection(), relRotation.getZf());
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

			/*
			Matrix rotX = MatrixHelperR3.getRotationMatrix(x, relRotation.getXf());
			Matrix rotY = MatrixHelperR3.getRotationMatrix(y, relRotation.getYf());
			Matrix rotZ = MatrixHelperR3.getRotationMatrix(z, relRotation.getZf());
			
			x = MatrixHelperR3.applyRotation(rotZ, x);
			x = MatrixHelperR3.applyRotation(rotY, x);
			
			y = MatrixHelperR3.applyRotation(rotX, y);
			y = MatrixHelperR3.applyRotation(rotZ, y);
			y = MatrixHelperR3.applyRotation(rotY, y);
			
			z = MatrixHelperR3.applyRotation(rotX, z);
			z = MatrixHelperR3.applyRotation(rotZ, z);
			z = MatrixHelperR3.applyRotation(rotY, z);
			*/

			locXAxis = new Line(relPosition.clone(), x);
			locYAxis = new Line(relPosition.clone(), y);
			locZAxis = new Line(relPosition.clone(), z);
		} else {
			Vector3d o = translate(new Vector3d(length, 0, 0));
			Vector3d x = translate(new Vector3d(length+1, 0, 0));
			Vector3d y = translate(new Vector3d(length, 1, 0));
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
		Vector3d vNew = v.clone();
		if (parent != null) vNew = parent.translate(vNew);
		if (parent != null) {
			vNew.add(Vector3d.multiply(parent.locXAxis.getDirection(), relPosition.getX() * relScaling.getX() + parent.length * parent.relScaling.getX()));
			vNew.add(Vector3d.multiply(parent.locYAxis.getDirection(), relPosition.getY() * relScaling.getY()));
			vNew.add(Vector3d.multiply(parent.locZAxis.getDirection(), relPosition.getZ() * relScaling.getZ()));
		} else {
			vNew.addX(relPosition.getX() * relScaling.getX());
			vNew.addY(relPosition.getY() * relScaling.getY());
			vNew.addZ(relPosition.getZ() * relScaling.getZ());
		}
		vNew = MatrixHelperR3.applyRotationAt(rotationMatrixX, vNew, (parent == null) ? locXAxis.getStart() : parent.locXAxis.getStart());
		vNew = MatrixHelperR3.applyRotationAt(rotationMatrixZ, vNew, (parent == null) ? locZAxis.getStart() : parent.locZAxis.getStart());
		vNew = MatrixHelperR3.applyRotationAt(rotationMatrixY, vNew, (parent == null) ? locYAxis.getStart() : parent.locYAxis.getStart());
		return vNew;
	}

	public void renderBone() {
		if (parent != null) {
			//RenderHelper.drawLine(parent.translate(relPosition), parent.translate(new Vector3d(0, 0, 0)), 0xffffffff, 5);
			RenderHelper.drawLine(locXAxis.getStart(), parent.locXAxis.getStart(), 0xffffffff, 5);
		} else {
			RenderHelper.drawPoint(relPosition, 0xbbdd44ff, 15);
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
