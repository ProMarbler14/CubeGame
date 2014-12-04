//-----------------------------------------------------------------------------
// Vector3.java
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

// THIS CLASS IS DEPRECATED, WILL USE THE UTIL CLASSES IN LWJGL

package math;

public class Vector3 {
	
	public float x;
	public float y;
	public float z;
	
	/**
	 * Creates a new vector
	 * @param x
	 * @param y 
	 * @param z
	 */
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Scales the vector by scale
	 * @param scale the scale of the vector
	 */
	public void scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}
}
