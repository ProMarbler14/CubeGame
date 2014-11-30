//-----------------------------------------------------------------------------
// GL.java
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

/**
 * OpenGL includes
 */
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

/**
 * The GL class is a higher level API that is responsible
 * for handling OpenGL between different contexts into a
 * common API.  It is responsible for maintaining an easy to
 * use graphics system.
 * 
 * Planned Support:
 * OpenGL 2.0 (VBO + Shaders)
 * OpenGL 1.5 (VBO + FFP)
 * OpenGL 1.x + ARB VBO (for ARB VBOs)
 * OpenGL 1.1+ (ImmediateMode + Display Lists)
 */
public final class GL {
	private static boolean initialized = false;
	private static int majorVersion = 0;
	private static int minorVersion = 0;
	
	/**
	 * Interal use only
	 * True if the current context supports at least OpenGL 2.0
	 */
	private static boolean supportsOpenGL20 = false;
	
	/**
	 * Interal use only
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
	 * True if the GL version doesn't support VBOs.  Then we rely on immediate mode + displayLists
	 */
	private static boolean useImmediateMode = false;
	
	/**
	 * Internal use only
	 * True if the GL context supports some form of VBOs
	 */
	private static boolean supportsVBO = false;
	
	/**
	 * Stores display lists and cleans them up at the end
	 * Since the garbage collector doesn't interfere with native and GL code
	 */
	private static ArrayList<Integer> displayListGC;
	
	/**
	 * Initializes the OpenGL system to be usable
	 */
	public static void init() {
		initialized = true;
		displayListGC = new ArrayList<Integer>();
		majorVersion = Integer.parseInt(getOpenGLVersion().substring(0,1).trim());
		minorVersion = Integer.parseInt(getOpenGLVersion().substring(2,3).trim());
		
		// enable backface culling (render only half of everything! :D cheap hack for hardware culling)
		glEnable(GL_CULL_FACE);
		
		/*
		if (majorVersion >= 2) {
			supportsOpenGL20 = true;
			supportsOpenGL15 = true;
			supportsVBO = true;
		} else if (majorVersion == 1) {
			if (minorVersion >= 5) {
				supportsOpenGL15 = true;
				supportsVBO = true;
			} else if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
				supportsARBVBO = true;
				supportsVBO = true;
			} else {*/
				// old powerpc macs probably? tehehe
				// I'LL SUPPORT THEM FOREVERRR
				useImmediateMode = true;
			/*}
		}*/
	}
	
	/**
	 * Cleans up the GL before closing the application
	 */
	public static void cleanup() {
		initialized = false;
		
		// free all display lists
		for (int id : displayListGC)
			glDeleteLists(id, 1);
		displayListGC.clear();
	}
	
	/**
	 * Creates a display list
	 * @return the display list id
	 */
	public static int genDisplayList() {
		int list = glGenLists(1);
		displayListGC.add(list);
		return list;
	}
	
	/**
	 * Delete a display list from the GL
	 * @param id the display list ID
	 */
	public static void deleteDisplayList(int id) {
		// free it from ram
		glDeleteLists(id, 1);
		displayListGC.remove(id);
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
	 * prepares a static vertex buffer object
	 * 
	 * @param id the VBO id
	 * @param data the data to be pushed into the VBO
	 */
	public static void prepareStaticVBO(int id, float [] data) {
		if (supportsOpenGL15) {
			
		} else if (supportsARBVBO) {
			
		}
	}
	
	/**
	 * Deletes the vertex buffer object
	 * @param id
	 */
	public static void deleteVBO(int id) {
		if (supportsOpenGL15)
			glDeleteBuffers(id);
		else if (supportsARBVBO)
			ARBVertexBufferObject.glDeleteBuffersARB(id);
	}
	
	/**
	 * 
	 * @return true if the GL context is rendering using immediate mode
	 */
	public static boolean isImmediateMode() {
		return useImmediateMode;
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
