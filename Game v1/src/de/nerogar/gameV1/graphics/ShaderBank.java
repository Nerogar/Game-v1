package de.nerogar.gameV1.graphics;

import java.util.*;

public class ShaderBank {
	private HashMap<String, Shader> shaders;

	public static ShaderBank instance = new ShaderBank();

	public ShaderBank() {
		shaders = new HashMap<String, Shader>();
	}

	public void createShaderProgramm(String name) {
		if (shaders.get(name) == null) {
			Shader newShader = new Shader(name);

			shaders.put(name, newShader);
		}
	}

	public Shader getShader(String name) {
		return shaders.get(name);
	}

	public void activateShader(String name) {
		Shader shader = shaders.get(name);
		if (shader != null) {
			shader.activate();
		}
	}
	
	public void deactivateShader(String name){
		
	}
}
