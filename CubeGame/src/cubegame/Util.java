//-----------------------------------------------------------------------------
// Util.java
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

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Util {
	/**
	 * Creates a float buffer for the given data, all rewound and everything. Perfect for
	 * passing into OpenGL.
	 * @param data The float[] data to use
	 * @return A FloatBuffer that *doesn't* make you want to eviscerate the creators of nio
	 */
	public static FloatBuffer createBuffer(float data[]) {
		//Fuck nio for everything except their one-liners
		return (FloatBuffer)BufferUtils.createFloatBuffer(data.length).put(data).rewind();
	}
}
