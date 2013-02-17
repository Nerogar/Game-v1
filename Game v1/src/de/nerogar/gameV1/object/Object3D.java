package de.nerogar.gameV1.object;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.image.TextureBank;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class Object3D {

	public float[] verticesQuads, normalsQuads, texCoordsQuads, verticesTriangles, normalsTriangles, texCoordsTriangles;
	public FloatBuffer vertexDataQuads, normalDataQuads, textureDataQuads, vertexDataTriangles, normalDataTriangles, textureDataTriangles;
	//public String texture;

	int vboVertexHandleQ, vboNormalHandleQ, vboTextureHandleQ, vboVertexHandleT, vboNormalHandleT, vboTextureHandleT;

	public Object3D() {

	}

	public void updateVbo() {
		vertexDataQuads = BufferUtils.createFloatBuffer(verticesQuads.length);
		vertexDataQuads.put(verticesQuads);
		vertexDataQuads.flip();

		normalDataQuads = BufferUtils.createFloatBuffer(normalsQuads.length);
		normalDataQuads.put(normalsQuads);
		normalDataQuads.flip();

		textureDataQuads = BufferUtils.createFloatBuffer(texCoordsQuads.length);
		textureDataQuads.put(texCoordsQuads);
		textureDataQuads.flip();

		vertexDataTriangles = BufferUtils.createFloatBuffer(verticesTriangles.length);
		vertexDataTriangles.put(verticesTriangles);
		vertexDataTriangles.flip();

		normalDataTriangles = BufferUtils.createFloatBuffer(normalsTriangles.length);
		normalDataTriangles.put(normalsTriangles);
		normalDataTriangles.flip();

		textureDataTriangles = BufferUtils.createFloatBuffer(texCoordsTriangles.length);
		textureDataTriangles.put(texCoordsTriangles);
		textureDataTriangles.flip();

		vboVertexHandleQ = glGenBuffers();
		vboNormalHandleQ = glGenBuffers();
		vboTextureHandleQ = glGenBuffers();

		vboVertexHandleT = glGenBuffers();
		vboNormalHandleT = glGenBuffers();
		vboTextureHandleT = glGenBuffers();

		if (verticesQuads.length > 0) {
			glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandleQ);
			glBufferData(GL_ARRAY_BUFFER, vertexDataQuads, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandleQ);
			glBufferData(GL_ARRAY_BUFFER, normalDataQuads, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandleQ);
			glBufferData(GL_ARRAY_BUFFER, textureDataQuads, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}

		if (verticesTriangles.length > 0) {

			glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandleT);
			glBufferData(GL_ARRAY_BUFFER, vertexDataTriangles, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandleT);
			glBufferData(GL_ARRAY_BUFFER, normalDataTriangles, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandleT);
			glBufferData(GL_ARRAY_BUFFER, textureDataTriangles, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}

	public void render(ObjectMatrix om, String texture) {
		render(om, texture, 1f);
	}
	
	public void render(ObjectMatrix om, String texture, float opacity) {
		//float x, float y, float z) {

		TextureBank.instance.bindTexture(texture);

		RenderHelper.enableAlpha();
		glPushMatrix();
		glColor4f(1f, 1f, 1f, opacity);
		glTranslatef(om.getPosition().getXf(), om.getPosition().getYf(), om.getPosition().getZf());
		glRotatef(om.getRotation().getZf(), 0, 1, 0);
		glScalef(om.getScaling().getXf(), om.getScaling().getYf(), om.getScaling().getZf());

		//render quads
		if (verticesQuads.length > 0) {
			glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandleQ);
			glVertexPointer(3, GL_FLOAT, 0, 0L);

			glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandleQ);
			glTexCoordPointer(2, GL_FLOAT, 0, 0L);

			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			glDrawArrays(GL_QUADS, 0, verticesQuads.length / 3);
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			glDisableClientState(GL_VERTEX_ARRAY);
		}

		//render triangles
		if (verticesTriangles.length > 0) {

			glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandleT);
			glVertexPointer(3, GL_FLOAT, 0, 0L);

			glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandleT);
			glTexCoordPointer(2, GL_FLOAT, 0, 0L);

			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			glDrawArrays(GL_TRIANGLES, 0, verticesTriangles.length / 3);
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			glDisableClientState(GL_VERTEX_ARRAY);
		}

		glPopMatrix();
		RenderHelper.disableAlpha();
	}
}