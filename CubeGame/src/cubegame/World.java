//-----------------------------------------------------------------------------
// World.java
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

package cubegame;

import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import graphics.GL;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class World {
	
	private static ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
	
	/**
	 * Texture map used for texturing the world
	 */
	private static Texture mapTexture;
	
	public static final int mapTextureWidth = 16;
	public static final int mapTextureHeight = 16;
	
	public static void init() {
		// map the texture
		mapTexture = Util.loadTexture("res/textures/texturemap.png", "PNG");
		
		// create some chunks
		for (int x = 0; x < 1; x ++) {
			for (int z = 0; z < 1; z ++) {
				World.addChunk(new Chunk(new Vector3f((float)(x * (float)Chunk.CHUNK_SIZE), 0.0f, (float)(z * (float)Chunk.CHUNK_SIZE))));
			}
		}
	}

	/**
	 * Renders the world chunks
	 */
	public static void render() {
		// bind the opengl texture
		mapTexture.bind();

		// enable drawing
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		// draw all chunks
		for (Chunk chunk : chunkList) {
			// TODO:
			// check to see if chunk is inside of camera's view frustrum
			chunk.render();
		}
		
		// disable drawing
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);			
	}
	
	/**
	 * Adds a chunk object into the world
	 * @param chunk the chunk object
	 */
	public static void addChunk(Chunk chunk) {
		chunkList.add(chunk);
	}
}
