package org.golde.java.game.renderEngine;

import java.io.File;

import org.golde.java.game.GLog;
import org.golde.java.game.helpers.IconLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	private static long lastFrameTime;
	private static float delta;

	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 3)
				.withForwardCompatible(true)
				.withProfileCore(true);
		try {

			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(true);
			Display.setResizable(false);
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Loading Game....");
			Display.setIcon(IconLoader.load(new File("res/icon.png")));

		} catch(LWJGLException e) {
			GLog.error(e, "Failed to create display!");
		}

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();

	}

	public static void aboutToStartGameLoop() {
		Display.setTitle("The Spook");
	}

	public static void updateDisplay() {

		/*if(Keyboard.isKeyDown(Keyboard.KEY_F)) {
			try {
				DisplayMode displayMode = null;
				DisplayMode[] modes = Display.getAvailableDisplayModes();

				for (int i = 0; i < modes.length; i++)
				{
					if (modes[i].getWidth() == 1280
							&& modes[i].getHeight() == 720
							&& modes[i].isFullscreenCapable())
					{
						displayMode = modes[i];
					}
				}
				if(displayMode != null) {
					GLog.info("Was not null");
					Display.setDisplayModeAndFullscreen(displayMode);
				}else {
					GLog.error("It was null!");
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}*/

		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	public static void closeDisplay() {
		Display.destroy();
		System.exit(0);
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static Vector2f getNormailzedMouseCoordinates() {
		float nx = -1.0f + 2.0f * (float) Mouse.getX() / (float) Display.getWidth();
		float nz = 1.0f - 2.0f * (float) Mouse.getY() / (float) Display.getHeight();
		return new Vector2f(nx, nz);
	}
}
