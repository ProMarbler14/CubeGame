//-----------------------------------------------------------------------------
// Cube.java
//
// Copyright (c) 2014 Jeff Hutchinson
// Copyright (c) 2014 Glenn Smith
// Copyright (c) 2014 Adric Blake
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
	 * List of materials
	 */
	public static final byte AIR = 0;
	public static final byte DIRT = 1;
	public static final byte GRASS = 2;
	public static final byte COBBLE = 3;
	public static final byte WATER = 4;
}
