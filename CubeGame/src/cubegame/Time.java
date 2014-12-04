//-----------------------------------------------------------------------------
// Time.java
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

import org.lwjgl.Sys;
import org.lwjgl.Sys.*;
import org.lwjgl.opengl.Display;

public class Time {
	/**
	 * The time that has passed in milliseconds from the last frame
	 */
	public static int delta = 0;
	
	/**
	 * Last frame's time
	 */
	private static long lastTime = 0;
	
	/**
	 * FPS counter
	 */
	private static int fpsCounter = 0;
	private static long nextSecond = 0;
	
	/**
	 * Current fps
	 */
	private static int fps;
	
	/**
	 * Initialize the timer
	 */
	public static void init() {
		lastTime = getTime();
		nextSecond = lastTime + 1000;
	}
	
	/**
	 * Get the time in milliseconds
	 * @return the time in milliseconds
	 */
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Calculate the elapsed time for the frame
	 */
	public static void update() {
		long time = getTime();
		delta = (int)(long)(time - lastTime);
		lastTime = time;
		
		fpsCounter ++;
		if (lastTime > nextSecond) {
			fps = fpsCounter;
			fpsCounter = 0;
			nextSecond = lastTime + 1000;
			
			// show fps
			Display.setTitle("JCraft Version 1.0.0 DEV FPS: " + Time.getFPS() + " mspf: " + (1000 / (float)Time.getFPS()));
		}
	}
	
	/**
	 * Determines if we should sleep on the next frame (prevents you from having over 1000fps)
	 * @return true if the main thread should sleep
	 */
	public static boolean shouldMainThreadSleep() {
		return (getTime() - lastTime) <= 0;
	}
	
	/**
	 * Gets the frames per second
	 * @return the fps
	 */
	public static int getFPS() {
		return fps;
	}
}
