//-----------------------------------------------------------------------------
// World.java
//
// Copyright (c) 2014 Jeff Hutchinson
// Copyright (c) 2014 Glenn Smith
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

import java.util.ArrayList;

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
	}

	/**
	 * Renders the world chunks
	 */
	public static void render() {
		// bind the opengl texture
		mapTexture.bind();
		
		// draw all chunks
		for (Chunk chunk : chunkList) {
			// TODO:
			// check to see if chunk is inside of camera's view frustrum
			chunk.render();
		}
	}
	
	/**
	 * Adds a chunk object into the world
	 * @param chunk the chunk object
	 */
	public static void addChunk(Chunk chunk) {
		chunkList.add(chunk);
	}
}
