//-----------------------------------------------------------------------------
// World.java
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
