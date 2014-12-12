//-----------------------------------------------------------------------------
// Level.java
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

import math.Vector3;

// note: should probably give this a different name?
public class Level {
	// TODO: Make a grid of chunks, call addchunk with coords
	public static final int GRID_X_DIM = 6;
	public static final int GRID_Y_DIM = 6;
	public static final int GRID_Z_DIM = 6;
	
	/**
	 * Prepares a default grid.
	 */
	public static void prepare() {
		//World.addChunk(new Chunk(new Vector3(0.0f, 0.0f, 0.0f)));
		prepare(GRID_X_DIM, GRID_Y_DIM, GRID_Z_DIM);
	}
	/**
	 * Prepares a grid with dimensions xdim * ydim * zdim
	 * @param xdim Number of chunks long for x dim
	 * @param ydim Number of chunks long for y dim
	 * @param zdim Number of chunks long for z dim
	 */
	public static void prepare(int xdim, int ydim, int zdim) {
		//TODO: make with custom dimensions
		
		// Prepare our grid, currently with one unit spacing.
		for (int x = 0; x < (xdim * Chunk.CHUNK_SIZE + 1.0); x+=(Chunk.CHUNK_SIZE + 1)) {
			for (int z = 0; z < (zdim * Chunk.CHUNK_SIZE + 1.0); z+=(Chunk.CHUNK_SIZE + 1)) {
				for (int y = 0; y < (ydim * Chunk.CHUNK_SIZE + 1.0); y+=(Chunk.CHUNK_SIZE + 1)) {
					Chunk nextChunk = new Chunk(new Vector3((float) x, (float) -y, (float) -z));
					nextChunk.setColors(0.2f, 0.4f, 0.6f);
					World.addChunk(nextChunk);
				}
			}
		}
		
	}
}
