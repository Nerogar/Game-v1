package de.nerogar.gameV1;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.GLU;

public class RenderEngine {
	public DisplayMode[] displayModes;
	public int displayMode;
	private int height = 600;
	private int width = 800;

	public boolean fullscreen;

	public static RenderEngine instance = new RenderEngine();

	public RenderEngine() {
		width = GameOptions.instance.getIntOption("width");
		height = GameOptions.instance.getIntOption("height");

		try {
			createWindow();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		init();
	}

	public void cleanup() {
		Display.destroy();
	}

	private void createWindow() throws LWJGLException {
		DisplayMode[] modes;
		ArrayList<DisplayMode> displayModeBuffer = new ArrayList<DisplayMode>();
		fullscreen = GameOptions.instance.getBoolOption("fullscreen");

		modes = Display.getAvailableDisplayModes();
		for (DisplayMode mode : modes) {
			boolean flag1 = mode.getFrequency() == Display.getDisplayMode().getFrequency();
			boolean flag2 = mode.getBitsPerPixel() == Display.getDisplayMode().getBitsPerPixel();
			boolean flag3 = mode.getHeight() <= Display.getDesktopDisplayMode().getHeight();
			boolean flag4 = mode.getWidth() <= Display.getDesktopDisplayMode().getWidth();
			if (flag1 && flag2 && flag3 && flag4) {
				displayModeBuffer.add(mode);
			}
		}

		displayModes = new DisplayMode[displayModeBuffer.size()];
		for (int i = 0; i < displayModes.length; i++) {
			displayModes[i] = displayModeBuffer.get(i);
		}

		sortDisplayModes(displayModes);

		if (fullscreen) {
			try {
				Display.setFullscreen(fullscreen);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < displayModes.length; i++) {
			DisplayMode mode = displayModes[i];
			if (mode.getHeight() == height && mode.getWidth() == width) {
				Display.setDisplayMode(mode);
				displayMode = i;
				Display.create();
			}
		}
	}

	private void sortDisplayModes(DisplayMode[] displayModes) {
		if (displayModes.length == 0 || displayModes == null) return;

		ArrayList<DisplayMode> displayModeList = new ArrayList<DisplayMode>();
		ArrayList<Integer> widthList = new ArrayList<Integer>();
		ArrayList<Integer> heightList = new ArrayList<Integer>();
		for (DisplayMode dm : displayModes) {
			displayModeList.add(dm);
			if (widthList.indexOf(dm.getWidth()) == -1) widthList.add(dm.getWidth());
			if (heightList.indexOf(dm.getHeight()) == -1) heightList.add(dm.getHeight());
		}

		int[] widths = new int[widthList.size()];
		for (int i = 0; i < widths.length; i++) {
			int minW = 0;
			for (int j = 0; j < widthList.size(); j++) {
				if (widthList.get(j) < widthList.get(minW)) minW = j;
			}
			widths[i] = widthList.get(minW);
			widthList.remove(minW);
		}

		int[] heights = new int[heightList.size()];
		for (int i = 0; i < heights.length; i++) {
			int minH = 0;
			for (int j = 0; j < heightList.size(); j++) {
				if (heightList.get(j) < heightList.get(minH)) minH = j;
			}
			heights[i] = heightList.get(minH);
			heightList.remove(minH);
		}

		DisplayMode[][] allDisplayModes = new DisplayMode[widths.length][heights.length];
		for (int i = 0; i < widths.length; i++) {
			for (int j = 0; j < heights.length; j++) {
				for (int k = 0; k < displayModes.length; k++) {
					if (displayModes[k].getWidth() == widths[i] && displayModes[k].getHeight() == heights[j]) allDisplayModes[i][j] = displayModes[k];
				}
			}
		}

		int index = 0;
		for (int i = 0; i < widths.length; i++) {
			for (int j = 0; j < heights.length; j++) {
				if (allDisplayModes[i][j] != null) {
					displayModes[index] = allDisplayModes[i][j];
					index++;
				}
			}
		}
	}

	public void setDisplayMode(int i) {
		if (i >= 0 && i < displayModes.length) {
			try {
				Display.setDisplayMode(displayModes[i]);
				displayMode = i;

				width = getDisplayMode().getWidth();
				height = getDisplayMode().getHeight();

				GameOptions.instance.setOption("width", String.valueOf(width));
				GameOptions.instance.setOption("height", String.valueOf(height));

				glScissor(0, 0, width, height);
				glViewport(0, 0, width, height);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
	}

	public DisplayMode getDisplayMode() {
		return displayModes[displayMode];
	}

	public String[] getDisplayModesAsString() {
		String[] ret = new String[displayModes.length];
		for (int i = 0; i < displayModes.length; i++) {
			ret[i] = displayModes[i].getWidth() + " x " + displayModes[i].getHeight();
		}

		return ret;
	}

	private void init() {
		glEnable(GL_TEXTURE_2D); // Enable Texture Mapping
		glShadeModel(GL_SMOOTH); // Enable Smooth Shading

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		glClearDepth(1.0); // Depth Buffer Setup
		glEnable(GL_DEPTH_TEST); // Enables Depth Testing
		glDepthFunc(GL_LEQUAL); // The Type Of Depth Testing

		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		setPerspective();

		// Perspective Calculations
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		// Set VSync
		setVSync();
	}

	public void setVSync() {
		Display.setVSyncEnabled(GameOptions.instance.getBoolOption("vSync"));
	}

	public void setOrtho() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Reset The Projection Matrix
		glOrtho(0.0D, getDisplayMode().getWidth(), getDisplayMode().getHeight(), 0.0D, 1f, -1f);
		glMatrixMode(GL_MODELVIEW);
	}

	public void setPerspective() {
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		//glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );
		//glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST );

		float fov = (float) GameOptions.instance.getDoubleOption("fov");
		glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
		glLoadIdentity(); // Reset The Projection Matrix
		GLU.gluPerspective(fov, (float) getDisplayMode().getWidth() / (float) getDisplayMode().getHeight(), 0.1f, 10000.0f);
		glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
	}

	public void toggleFullscreen() {
		fullscreen = !fullscreen;

		try {
			Display.setFullscreen(fullscreen);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDisplayMode(displayMode);

		GameOptions.instance.setBoolOption("fullscreen", fullscreen);
	}

	public void checkErrors() {
		int errorCode = glGetError();
		if (errorCode != GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorCode);
			System.out.println("openGL error detected:");
			System.out.println(errorString);
		}
	}
}
