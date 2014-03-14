package de.nerogar.gameV1.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

import org.lwjgl.opengl.Display;

import de.nerogar.gameV1.*;
import de.nerogar.gameV1.gui.GuiList;

public class RenderSceneContainer {

	/*
	 * render pipeline:
	 * 
	 * World:
	 *  > in world.colorTextureID
	 * Lighting:
	 *  > in lightningEffectContainer.colorTextureID
	 *  > in lightningMask to mask depth with world.depthTextureID
	 *  > in lightningBlur to blur in x direction
	 * Gui:
	 *  > in guiList.colorTextureID
	 * 
	 * Composition:
	 *  > world
	 *  > overlay lightning (blured in y)
	 *  > overlay gui
	 */

	private Shader compositionShader;

	public World world;
	public LightningEffectContainer lightningEffectContainer;
	public GuiList guiList;

	public RenderSceneLightningMask lightningMask;
	public RenderSceneLightningBlur lightningBlur;

	public RenderSceneContainer(Game game) {
		compositionShader = new Shader("composition");

		world = game.world;
		lightningEffectContainer = game.world.lightningEffectContainer;
		guiList = game.guiList;

		lightningMask = new RenderSceneLightningMask(this);
		lightningBlur = new RenderSceneLightningBlur(this);

		setupShader();
	}

	private void setupShader() {
		compositionShader.setVertexShader("res/shaders/compositionShader.vert");
		compositionShader.setFragmentShader("res/shaders/compositionShader.frag");
		compositionShader.compile();
	}

	private void activateShader() {
		compositionShader.activate();

		TextureBank.instance.bindTexture(world.colorTextureID, 0);
		//TextureBank.instance.bindTexture(lightningBlur.colorTextureID, 1);
		TextureBank.instance.bindTexture(lightningBlur.colorTextureID, 1);
		TextureBank.instance.bindTexture(guiList.colorTextureID, 2);

		glUniform1i(glGetUniformLocation(compositionShader.shaderHandle, "worldColorTex"), 0);
		glUniform1i(glGetUniformLocation(compositionShader.shaderHandle, "effectColorTex"), 1);
		glUniform1i(glGetUniformLocation(compositionShader.shaderHandle, "guiColorTex"), 2);
	}

	public void deactivateShader() {

		compositionShader.deactivate();
	}

	public void render(double time) {
		guiList.renderScene(time);

		if (world.isLoaded) {
			world.renderScene(time);
			world.lightningEffectContainer.renderScene(time);
		}

		lightningMask.renderScene(time);
		lightningBlur.renderScene(time);

		RenderEngine.instance.setOrtho();

		activateShader();

		drawQuad(time);

		deactivateShader();
	}

	public void drawQuad(double time) {
		glEnable(GL_TEXTURE_2D);

		glBegin(GL_QUADS);

		glTexCoord2f(0, 1);
		glVertex3f(0, 0, -1);

		glTexCoord2f(0, 0);
		glVertex3f(0, Display.getHeight(), -1);

		glTexCoord2f(1, 0);
		glVertex3f(Display.getWidth(), Display.getHeight(), -1);

		glTexCoord2f(1, 1);
		glVertex3f(Display.getWidth(), 0, -1);

		glEnd();
	}

	public void setSceneResolutions() {
		guiList.setResolution(RenderEngine.instance.getDisplayMode().getWidth(), RenderEngine.instance.getDisplayMode().getHeight());
		world.setResolution(RenderEngine.instance.getDisplayMode().getWidth(), RenderEngine.instance.getDisplayMode().getHeight());
		world.lightningEffectContainer.setResolution(RenderEngine.instance.getDisplayMode().getWidth(), RenderEngine.instance.getDisplayMode().getHeight());
		lightningMask.setResolution(RenderEngine.instance.getDisplayMode().getWidth(), RenderEngine.instance.getDisplayMode().getHeight());
		lightningBlur.setResolution(RenderEngine.instance.getDisplayMode().getWidth(), RenderEngine.instance.getDisplayMode().getHeight());
	}

}
