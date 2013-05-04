package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.matrix.Matrix;
import de.nerogar.gameV1.matrix.MatrixHelperR3;
import de.nerogar.gameV1.physics.Line;

public class Bone {

	public Bone parent;

	public Vector3d relPosition;
	public Vector3d relScaling;
	public Vector3d relRotation;

	private Line locXAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(1, 0, 0));
	private Line locYAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(0, 1, 0));
	private Line locZAxis = new Line(new Vector3d(0, 0, 0), new Vector3d(0, 0, 1));

	private boolean updated;
	private Matrix rotationMatrixX = new Matrix(3, 3);
	private Matrix rotationMatrixY = new Matrix(3, 3);
	private Matrix rotationMatrixZ = new Matrix(3, 3);

	public Bone(Bone parent, Vector3d pos, Vector3d sca, Vector3d rot) {
		this.parent = parent;
		this.relPosition = pos;
		this.relScaling = sca;
		this.relRotation = rot;
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
			locXAxis = new Line(relPosition.clone(), new Vector3d(1, 0, 0));
			locYAxis = new Line(relPosition.clone(), new Vector3d(0, 1, 0));
			locZAxis = new Line(relPosition.clone(), new Vector3d(0, 0, 1));
		} else {
			Vector3d o = translate(new Vector3d(0, 0, 0));
			Vector3d x = translate(new Vector3d(1, 0, 0));
			Vector3d y = translate(new Vector3d(0, 1, 0));
			Vector3d z = translate(new Vector3d(0, 0, 1));
			//Vector3d z = Vector3d.crossProduct(x, y);
			//System.out.println(Vector3d.dotProduct(x, y)+" "+Vector3d.dotProduct(x, z)+" "+Vector3d.dotProduct(y, z));
			locXAxis = new Line(o, Vector3d.subtract(x, o).normalize());
			locYAxis = new Line(o, Vector3d.subtract(y, o).normalize());
			locZAxis = new Line(o, Vector3d.subtract(z, o).normalize());
		}
	}

	public Vector3d translate(Vector3d v) {
		Vector3d vNew = v.clone();
		if (parent != null) vNew = parent.translate(vNew);
		vNew.add(Vector3d.multiply(locXAxis.getDirection(), relPosition.getX() * relScaling.getX()));
		vNew.add(Vector3d.multiply(locYAxis.getDirection(), relPosition.getY() * relScaling.getY()));
		vNew.add(Vector3d.multiply(locZAxis.getDirection(), relPosition.getZ() * relScaling.getZ()));
		vNew = MatrixHelperR3.applyRotationAt(rotationMatrixX, vNew, (parent == null) ? locXAxis.getStart() : parent.locXAxis.getStart());
		vNew = MatrixHelperR3.applyRotationAt(rotationMatrixY, vNew, (parent == null) ? locYAxis.getStart() : parent.locYAxis.getStart());
		vNew = MatrixHelperR3.applyRotationAt(rotationMatrixZ, vNew, (parent == null) ? locZAxis.getStart() : parent.locZAxis.getStart());
		return vNew;
	}

	public void renderBone() {
		if (parent != null) {
			RenderHelper.drawLine(translate(new Vector3d(0, 0, 0)), parent.translate(new Vector3d(0, 0, 0)), 0xffffffff, 5);
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

}
