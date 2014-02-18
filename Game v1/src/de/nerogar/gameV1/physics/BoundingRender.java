package de.nerogar.gameV1.physics;

import static org.lwjgl.opengl.GL11.*;

import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.Vector3d;

public class BoundingRender {
	//private int circleDivisions = 10;
	//private float cLL = 1 / circleDivisions;

	public static void renderBall(BoundingCircle bound, int color) {
		// Hier fehlt die Kreiszeichnenfunktion anscheinend noch, Lückenfüller
		return;
	}

	// renderOBB fehlt

	public static boolean shellRenderOutlines() {
		return GameOptions.instance.getBoolOption("showAABBs") && GameOptions.instance.getBoolOption("debug");
	}

	public static void renderAABB(BoundingAABB bound, int color) {
		if (!shellRenderOutlines()) return;
		Vector3d a = bound.a;
		Vector3d b = bound.b;
		Vector3d[] v = new Vector3d[8];

		v[0] = a;
		v[1] = new Vector3d(b.getX(), a.getY(), a.getZ());
		v[2] = new Vector3d(b.getX(), b.getY(), a.getZ());
		v[3] = new Vector3d(a.getX(), b.getY(), a.getZ());

		v[4] = new Vector3d(a.getX(), a.getY(), b.getZ());
		v[5] = new Vector3d(b.getX(), a.getY(), b.getZ());
		v[6] = b;
		v[7] = new Vector3d(a.getX(), b.getY(), b.getZ());

		render8v(v, color);
	}

	public static void render8v(Vector3d[] v, int color) {
		/*
		 * 0 1
		 * 
		 * 4 5
		 * 
		 * 3 2
		 * 
		 * 7 6
		 */

		if (v.length == 8) {
			renderLine(v[0], v[1], color);
			renderLine(v[1], v[2], color);
			renderLine(v[2], v[3], color);
			renderLine(v[3], v[0], color);

			renderLine(v[4], v[5], color);
			renderLine(v[5], v[6], color);
			renderLine(v[6], v[7], color);
			renderLine(v[7], v[4], color);

			renderLine(v[0], v[4], color);
			renderLine(v[1], v[5], color);
			renderLine(v[2], v[6], color);
			renderLine(v[3], v[7], color);
		}
	}

	public static void renderLine(Vector3d v1, Vector3d v2, int color) {
		float r = ((color & 0xff0000) >> 16) / 255f;
		float g = ((color & 0x00ff00) >> 8) / 255f;
		float b = (color & 0x0000ff) / 255f;

		glDisable(GL_TEXTURE_2D);

		glColor3f(r, g, b);

		glBegin(GL_LINES);
		glVertex3f(v1.getXf(), v1.getYf(), v1.getZf());
		glVertex3f(v2.getXf(), v2.getYf(), v2.getZf());
		glEnd();

		glEnable(GL_TEXTURE_2D);

	}
}