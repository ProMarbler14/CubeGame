//-----------------------------------------------------------------------------
// Camera.java
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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.input.Mouse;

import math.Vector3;

public class Camera {
	/**
	 * The 3D position of the camera
	 */
	private Vector3 position;
	
	/**
	 * The euler rotation of the camera
	 */
	private Vector3 rotation;
	
	/**
	 * Creates a camera object and initializes it
	 */
	public Camera() {
		init();
	}
	
	/**
	 * Initializes the camera
	 */
	private void init() {
		// initialize the OpenGL camera
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(70, Graphics.getAspectRatio(), 0.01f, 200.0f);
		glMatrixMode(GL_MODELVIEW);
		
		// set transform
		setTransform(new Vector3(0.0f, 0.0f, 0.0f), new Vector3(20.0f, 30.0f, 0.0f));
	}
	
	/**
	 * Updates the camera to its current transformation
	 */
	public void update() {
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glTranslatef(-position.x, position.y, position.z);
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		glRotatef(rotation.z, 0, 0, 1);
	}
	
	/**
	 * Sets the position of the camera in 3D space
	 * @param pos the position vector of the camera
	 */
	public void setPosition(Vector3 pos) {
		position = pos;
	}
	
	/**
	 * Sets the rotation of the camera in euler angles
	 * @param rot the euler rotation vector
	 */
	public void setRotation(Vector3 rot) {
		rotation = rot;
	}
	
	/**
	 * Set the transformation matrix of the camera
	 * @param pos the position of the camera
	 * @param rot the rotation of the camera in euler representation
	 */
	public void setTransform(Vector3 pos, Vector3 rot) {
		position = pos;
		rotation = rot;
	}
	
	/**
	 * Gets the position of the camera
	 * @return the position of the camera
	 */
	public Vector3 getPosition() {
		return position;
	}
	
	/**
	 * Gets the euler rotation of the camera
	 * @return the rotation
	 */
	public Vector3 getRotation() {
		return rotation;
	}
	
	/**
	 * apply camera rotation and translations
	 * @param x the amount to move in the X direction
	 * @param z the amount to move in the Z direction
	 */
	public void apply(float x, float z) {
		// first apply rotation
		applyRotation();
		
		double yaw = (double)getYaw();
		
		// x axis
		if (x > 0.0f) {
			
		} else if (x < 0.0f) {
			
		}
		
		// z axis
		if (z > 0.0f) {
			position.x -= Math.abs(z) * (float)Math.sin(Math.toRadians(yaw));
			position.z += Math.abs(z) * (float)Math.cos(Math.toRadians(yaw));
		} else if (z < 0.0f) {
			position.x += Math.abs(z) * (float)Math.sin(Math.toRadians(yaw));
			position.z -= Math.abs(z) * (float)Math.cos(Math.toRadians(yaw));
		}
	}
	
	/**
	 * Adds rotation to the camera from mouse movement
	 */
	private void applyRotation() {
		rotation.x += (float)Mouse.getDY();
		rotation.y += (float)Mouse.getDX();
	}
	
	public float getYaw() {
		return rotation.y;
	}
	
	public float getPitch() {
		return rotation.x;
	}
	
	/**
	 * Moves the camera by this amount
	 * @param x the amount to move in the X direction
	 * @param y the amount to move in the Y direction
	 * @param z the amount to move in the Z direction
	 * 
	 * @deprecated
	 */
	// TODO remove this
	public void moveBy(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
}
