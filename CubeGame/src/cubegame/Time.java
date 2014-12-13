//-----------------------------------------------------------------------------
// Time.java
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

import org.lwjgl.Sys;
import org.lwjgl.Sys.*;
import org.lwjgl.opengl.Display;

public class Time {
	/**
	 * The time that has passed in milliseconds from the last frame
	 */
	public static int delta = 0;
	
	/**
	 * Last frame's time since timer start, counted from 0
	 */
	private static int lastTime = 0;
	
	/**
	 * FPS counter
	 */
	private static int fpsCounter = 0;
	private static int nextSecond = 0;
	
	/**
	 * Current fps
	 */
	private static int fps;
	
	/**
	 * Timer start time, in milliseconds since the Unix epoch.
	 */
	private static long startTime;
	
	/**
	 * Initialize the timer
	 */
	public static void init() {
		startTime = Sys.getTime();
		lastTime = 0;
		nextSecond = lastTime + 1000;
	}
	
	/**
	 * Get the running time in milliseconds
	 * @return the running time in milliseconds
	 */
	public static int getRunTime() {
		return (int) (((Sys.getTime() - startTime) * 1000) / Sys.getTimerResolution());
	}
	
	/**
	 * Gets the actual (system) time.
	 * @return the current system Unix time.
	 */
	public static long getSystemTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Calculate the elapsed time for the frame
	 */
	public static void update() {
		int time = getRunTime();
		delta = time - lastTime;
		lastTime = time;
		
		fpsCounter ++;
		if (lastTime > nextSecond) {
			fps = fpsCounter;
			fpsCounter = 0;
			nextSecond = lastTime + 1000;
			
			// show fps
			Display.setTitle("JCraft Version 1.0.0 DEV FPS: " + Time.getFPS() + " mspf: " + (1000 / (float)Time.getFPS())/* + " Pos: " + Graphics.camera.getPosition() + " Rot: " + Graphics.camera.getPosition()*/);
		}
	}
	
	/**
	 * Determines if we should sleep on the next frame (prevents you from having over 1000fps)
	 * @return true if the main thread should sleep
	 */
	public static boolean shouldMainThreadSleep() {
		return (getRunTime() - lastTime) <= 0;
	}
	
	/**
	 * Gets the frames per second
	 * @return the fps
	 */
	public static int getFPS() {
		return fps;
	}
	
}
