package de.nerogar.gameV1.graphics;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public abstract class RenderScene {

	private boolean initialized;

	public int width, height;

	public int framebufferID, colorTextureID, depthTextureID;

	public RenderScene() {

	}

	public RenderScene(int width, int height) {
		setResolution(width, height);
	}

	public void setResolution(int width, int height) {
		this.width = width;
		this.height = height;

		framebufferID = glGenFramebuffersEXT();
		colorTextureID = glGenTextures();
		depthTextureID = glGenTextures();

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		glBindTexture(GL_TEXTURE_2D, colorTextureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, colorTextureID, 0);

		glBindTexture(GL_TEXTURE_2D, depthTextureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (java.nio.ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, depthTextureID, 0);

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		initialized = true;
	}

	public void renderScene(double time) {
		if (!initialized) throw new IllegalStateException("RenderScene not initialized");

		glViewport(0, 0, width, height);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		render(time);

		glEnable(GL_TEXTURE_2D);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glViewport(0, 0, width, height);
		glLoadIdentity();
	}

	public abstract void render(double time);

}
