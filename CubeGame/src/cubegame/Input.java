//-----------------------------------------------------------------------------
// Input.java
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

import org.lwjgl.input.*;

public class Input {
	private static float horizontal = 0.0f;
	private static float vertical = 0.0f;
	
	/**
	 * Pulls the input from the keyboard
	 */
	public static void pullInput() {
		while (Keyboard.next()) {
			// key press down
			if (Keyboard.getEventKeyState()) {
				// Vertical input
				if (Keyboard.getEventKey() == Keyboard.KEY_W)
					vertical += 1.0f;
				if (Keyboard.getEventKey() == Keyboard.KEY_S)
					vertical -= 1.0f;
				
				// Horizontal input
				if (Keyboard.getEventKey() == Keyboard.KEY_D)
					horizontal += 1.0f;
				if (Keyboard.getEventKey() == Keyboard.KEY_A)
					horizontal -= 1.0f;
			} else { // key press up
				// Vertical input
				if (Keyboard.getEventKey() == Keyboard.KEY_W)
					vertical -= 1.0f;
				if (Keyboard.getEventKey() == Keyboard.KEY_S)
					vertical += 1.0f;
				
				// Horizontal input
				if (Keyboard.getEventKey() == Keyboard.KEY_D)
					horizontal -= 1.0f;
				if (Keyboard.getEventKey() == Keyboard.KEY_A)
					horizontal += 1.0f;				
			}
		}
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
}
