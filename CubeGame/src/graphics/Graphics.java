//-----------------------------------------------------------------------------
// Graphics.java
//
// Copyright (c) 2014 Jeff Hutchinson
// Copyright (c) 2014 Glenn Smith
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without 
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, 
//    this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation 
//    and/or other materials provided with the distribution.
//
// 3. Neither the name of the copyright holder nor the names of its 
//    contributors may be used to endorse or promote products derived from this
//    software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//-----------------------------------------------------------------------------

package graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import cubegame.*;
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
			Display.setTitle("CubeGame 1.0.0 - DEV");
			Display.create();
			
			// Initialize our OpenGL layer
			GL.init();
			
			// Check for GL 2.0 support (need that at least)
			checkOpenGL();
			
			// initialize the camera
			camera = new Camera();
			
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
		
		// render the world
		World.render();
		
		// Swap the buffers
		Display.update();
	}
	
	/**
	 * Destroys the canvas render window and shuts down the graphics library
	 */
	public static void destroy() {		
		System.out.println("Graphics are shutting down.");
		// cleanup the GL
		GL.cleanup();
		
		Display.destroy();
	}
	
	/**
	 * Checks to ensure that we have a proper min openGL version
	 */
	private static void checkOpenGL() {
		if (GL.getOpenGLMajorVersion() == 1 && GL.getOpenGLMinorVersion() < 1) {
			System.err.println("You need at least OpenGL 1.1 in order to run CubeGame.");
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
