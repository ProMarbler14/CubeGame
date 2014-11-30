//-----------------------------------------------------------------------------
// Cube.java
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
		{ 0.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f }, // back
		{ 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f },     // front
		{ 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 0.0f },   // right
		{ 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, -1.0f },   // left
		{ 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f },   // bottom
		{ 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f }    // top
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
	 * List of materials
	 */
	public static final byte AIR = 0;
	public static final byte DIRT = 1;
	public static final byte GRASS = 2;
	public static final byte COBBLE = 3;
	public static final byte WATER = 4;
}
