package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.matrix.MatrixHelperR3;
import de.nerogar.gameV1.physics.Line;

public class Bone {

	public Bone parent;

	public Vector3d relPosition;
	public Vector3d relScaling;
	public Vector3d relRotation;

	private Line locXAxis;
	private Line locYAxis;
	private Line locZAxis;

	public Bone(Bone parent, Vector3d pos, Vector3d sca, Vector3d rot) {
		this.parent = parent;
		this.relPosition = pos;
		this.relScaling = sca;
		this.relRotation = rot;
	}

	public void updateBone() {
		updateLocalAxes();
	}

	public void updateLocalAxes() {
		if (parent == null) {
			locXAxis = new Line(relPosition.clone(), new Vector3d(1, 0, 0));
			locYAxis = new Line(relPosition.clone(), new Vector3d(0, 1, 0));
			locZAxis = new Line(relPosition.clone(), new Vector3d(0, 0, 1));
		} else {
			//parent.updateLocalAxes();
			Vector3d x1 = parent.locXAxis.getStart();
			Vector3d x2 = Vector3d.add(x1, parent.locXAxis.getDirection());
			Vector3d y1 = parent.locYAxis.getStart();
			Vector3d y2 = Vector3d.add(y1, parent.locYAxis.getDirection());
			Vector3d z1 = parent.locZAxis.getStart();
			Vector3d z2 = Vector3d.add(z1, parent.locZAxis.getDirection());

			x1.add(relPosition);
			x2.add(relPosition);

			y1.add(relPosition);
			y2.add(relPosition);

			z1.add(relPosition);
			z2.add(relPosition);
			
			x1 = MatrixHelperR3.rotateAt(x1, parent.locXAxis, relRotation.getXf());
			x2 = MatrixHelperR3.rotateAt(x2, parent.locXAxis, relRotation.getXf());
			x1 = MatrixHelperR3.rotateAt(x1, parent.locYAxis, relRotation.getYf());
			x2 = MatrixHelperR3.rotateAt(x2, parent.locYAxis, relRotation.getYf());
			x1 = MatrixHelperR3.rotateAt(x1, parent.locZAxis, relRotation.getZf());
			x2 = MatrixHelperR3.rotateAt(x2, parent.locZAxis, relRotation.getZf());

			y1 = MatrixHelperR3.rotateAt(y1, parent.locXAxis, relRotation.getXf());
			y2 = MatrixHelperR3.rotateAt(y2, parent.locXAxis, relRotation.getXf());
			y1 = MatrixHelperR3.rotateAt(y1, parent.locYAxis, relRotation.getYf());
			y2 = MatrixHelperR3.rotateAt(y2, parent.locYAxis, relRotation.getYf());
			y1 = MatrixHelperR3.rotateAt(y1, parent.locZAxis, relRotation.getZf());
			y2 = MatrixHelperR3.rotateAt(y2, parent.locZAxis, relRotation.getZf());

			z1 = MatrixHelperR3.rotateAt(z1, parent.locXAxis, relRotation.getXf());
			z2 = MatrixHelperR3.rotateAt(z2, parent.locXAxis, relRotation.getXf());
			z1 = MatrixHelperR3.rotateAt(z1, parent.locYAxis, relRotation.getYf());
			z2 = MatrixHelperR3.rotateAt(z2, parent.locYAxis, relRotation.getYf());
			z1 = MatrixHelperR3.rotateAt(z1, parent.locZAxis, relRotation.getZf());
			z2 = MatrixHelperR3.rotateAt(z2, parent.locZAxis, relRotation.getZf());

			locXAxis = new Line(x1, Vector3d.subtract(x2, x1));
			locYAxis = new Line(y1, Vector3d.subtract(y2, y1));
			locZAxis = new Line(z1, Vector3d.subtract(z2, z1));
		}
	}

	public Vector3d translate(Vector3d v) {
		Vector3d vNew = v.clone();
		if (parent != null) {
			vNew = parent.translate(vNew);
			/*vNew.add(relPosition);
			vNew = MatrixHelperR3.rotateAt(vNew, locXAxis, relRotation.getXf());
			vNew = MatrixHelperR3.rotateAt(vNew, locYAxis, relRotation.getYf());
			vNew = MatrixHelperR3.rotateAt(vNew, locZAxis, relRotation.getZf());
		} else {
			vNew.add(relPosition);
			vNew = MatrixHelperR3.rotateAt(vNew, locXAxis, relRotation.getXf());
			vNew = MatrixHelperR3.rotateAt(vNew, locYAxis, relRotation.getYf());
			vNew = MatrixHelperR3.rotateAt(vNew, locZAxis, relRotation.getZf());*/
		}
		vNew.add(relPosition);
		vNew = MatrixHelperR3.rotateAt(vNew, locXAxis, relRotation.getXf());
		vNew = MatrixHelperR3.rotateAt(vNew, locYAxis, relRotation.getYf());
		vNew = MatrixHelperR3.rotateAt(vNew, locZAxis, relRotation.getZf());
		return vNew;
	}

	/*public void translateAbsolute(Vector3d pos, Vector3d sca, Vector3d rot) {

		position = relPosition;
		scaling = relScaling;
		rotation = relRotation;

		position.add(pos);
		scaling.add(sca);
		rotation.add(rot);

		Matrix scaleM = sca.toMatrix();
		position = Matrix.multiply(scaleM, position.toMatrix()).toVector3d();
		scaling = Matrix.multiply(scaleM, scaling.toMatrix()).toVector3d();
		rotation = Matrix.multiply(scaleM, rotation.toMatrix()).toVector3d();

		//locXAxis = MatrixHelperR3.rotate(new Vector3d(1,0,0), new Vector3d(), );
		//locXAxis = MatrixHelperR3.rotateAt(new Vector3d(1,0,0), new Line(pos,new Vector3d(1,0,0)), rot.getX());

		//position = MatrixHelperR3.rotateAt(position, rotLine, rot.getX());

	}*/

	public void renderBone() {
		//if (parent != null) RenderHelper.drawLine(translate(new Vector3d(0, 0, 0)), translate(relPosition), 0xffffffff);
		if (parent != null) {
			RenderHelper.drawLine(parent.translate(parent.relPosition), parent.translate(relPosition), 0xffffffff);
		}
		RenderHelper.drawLine(locXAxis, 0xff0000ff);
		RenderHelper.drawLine(locYAxis, 0x00ff00ff);
		RenderHelper.drawLine(locZAxis, 0x0000ffff);
		//RenderHelper.drawLine(new Vector3d(-10, 5, -10), new Vector3d(10, 15, 10), 0xffffffff);
		//RenderHelper.drawTriangle(new Vector3d(-10, 5, -10), new Vector3d(10, 10, -10), new Vector3d(10, 15, 10), 0xffffffff);
	}

}
