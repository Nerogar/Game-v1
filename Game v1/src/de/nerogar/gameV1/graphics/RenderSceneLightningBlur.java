package de.nerogar.gameV1.graphics;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import de.nerogar.gameV1.RenderEngine;

public class RenderSceneLightningBlur extends RenderScene {

	private RenderSceneContainer sceneContainer;
	public Shader blurShader;

	public RenderSceneLightningBlur(RenderSceneContainer sceneContainer) {
		this.sceneContainer = sceneContainer;
		setupShader();
	}

	private void setupShader() {
		blurShader = new Shader("lightningBlur");
		blurShader.setVertexShader("res/shaders/lightning/blurShader1.vert");
		blurShader.setFragmentShader("res/shaders/lightning/blurShader1.frag");
		blurShader.compile();
	}

	private void activateShader() {
		blurShader.activate();

		TextureBank.instance.bindTexture(sceneContainer.lightningMask.colorTextureID, 0);

		glUniform1i(glGetUniformLocation(blurShader.shaderHandle, "effectColorTex"), 0);
	}

	public void deactivateShader() {
		blurShader.deactivate();
	}

	@Override
	public void render(double time) {
		activateShader();

		RenderEngine.instance.setOrtho();

		sceneContainer.drawQuad(time);

		deactivateShader();

	}

}
