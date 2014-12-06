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
import math.Vector3;

public class World {
	
	private static ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
	
	// Timekeeping variables.
	private static final int INTERVAL = 2000;		// in milliseconds
	private static int nextSecond = 2000;		// unix epoch + 1000 milliseconds
	private static boolean shouldUpdate = false;
	
	
	
	/**
	 * Renders the world chunks
	 */
	public static void render() {
		shouldUpdate = checkTime();
		
		// draw all chunks
		for (Chunk chunk : chunkList) {
			// Update the color afters each interval.
			// Only works in VBO mode.
//			if (shouldUpdate) {
//					// Set the colors
//					updateColors(chunk);
//			}
			
			// TODO: check to see if chunk is inside of camera's view frustrum
			chunk.render();
		}
		shouldUpdate = false;
		
	}
	
	/**
	 * Check if the time interval has elapsed.
	 * @return true if the time interval has elapsed.
	 */
	private static boolean checkTime() {
		if (Time.getRunTime() >= nextSecond) {
			// Increment time
			nextSecond += INTERVAL;
			System.out.println("Stepping time. Next update time: " + nextSecond + " ms");
			return true;
		}
		return false;
	}
	
	/**
	 * Sets colors for a chunk
	 * @param chunk Chunk object to change
	 */
	private static void updateColors(Chunk chunk) {
		//TODO actually change colors
		Vector3 pos = chunk.getChunkPosition();
		float x = pos.x; float y = pos.y; float z = pos.z;
		System.out.println("(not) Updating chunk at " + x + ", " + y + ", " + z);
		chunk.setColors(1.0f, 0.0f, 0.0f);
	}
	
	/**
	 * Adds a chunk object into the world
	 * @param chunk the chunk object
	 */
	public static void addChunk(Chunk chunk) {
		chunkList.add(chunk);
	}
}
