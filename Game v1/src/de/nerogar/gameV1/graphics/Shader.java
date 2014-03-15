package de.nerogar.gameV1.graphics;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.*;
import java.util.HashMap;

public class Shader {

	public int shaderHandle;
	public String name;
	public HashMap<String, Integer> uniforms;
	public HashMap<String, Integer> attributes;

	private String vertexShaderFile, vertexShader;
	private String fragmentShaderFile, fragmentShader;
	private int vertexShaderHandle, fragmentShaderHandle;
	private boolean useVertexShader, useFragmentShader;

	private boolean active;
	private boolean compiled;

	public Shader(String name) {
		this.name = name;
		uniforms = new HashMap<String, Integer>();
		attributes = new HashMap<String, Integer>();
	}

	public void setVertexShader(String filename) {
		useVertexShader = true;
		vertexShaderFile = filename;
		File tempFile = new File(filename);
		String dirPath = tempFile.getParentFile().getAbsolutePath() + File.separator;
		String newFilename = tempFile.getName();
		vertexShader = readFile(dirPath, newFilename, null);
	}

	public void setFragmentShader(String filename) {
		useFragmentShader = true;
		fragmentShaderFile = filename;
		File tempFile = new File(filename);
		String dirPath = tempFile.getParentFile().getAbsolutePath() + File.separator;
		String newFilename = tempFile.getName();
		fragmentShader = readFile(dirPath, newFilename, null);
	}

	public void compile() {
		if (compiled) {
			glDeleteProgram(shaderHandle);
			//glDeleteShader(vertexShaderHandle);
			//glDeleteShader(fragmentShaderHandle);
		}

		shaderHandle = glCreateProgram();
		if (useVertexShader) vertexShaderHandle = glCreateShader(GL_VERTEX_SHADER);
		if (useFragmentShader) fragmentShaderHandle = glCreateShader(GL_FRAGMENT_SHADER);

		boolean compileError = false;
		if (useVertexShader) {
			glShaderSource(vertexShaderHandle, vertexShader);
			glCompileShader(vertexShaderHandle);
			if (glGetShader(vertexShaderHandle, GL_COMPILE_STATUS) == GL_FALSE) {
				System.err.println("Vertex shader wasn't able to be compiled correctly. Error log:");
				System.err.println(glGetShaderInfoLog(vertexShaderHandle, 1024));
				compileError = true;
			}
		}

		if (useFragmentShader && !compileError) {
			glShaderSource(fragmentShaderHandle, fragmentShader);
			glCompileShader(fragmentShaderHandle);
			if (glGetShader(fragmentShaderHandle, GL_COMPILE_STATUS) == GL_FALSE) {
				System.err.println("Fragment shader wasn't able to be compiled correctly. Error log:");
				System.err.println(glGetShaderInfoLog(fragmentShaderHandle, 1024));
			}
		}

		if (useVertexShader && !compileError) glAttachShader(shaderHandle, vertexShaderHandle);
		if (useFragmentShader && !compileError) glAttachShader(shaderHandle, fragmentShaderHandle);

		if (!compileError) {
			glLinkProgram(shaderHandle);
			if (glGetProgram(shaderHandle, GL_LINK_STATUS) == GL_FALSE) {
				System.err.println("Shader program wasn't linked correctly.");
				System.err.println(glGetProgramInfoLog(shaderHandle, 1024));
				compileError = true;
			}
			glDeleteShader(vertexShaderHandle);
			glDeleteShader(fragmentShaderHandle);
		}

		if (!compileError) {
			compiled = true;
		}
	}

	public String readFile(String dirname, String filename, StringBuilder text) {
		if (text == null) {
			text = new StringBuilder();
		}

		try {
			BufferedReader fileReader = null;
			fileReader = new BufferedReader(new FileReader(dirname + filename));
			String line;
			while ((line = fileReader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) {
					if (line.startsWith("#insert")) {
						String[] lineSegments = line.split(" ");
						if (lineSegments.length > 1) {
							String filepath = dirname + File.separator + lineSegments[1];
							File tempFile = new File(filepath);
							String dirPath = tempFile.getParentFile().getAbsolutePath() + File.separator;
							String newFilename = tempFile.getName();
							readFile(dirPath, newFilename, text);
						}
					} else if (line.startsWith("#resolution")) {
						text.append("uniform vec2 resolution;\n");
					}
				} else {
					text.append(line).append("\n");
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("loaded shader: " + filename);

		return text.toString();
	}

	public void reloadFiles() {
		if (useVertexShader) setVertexShader(vertexShaderFile);
		if (useFragmentShader) setFragmentShader(fragmentShaderFile);
	}

	public void activate() {
		if (!active && compiled) {
			active = true;
			glUseProgram(shaderHandle);
		}
	}

	public void deactivate() {
		if (active) {
			active = false;
			glUseProgram(0);
		}
	}

	public void setResolution(float x, float y) {
		activate();

		glUniform2f(glGetUniformLocation(shaderHandle, "resolution"), x, y);

		deactivate();
	}
}
