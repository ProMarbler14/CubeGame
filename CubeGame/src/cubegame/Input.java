//-----------------------------------------------------------------------------
// Input.java
//
// Copyright (c) 2014 Jeff Hutchinson
// Copyright (c) 2014 Glenn Smith
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

import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;

public class Input {
	private static float horizontal = 0.0f;
	private static float vertical = 0.0f;
	
	private static float pitch = 0.0f;
	private static float yaw = 0.0f;
	
	/**
	 * Pulls the input from the keyboard
	 */
	public static void pullInput() {
		horizontal = 0;
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			horizontal += 1.0f;
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			horizontal += -1.0f;
		
		vertical = 0;
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			vertical += 1.0f;
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			vertical += -1.0f;
		
		int cx = Display.getWidth() / 2;
		int cy = Display.getHeight() / 2;
		
		pitch = (float)Mouse.getDY();
		yaw = (float)Mouse.getDX();
		
	}
	
	/**
	 * The horizontal axis of input
	 * @return [-1.0f, 1.0f]
	 */
	public static float getHorizontal() {
		return horizontal;
	}
	
	/**
	 * The vertical axis of input
	 * @return [-1.0f, 1.0f]
	 */
	public static float getVertical() {
		return vertical;
	}
	
	/**
	 * The change in pitch to be applied
	 * @return float
	 */
	public static float getPitch() {
		return pitch;
	}
	
	/**
	 * The change in yaw to be applied
	 * @return float
	 */
	public static float getYaw() {
		return yaw;
	}
}
