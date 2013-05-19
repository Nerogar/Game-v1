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

	public double length;

	public final ObjectMatrix defaults;
	public ObjectMatrix relative;

	//public final Vector3d defPosition, defScaling, defRotation;
	//public Vector3d relPosition, relScaling, relRotation;

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

	public Bone(Bone parent, double length) {
		this(parent, length, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
	}

	public Bone(Bone parent, double length, Vector3d pos, Vector3d sca, Vector3d rot) {
		this.parent = parent;
		this.length = length;
		this.defaults = new ObjectMatrix(pos, rot, sca);
		this.relative = new ObjectMatrix();
		update();
		transPos = (parent == null) ? locXAxis.getStart().clone() : parent.locXAxis.getStart().clone();
	}

	public void update() {
		if (isUpdated()) return;
		if (parent != null) if (!parent.isUpdated()) parent.update();
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
		rotationMatrixX = MatrixHelperR3.getRotationMatrix((parent == null) ? locXAxis.getDirection() : parent.locXAxis.getDirection(), relative.rotation.getXf());
		rotationMatrixY = MatrixHelperR3.getRotationMatrix((parent == null) ? locYAxis.getDirection() : parent.locYAxis.getDirection(), relative.rotation.getYf());
		rotationMatrixZ = MatrixHelperR3.getRotationMatrix((parent == null) ? locZAxis.getDirection() : parent.locZAxis.getDirection(), relative.rotation.getZf());

		rotationMatrixXwithDefault = MatrixHelperR3.getRotationMatrix((parent == null) ? locXAxis.getDirection() : parent.locXAxis.getDirection(), defaults.rotation.getXf() + relative.rotation.getXf());
		rotationMatrixYwithDefault = MatrixHelperR3.getRotationMatrix((parent == null) ? locYAxis.getDirection() : parent.locYAxis.getDirection(), defaults.rotation.getYf() + relative.rotation.getYf());
		rotationMatrixZwithDefault = MatrixHelperR3.getRotationMatrix((parent == null) ? locZAxis.getDirection() : parent.locZAxis.getDirection(), defaults.rotation.getZf() + relative.rotation.getZf());
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

			Vector3d defPlusRel = Vector3d.add(defaults.position, relative.position);
			locXAxis = new Line(defPlusRel, x);
			locYAxis = new Line(defPlusRel, y);
			locZAxis = new Line(defPlusRel, z);
		} else {
			Vector3d o = transformate(new Vector3d(length, 0, 0), true);
			Vector3d x = transformate(new Vector3d(length + 1, 0, 0), true);
			Vector3d y = transformate(new Vector3d(length, 1, 0), true);
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

	public Vector3d transformate(Vector3d v) {
		return transformate(v, false);
	}

	public Vector3d transformate(Vector3d v, boolean withDefault) {
		Vector3d vNew = v.clone();
		Vector3d position = withDefault ? Vector3d.add(relative.position, defaults.position) : relative.position;
		Vector3d scaling = withDefault ? Vector3d.multiply(relative.scaling, defaults.scaling) : relative.scaling;
		Vector3d posMulSca = Vector3d.multiply(position, scaling);
		if (parent != null) vNew = parent.transformate(vNew, true);
		if (parent != null) {
			vNew.add(Vector3d.multiply(parent.locXAxis.getDirection(), posMulSca.getX() + parent.length * (parent.relative.scaling.getX() * (withDefault ? parent.defaults.scaling.getX() : 1))));
			vNew.add(Vector3d.multiply(parent.locYAxis.getDirection(), posMulSca.getY()));
			vNew.add(Vector3d.multiply(parent.locZAxis.getDirection(), posMulSca.getZ()));
		} else {
			vNew.add(posMulSca);
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
			RenderHelper.drawPoint(Vector3d.add(relative.position, defaults.position), 0xbbdd44ff, 15);
		}
		RenderHelper.drawLine(locXAxis, 0xff0000ff, 1);
		RenderHelper.drawLine(locYAxis, 0x00ff00ff, 1);
		RenderHelper.drawLine(locZAxis, 0x0000ffff, 1);
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
