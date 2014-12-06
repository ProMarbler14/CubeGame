//-----------------------------------------------------------------------------
// Main.java
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

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Main {
	public static void main(String[] args) {
		Graphics.init(800, 600);
		Time.init();
		World.init();
		Mouse.setGrabbed(true);

		// Comment me to disable vertical sync
		//Display.setVSyncEnabled(true);
		
		do {
			//Can't have over 1,000 fps...or else delta would be 0
			if (Time.shouldMainThreadSleep()) {
				try {
					Thread.currentThread();
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			Time.update();
			Input.pullInput();
			Graphics.render(Time.delta);
			
		} while (!Display.isCloseRequested());
		
		// cleanup
		Graphics.destroy();
	}
}
