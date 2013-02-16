package de.nerogar.gameV1.object;

import static org.lwjgl.opengl.GL11.*;

import de.nerogar.gameV1.Camera;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.image.TextureBank;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class ObjectSprite extends Object3D {
	float size;
	Camera camera;

	public ObjectSprite(float size, Camera camera) {
		this.size = size;
		this.camera = camera;
	}

	public void render(ObjectMatrix om, String texture) {
		TextureBank.instance.bindTexture(texture);

		glPushMatrix();
		glColor3f(1f, 1f, 1f);
		glTranslatef(om.getPosition().getXf(), om.getPosition().getYf(), om.getPosition().getZf());
		glScalef(om.getScaling().getXf(), om.getScaling().getYf(), om.getScaling().getZf());

		Vector3d dir = Vector3d.subtract(camera.getCamPosition(), om.position);
		float rot = (float) Math.atan(dir.getXf() / dir.getZf());
		rot *= 180 / Math.PI;
		if (dir.getZ() < 0) rot += 180;

		glRotatef(rot, 0, 1, 0);

		//render quads
		glBegin(GL_QUADS);
		glTexCoord2f(1, 1);
		glVertex3f(1, 0, 0);
		glTexCoord2f(1, 0);
		glVertex3f(1, 2, 0);
		glTexCoord2f(0, 0);
		glVertex3f(-1, 2, 0);
		glTexCoord2f(0, 1);
		glVertex3f(-1, 0, 0);

		glEnd();

		glPopMatrix();
	}
}
