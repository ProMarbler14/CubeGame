//-----------------------------------------------------------------------------
// Cube.java
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

import org.lwjgl.util.vector.Vector2f;

public class Cube {
	public static final int FACE_BACK = 0;
	public static final int FACE_FRONT = 1;
	public static final int FACE_RIGHT = 2;
	public static final int FACE_LEFT = 3;
	public static final int FACE_BOTTOM = 4;
	public static final int FACE_TOP = 5;
	
	/**
	 * The vertices (in unit notation) for a specific face
	 * To use, float vert[] = vertices[FACE_X]; where X is the face you want of the cube
	 */	
	public static float vertices[][] = {
		{
			// back
			0.0f, 0.0f, 0.0f, // 0
			1.0f, 0.0f, 0.0f, // 1
			1.0f, 1.0f, 0.0f, // 2
			0.0f, 1.0f, 0.0f, // 3
		},
		{
			// front
			1.0f, 0.0f, 1.0f, // 0
			0.0f, 0.0f, 1.0f, // 1
			0.0f, 1.0f, 1.0f, // 2
			1.0f, 1.0f, 1.0f  // 3
		},
		{
			// right
			1.0f, 0.0f, 0.0f, // 0
			1.0f, 0.0f, 1.0f, // 1
			1.0f, 1.0f, 1.0f, // 2
			1.0f, 1.0f, 0.0f  // 3
		},
		{
			// left
			0.0f, 0.0f, 1.0f, // 0
			0.0f, 0.0f, 0.0f, // 1
			0.0f, 1.0f, 0.0f, // 2
			0.0f, 1.0f, 1.0f  // 3 
		},
		{
			// bottom
			0.0f, 0.0f, 1.0f, // 0
			1.0f, 0.0f, 1.0f, // 1
			1.0f, 0.0f, 0.0f, // 2
			0.0f, 0.0f, 0.0f  // 3
		},
		{
			// top
			0.0f, 1.0f, 0.0f, // 0
			1.0f, 1.0f, 0.0f, // 1
			1.0f, 1.0f, 1.0f, // 2
			0.0f, 1.0f, 1.0f  // 3
		}
	};
	
	/**
	 * The normal vectors for each face
	 */
	public static float normals[][] = { 
		{ 0.0f, 0.0f, -1.0f }, // back
		{ 0.0f, 0.0f, 1.0f }, // front
		{ 1.0f, 0.0f, 0.0f }, // right
		{-1.0f, 0.0f, 0.0f }, // left
		{ 0.0f, -1.0f, 0.0f }, // bottom
		{ 0.0f, 1.0f, 0.0f } // top
	};
	
	/**
	 * @param offset the texture offset to get (note, recursivly wraps to next column if > length of row)
	 * @param textureWidth
	 * @param textureHeight
	 * @return an array of UVs mapped from the giving texture offset
	 * @note this guy deserves a cookie:
	 * http://gamedev.stackexchange.com/a/48903
	 */
	public static Vector2f[] getUVTextureMap(short offset, int textureWidth, int textureHeight) {
		int off = (int)offset;
		int u = off % textureWidth;
		int v = off / textureHeight;
		float xOffset = 1.0f / textureWidth;
		float yOffset = 1.0f / textureHeight;
		float uOffset = u * xOffset;
		float vOffset = v * yOffset;
		
		Vector2f uvs[] = new Vector2f[6];
		uvs[0] = new Vector2f(uOffset, vOffset + yOffset); // 0, 1 (0)
		uvs[1] = new Vector2f(uOffset + xOffset, vOffset + yOffset); // 1, 1 (1)
		uvs[2] = new Vector2f(uOffset + xOffset, vOffset); // 1, 0 (2)
		uvs[3] = new Vector2f(uOffset + xOffset, vOffset); // 1, 0 (2)
		uvs[4] = new Vector2f(uOffset, vOffset);           // 0, 0 (3)
		uvs[5] = new Vector2f(uOffset, vOffset + yOffset); // 0, 1 (0)
		return uvs;
	}
	
	/**
	 * List of materials
	 */
	public static final short AIR = -1;
	public static final short DIRT = 0;
	public static final short GRASS = 1;
	public static final short COBBLE = 2;
	public static final short WATER = 3;
}
