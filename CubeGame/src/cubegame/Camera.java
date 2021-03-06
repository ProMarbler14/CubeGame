//-----------------------------------------------------------------------------
// Camera.java
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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import graphics.Graphics;

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
	
	/**
	 * Movement speed of the camera to be applied
	 */
	public static final float movementSpeed = 10.0f;
	
	/**
	 * Rotation speed of the camera
	 */
	public static final float cameraSpeed = 0.01f;
	
	/**
	 * Maximum pitch
	 */
	public static final float pitchMax = 1.4f;
	
	/**
	 * Minimum pitch
	 */
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
		
		//Rotate the display by the rotatin matrix
		Matrix4f r = getRotationMatrix();

		//Cheap trick to get the float buffer to work, because using store() gives invalid transformations
		// for some reasons.
		float mat[] = {
			r.m00, r.m10, r.m20, r.m30,
			r.m01, r.m11, r.m21, r.m31,
			r.m02, r.m12, r.m22, r.m32,
			r.m03, r.m13, r.m23, r.m33
		};
		
		//Create the actual float buffer because we love nio /s
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(mat);
		//Fuck nio. If you don't rewind then glLoadMatrix tries to start loading from the end of the buffer.
		buffer.rewind();

		//Load the actual matrix
		glLoadMatrix(buffer);
		
		//Translate the matrix, not sure why Jeff inverted x, but this works
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
	 * Gets the Euler rotation of the camera
	 * @return the rotation
	 */
	public Vector3f getRotation() {
		return rotation;
	}
	
	/**
	 * Applies the given motion
	 * @param x the amount to move in the X direction
	 * @param y the amount to move in the Y direction
	 * @param z the amount to move in the Z direction
	 */
	public void applyMotion(float x, float y, float z) {
		//Create a raw motion vector scaled by speed
		Vector3f motion = (Vector3f) new Vector3f(x, y, z).scale(movementSpeed);
		
		//A matrix is used to rotate the motion vector to the camera's current orientation 
		Matrix4f matrix = (Matrix4f) new Matrix4f().setIdentity();
		
		//Yaw is inverted because we're facing outwards (thanks to Jeff for explaining this to me)
		Matrix4f.rotate(rotation.y, new Vector3f(0.0f, -1.0f, 0.0f), matrix, matrix);
		Matrix4f.rotate(rotation.x, new Vector3f(1.0f, 0.0f, 0.0f), matrix, matrix);
		
		//Translate second
		Matrix4f.translate(motion, matrix, matrix);
		
		//Cheap trick to get position from the matrix, as LWJGL doesn't actually have .position
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
		
		//Clamp pitch
		rotation.x = Math.max(Math.min(rotation.x, pitchMax), pitchMin);
		
		//Clamp yaw
		rotation.y %= Math.PI * 2.0f;
	}
	
	/**
	 * Gets the camera's rotation as a Matrix4f
	 * @return A matrix representation of the camera's rotation
	 */
	public Matrix4f getRotationMatrix() {
		Matrix4f r = new Matrix4f();
		r.setIdentity();
		
		//Rotate by yaw, pitch
		Matrix4f.rotate(rotation.y, new Vector3f(0.0f, 1.0f, 0.0f), r, r);
		Matrix4f.rotate(rotation.x, new Vector3f(1.0f, 0.0f, 0.0f), r, r);
		
		return r;
	}
}
