//-----------------------------------------------------------------------------
// Graphics.java
//
// Copyright (c) 2014 Jeff Hutchinson
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//-----------------------------------------------------------------------------

package cubegame;

import math.Vector3;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import test.GLTest;
import static org.lwjgl.opengl.GL11.*;

public final class Graphics {
	/**
	 * The camera object for the user
	 */
	public static Camera camera = null;
	
	/**
	 * Initialize the graphics library
	 * @param x the x resolution of the canvas
	 * @param y the y resolution of the canvas
	 */
	public static void init(int x, int y) {
		try {
			Display.setDisplayMode(new DisplayMode(x, y));
			Display.setTitle("JCraft 1.0.0 - DEV");
			Display.create();
			
			// Initialize our OpenGL layer
			GL.init();
			
			// Check for GL 2.0 support (need that at least)
			checkOpenGL();
			
			// initialize the camera
			camera = new Camera();
			
			// create a chunk!
			World.addChunk(new Chunk(new Vector3(0.0f, 0.0f, 0.0f)));
			
			// test immediate mode
			//GLTest.prepareImmediateMode();
			
		} catch (LWJGLException e) {
			destroy();
			System.exit(1);
		}
	}
	
	/**
	 * Main render function to render the game
	 * At the end, the buffers are swapped.
	 */
	public static void render(int delta) {
		// clear the screen and set view port
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		// update camera position and center cursor so we can measure dx/dy of mouse
		float dt = delta / 1000.0f;
		//camera.apply(Input.getHorizontal() * dt, Input.getVertical() * dt);
		camera.applyMotion(Input.getHorizontal() * dt, 0, Input.getVertical() * dt);
		camera.applyRotation(Input.getPitch(), Input.getYaw());
		camera.update();
		
		// test immediate mode
		//GLTest.renderImmediateMod();
		
		// render the world
		World.render();
		
		// Swap the buffers
		Display.update();
	}
	
	/**
	 * Destroys the canvas render window and shuts down the graphics library
	 */
	public static void destroy() {
		
		// cleanup immediate mode
		//GLTest.cleanUpImmediateMode();
		
		// cleanup the GL
		GL.cleanup();
		
		Display.destroy();
	}
	
	/**
	 * Checks to ensure that we have a proper min openGL version
	 */
	private static void checkOpenGL() {
		if (GL.getOpenGLMajorVersion() < 2) {
			System.err.println("You need at least OpenGL 2.0 in order to run JCraft.");
			System.err.println("Current OpenGL version: " + GL.getOpenGLVersion());
			destroy();
			System.exit(1);
		} else {
			System.out.println("Running with OpenGL " + GL.getOpenGLVersion());
		}		
	}
	
	/**
	 * Gets the aspect ratio of the window
	 * @return the aspect ratio
	 */
	public static float getAspectRatio() {
		return (float)Display.getWidth() / (float)Display.getHeight();
	}
}
