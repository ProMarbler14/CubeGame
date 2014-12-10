//-----------------------------------------------------------------------------
// GL.java
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

/**
 * OpenGL includes
 */
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GLContext;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import cubegame.Util;

/**
 * The GL class is a higher level API that is responsible
 * for handling OpenGL between different contexts into a
 * common API.  It is responsible for maintaining an easy to
 * use graphics system.
 * 
 * Current API's supported:
 * 
 * - Fixed Function Pipeline:
 *    - GL 1.1 (Vertex Arrays)
 *    - GL 1.x (ARB VBO required)
 *    - GL 1.5 (Vertex Buffer Objects)
 */
public final class GL {
	private static boolean initialized = false;
	private static int majorVersion = 0;
	private static int minorVersion = 0;
	
	/**
	 * Internal use only
	 * True if the current context supports at least OpenGL 2.0
	 */
	@SuppressWarnings(value = { "unused" })
	private static boolean supportsOpenGL20 = false;
	
	/**
	 * Internal use only
	 * True if the current context supports at least OpenGL 1.5
	 */
	private static boolean supportsOpenGL15 = false;
	
	/**
	 * Internal use only
	 * True if the GL version is < 1.5 and the context has the vbo ARB extension
	 */
	private static boolean supportsARBVBO = false;
	
	/**
	 * Internal use only
	 * True if the GL context is a legacy openGL context. Legacy is the last resort context
	 * that is 100% fixed function pipeline with no vbo extensions.  All client vertex arrays.
	 */
	private static boolean isLegacy = false;

	/**
	 * Stores VBO ids and cleans them up at the end
	 * Since the garbage collector doesn't interfere with native and GL code
	 */
	private static ArrayList<Integer> vboGC;
	
	/**
	 * Initializes the OpenGL system to be usable
	 */
	public static void init() {
		initialized = true;
		vboGC = new ArrayList<Integer>();
		majorVersion = Integer.parseInt(getOpenGLVersion().substring(0,1).trim());
		minorVersion = Integer.parseInt(getOpenGLVersion().substring(2,3).trim());
		
		// set the depth buffer for proper depth alignment
		glEnable(GL_DEPTH_TEST);
		
		// enable hardware culling, idk why its GL_FRONT but its inside out with doing the default...
		glCullFace(GL_FRONT);
		glEnable(GL_CULL_FACE);
		
		glShadeModel(GL_SMOOTH);
		glEnable(GL_COLOR_MATERIAL);
		
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		
		glLight(GL_LIGHT0, GL_POSITION, Util.createBuffer(new float[] {0.0f, 1.0f, 1.0f, 0.0f}));
		glLight(GL_LIGHT0, GL_DIFFUSE, Util.createBuffer(new float[] {1.0f, 1.0f, 1.0f, 1.0f}));
		
		// texturing:
		glEnable(GL_TEXTURE_2D);
		
		if (majorVersion >= 2) {
			supportsOpenGL20 = true;
			supportsOpenGL15 = true;
		} else if (majorVersion == 1) {
			if (minorVersion >= 5) {
				supportsOpenGL15 = true;
			} else if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
				supportsARBVBO = true;
			} else {
				// old powerpc macs probably? tehehe
				// I'LL SUPPORT THEM FOREVERRR
				isLegacy = true;
			}
		}
	}
	
	/**
	 * Cleans up the GL before closing the application
	 */
	public static void cleanup() {
		initialized = false;

		// free all vbos
		for (int id : vboGC) {
			if (supportsOpenGL15)
				glDeleteBuffers(id);
			else if (supportsARBVBO)
				ARBVertexBufferObject.glDeleteBuffersARB(id);
		}
		vboGC.clear();
	}

	/**
	 * generates a vertex buffer object
	 * @return the ID of the VBO, or -1 if the VBO couldn't be created
	 */
	public static int genVBO() {
		if (supportsOpenGL15)
			return glGenBuffers();
		else if (supportsARBVBO) {
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
			ARBVertexBufferObject.glGenBuffersARB(buffer);
			return buffer.get(0);
		}
		return -1;
	}
	
	/**
	 * prepares a static vertex buffer object of floats
	 * 
	 * @param id the VBO id
	 * @param data (floats) the data to be pushed into the VBO
	 * @see int method
	 */
	public static void prepareStaticVBO(int id, FloatBuffer buffer) {
		if (supportsOpenGL15) {
			glBindBuffer(GL_ARRAY_BUFFER, id);
			glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		} else if (supportsARBVBO) {
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
			ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		}
	}
	
	/**
	 * prepares a static vertex buffer object of integers
	 * 
	 * @param id the VBO id
	 * @param data (integers) the data to be pushed into the VBO
	 * @see float method
	 */
	public static void prepareStaticVBO(int id, IntBuffer buffer) {
		if (supportsOpenGL15) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		} else if (supportsARBVBO) {
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
			ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		}
	}
	
	/**
	 * Binds a static VBO before submitting it to the graphics driver
	 * @param id the VBO id
	 */
	public static void bindStaticBuffer(int id) {
		if (supportsOpenGL15)
			glBindBuffer(GL_ARRAY_BUFFER, id);
		else if (supportsARBVBO)
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
	}
	
	/**
	 * Binds a static VBO (index buffer object) before submitting it to the graphics driver
	 * @param id the VBO id
	 */
	public static void bindStaticIndexBuffer(int id) {
		if (supportsOpenGL15)
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
		else if (supportsARBVBO)
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);		
	}
	
	/**
	 * Deletes the vertex buffer object (or index buffer object)
	 * @param id the VBO id
	 */
	public static void deleteVBO(int id) {
		if (supportsOpenGL15)
			glDeleteBuffers(id);
		else if (supportsARBVBO)
			ARBVertexBufferObject.glDeleteBuffersARB(id);
		vboGC.remove(id);
	}
	
	/**
	 * Checks to see if the GL is rendering with a legacy context.
	 * A legacy context is Fixed-Function Pipeline OpenGL with no
	 * vertex buffers. The only requirement is at least GL 1.1
	 * @return true if the GL is a legacy context
	 */
	public static boolean isLegacy() {
		return isLegacy;
	}
	
	/**
	 * Gets the OpenGL version from the current context
	 * @return the OpenGL version string, major.minor
	 */
	public static String getOpenGLVersion() {
		if (!initialized) {
			System.err.println("GL was not initialized!");
			return null;
		}
		String gl = glGetString(GL_VERSION);
		gl = gl.substring(0, gl.indexOf(" ")).trim();
		return gl;
	}
	
	/**
	 * Gets the major version of the current OpenGL context
	 * @return the major version of the current OpenGL context
	 */
	public static int getOpenGLMajorVersion() {
		if (!initialized) {
			System.err.println("GL was not initialized!");
			return -1;
		}
		return majorVersion;
	}
	
	/**
	 * Gets the minor version of the current OpenGL context
	 * @return the minor version of the current OpenGL context
	 */
	public static int getOpenGLMinorVersion() {
		if (!initialized) {
			System.err.println("GL was not initialized!");
			return -1;
		}
		return minorVersion;
	}
}
