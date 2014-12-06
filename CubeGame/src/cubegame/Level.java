//-----------------------------------------------------------------------------
// Level.java
//
// Copyright (c) 2014 Jeff Hutchinson
// Copyright (c) 2014 Glenn Smith
// Copyright (c) 2014 Adric Blake
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
