package de.nerogar.gameV1.graphics;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import de.nerogar.gameV1.RenderEngine;

public class RenderSceneLightningMask extends RenderScene {

	private RenderSceneContainer sceneContainer;
	public Shader maskShader;

	public RenderSceneLightningMask(RenderSceneContainer sceneContainer) {
		this.sceneContainer = sceneContainer;
		setupShader();
	}

	private void setupShader() {
		maskShader = new Shader("lightningBlur");
		maskShader.setVertexShader("res/shaders/lightning/maskShader.vert");
		maskShader.setFragmentShader("res/shaders/lightning/maskShader.frag");
		maskShader.compile();
	}

	private void activateShader() {
		maskShader.activate();

		TextureBank.instance.bindTexture(sceneContainer.world.depthTextureID, 0);
		TextureBank.instance.bindTexture(sceneContainer.lightningEffectContainer.depthTextureID, 1);
		TextureBank.instance.bindTexture(sceneContainer.lightningEffectContainer.colorTextureID, 2);

		glUniform1i(glGetUniformLocation(maskShader.shaderHandle, "worldDepthTex"), 0);
		glUniform1i(glGetUniformLocation(maskShader.shaderHandle, "effectDepthTex"), 1);
		glUniform1i(glGetUniformLocation(maskShader.shaderHandle, "effectColorTex"), 2);
	}

	public void deactivateShader() {
		maskShader.deactivate();
	}

	@Override
	public void render(double time) {
		activateShader();

		RenderEngine.instance.setOrtho();

		sceneContainer.drawQuad(time);

		deactivateShader();

	}

}
