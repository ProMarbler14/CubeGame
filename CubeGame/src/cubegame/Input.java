//-----------------------------------------------------------------------------
// Input.java
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
