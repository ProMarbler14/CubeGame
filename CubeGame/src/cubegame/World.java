//-----------------------------------------------------------------------------
// World.java
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

import java.util.ArrayList;

public class World {
	
	private static ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
	
	/**
	 * Renders the world chunks
	 */
	public static void render() {
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
