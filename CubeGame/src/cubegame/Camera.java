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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import math.Vector3;

public class Camera {
	/**
	 * The 3D position of the camera
	 */
	private Vector3f position;
	
	/**
	 * The euler rotation of the camera
	 */
	private Vector3f rotation;
	
	public static final float movementSpeed = 10.0f;
	public static final float cameraSpeed = 0.01f;
	
	public static final float pitchMax = 1.4f;
	public static final float pitchMin = -1.55f;
	
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
		setTransform(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
	}
	
	/**
	 * Updates the camera to its current transformation
	 */
	public void update() {
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		Matrix4f r = getRotationMatrix();

		float mat[] = {
			r.m00, r.m10, r.m20, r.m30,
			r.m01, r.m11, r.m21, r.m31,
			r.m02, r.m12, r.m22, r.m32,
			r.m03, r.m13, r.m23, r.m33
		};
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(mat);
		buffer.rewind();
		
		glLoadMatrix(buffer);
		glTranslatef(-position.x, position.y, position.z);
	}
	
	/**
	 * Sets the position of the camera in 3D space
	 * @param pos the position vector of the camera
	 */
	public void setPosition(Vector3f pos) {
		position = pos;
	}
	
	/**
	 * Set the transformation matrix of the camera
	 * @param pos the position of the camera
	 * @param rot the rotation of the camera in euler representation
	 */
	public void setTransform(Vector3f pos, Vector3f rot) {
		position = pos;
		rotation = rot;
	}
	
	/**
	 * Gets the position of the camera
	 * @return the position of the camera
	 */
	public Vector3f getPosition() {
		return position;
	}
	
	/**
	 * Gets the matrix rotation of the camera
	 * @return the rotation
	 */
	public Vector3f getRotation() {
		return rotation;
	}
	
	public float getYaw() {
		//TODO
		return 0;
//		return rotation.y;
	}
	
	public float getPitch() {
		//TODO
		return 0;
//		return rotation.x;
	}
	
	/**
	 * Applies the given motion
	 * @param x the amount to move in the X direction
	 * @param y the amount to move in the Y direction
	 * @param z the amount to move in the Z direction
	 */
	public void applyMotion(float x, float y, float z) {
		Vector3f motion = (Vector3f) new Vector3f(x, y, z).scale(movementSpeed);
		
		Matrix4f matrix = (Matrix4f) new Matrix4f().setIdentity();
		Matrix4f.rotate(rotation.y, new Vector3f(0.0f, -1.0f, 0.0f), matrix, matrix);
		Matrix4f.rotate(rotation.x, new Vector3f(1.0f, 0.0f, 0.0f), matrix, matrix);
		Matrix4f.translate(motion, matrix, matrix);
		
		position = Vector3f.add(position, new Vector3f(matrix.m30, matrix.m31, matrix.m32), null);
	}
	
	/**
	 * Applies the given rotation, adding it to the camera's current rotation vector
	 * @param pitch The pitch to add to the current rotation
	 * @param yaw The yaw to add to the current rotation
	 */
	public void applyRotation(float pitch, float yaw) {
		rotation.x += pitch * cameraSpeed;
		rotation.y += -yaw * cameraSpeed;
		
		rotation.x = Math.max(Math.min(rotation.x, pitchMax), pitchMin);
	}
	
	public Matrix4f getRotationMatrix() {
		Matrix4f r = new Matrix4f();
		r.setIdentity();
		Matrix4f.rotate(rotation.y, new Vector3f(0.0f, 1.0f, 0.0f), r, r);
		Matrix4f.rotate(rotation.x, new Vector3f(1.0f, 0.0f, 0.0f), r, r);
		
		return r;
	}
}
