//-----------------------------------------------------------------------------
// Chunk.java
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

import java.util.ArrayList;

import org.lwjgl.opengl.GL15;

import math.Vector3;
import static org.lwjgl.opengl.GL11.*;

public class Chunk {	
	public static final int CHUNK_SIZE = 16;
	
	/**
	 * cubeList holds each cube's type (grass, air, ect)
	 * stored in the smallest datatype possible
	 */
	private byte[][][] cubeList;
	
	/**
	 * vertexList holds the vertices that are used for rendering the chunk
	 */
	private ArrayList<Float> vertexList;
	
	/**
	 * normalList holds the normals that are used for rendering the chunk
	 */
	private ArrayList<Float> normalList; 
	
	/**
	 * The global world position of the chunk
	 */
	private Vector3 position;
	
	/**
	 * The display list id if display lists are used
	 */
	private int displayList = -1;
	
	private int vertexBufferId = -1;
	private int indexBufferId = -1;
	
	/**
	 * Colors that could be set, determines chunk color.
	 * Ranges from 0.0f to 1.0f
	 */
	private float redValue = 0.0f;
	private float greenValue = 0.0f;
	private float blueValue = 1.0f;
	
	/**
	 * Sets the colors for rendering the chunk
	 * @param r value of red
	 * @param g value of green
	 * @param b value of blue
	 */
	public void setColors(float r, float g, float b) {
		//System.out.println("Colors set to " + r + " " + g + " " + b);
		redValue = r;
		greenValue = g;
		blueValue = b;
	}
	
	
	/**
	 * Creates a chunk of the size CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE
	 * @param position the world position of the cube, representing position
	 * of cube inside of chunk at local position <0, 0, 0>
	 */
	public Chunk(Vector3 position) {
		vertexList = new ArrayList<Float>();
		normalList = new ArrayList<Float>();
		cubeList = new byte[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
		this.position = position;
		createChunk();
	}

	/**
	 * Creates the chunk
	 */
	private void createChunk() {
		// create the chunk
		for (int x = 0; x < CHUNK_SIZE; x ++) {
			for (int z = 0; z < CHUNK_SIZE; z ++) {
				for (int y = 0; y < CHUNK_SIZE; y ++) {
					//if (y > 0)
						//break;
					
					// for now, just one layer of dirt
					//if (x % 4 == 0)
						cubeList[x][y][z] = Cube.DIRT;
					//else
						//cubeList[x][y][z] = Cube.AIR;
				}
			}
		}
		
		System.out.println("Creating a new chunk");
		
		// construct the list
		buildChunk();
		
		// prep it (build display list or vbo)
		preRenderChunk();
	}
	
	/**
	 * Builds an, optimized chunk
	 */
	private void buildChunk() {
		// clear list
		vertexList.clear();
		normalList.clear();		
		
		int x, y, z;
		for (x = 0; x < CHUNK_SIZE; x ++) {
			for (z = 0; z < CHUNK_SIZE; z ++) {
				for (y = 0; y < CHUNK_SIZE; y ++) {
					
					// if it is air, we render no sides!
					if (cubeList[x][y][z] == Cube.AIR)
						continue;

					// back face
					if (z == 0 || isTransparent(x, y, z - 1))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_BACK);
					
					// front face
					if ((z + 1 == CHUNK_SIZE) || isTransparent(x, y, z + 1))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_FRONT);
					
					// left face
					if (x == 0 || isTransparent(x - 1, y, z))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_LEFT);
					
					// right face
					if ((x + 1 == CHUNK_SIZE) || isTransparent(x + 1, y, z))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_RIGHT);
					
					// bottom face
					if (y == 0 || isTransparent(x, y - 1, z))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_BOTTOM);
					
					// top face
					if ((y + 1 == CHUNK_SIZE) || isTransparent(x, y + 1, z))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_TOP);
				}
			}
		}
	}
	
	/**
	 * Builds a face for a cube
	 * @param x cube x position
	 * @param y cube y position
	 * @param z cube z position
	 * @param side the face of the cube
	 */
	private void buildFace(float x, float y, float z, int side) {
		float buffer[] = Cube.vertices[side];
		
		if (GL.isImmediateMode()) {
			// 0, 1, 2, 2, 3, 0
			
			// point 0
			vertexList.add(buffer[0] + position.x + x);
			vertexList.add(buffer[1] + position.y + y);
			vertexList.add(buffer[2] + position.z + z);
			
			// point 1
			vertexList.add(buffer[3] + position.x + x);
			vertexList.add(buffer[4] + position.y + y);
			vertexList.add(buffer[5] + position.z + z);
			
			// point 2 (2 times for winding indicies....gotta keep em seperate cuz of normals and coords and stuff)
			for (int i = 0; i < 2; i ++) {
				vertexList.add(buffer[6] + position.x + x);
				vertexList.add(buffer[7] + position.y + y);
				vertexList.add(buffer[8] + position.z + z);
			}
			
			// point 3
			vertexList.add(buffer[9]  + position.x + x);
			vertexList.add(buffer[10] + position.y + y);
			vertexList.add(buffer[11] + position.z + z);
			
			// point 0 (again)
			vertexList.add(buffer[0] + position.x + x);
			vertexList.add(buffer[1] + position.y + y);
			vertexList.add(buffer[2] + position.z + z);
			
			// normal
			float normals[] = Cube.normals[side];
			normalList.add(normals[0]);
			normalList.add(normals[1]);
			normalList.add(normals[2]);
		} else {
			// yay, VBOs :D
			// TODO: index buffers, actually maybe not....dunno yet, oh well
			// 0, 1, 2, 2, 3, 0
			
			// point 0
			vertexList.add(buffer[0] + position.x + x);
			vertexList.add(buffer[1] + position.y + y);
			vertexList.add(buffer[2] + position.z + z);
			
			// point 1
			vertexList.add(buffer[3] + position.x + x);
			vertexList.add(buffer[4] + position.y + y);
			vertexList.add(buffer[5] + position.z + z);
			
			// point 2 (2 times for winding indicies....gotta keep em seperate cuz of normals and coords and stuff)
			for (int i = 0; i < 2; i ++) {
				vertexList.add(buffer[6] + position.x + x);
				vertexList.add(buffer[7] + position.y + y);
				vertexList.add(buffer[8] + position.z + z);
			}
			
			// point 3
			vertexList.add(buffer[9]  + position.x + x);
			vertexList.add(buffer[10] + position.y + y);
			vertexList.add(buffer[11] + position.z + z);
			
			// point 0 (again)
			vertexList.add(buffer[0] + position.x + x);
			vertexList.add(buffer[1] + position.y + y);
			vertexList.add(buffer[2] + position.z + z);			
		}
	}

	/**
	 * Prepares the chunk for rendering (display list or VBO)
	 * This is only called upon chunk creation and whenever a new cube is set in the chunk
	 * (as the displayList / VBO needs updated)
	 */
	private void preRenderChunk() {
		if (GL.isImmediateMode()) {
			System.out.println("Preparing the chunk Display List!");
			// generate a new display list, if one already exists, delete it and make a new one since
			// the block has been updated
			// a display list that = -1 was never set
			if (displayList != -1)
				GL.deleteDisplayList(displayList);
			displayList = GL.genDisplayList();
			glNewList(displayList, GL_COMPILE);
			glBegin(GL_TRIANGLES);
				// with display list, you have to re-do the whole displayList :(
				int size = vertexList.size();
				glColor3f(redValue, greenValue, blueValue);
				int normal = 0;
				for (int i = 0; i < size; i += 18) {					
					// ADD THE NORMAL	
					glNormal3f(normalList.get(normal), normalList.get(normal + 1), normalList.get(normal + 2));
					normal += 3;
					
					// and the verticies
					glVertex3f(vertexList.get(i), vertexList.get(i + 1), vertexList.get(i + 2));
					glVertex3f(vertexList.get(i + 3), vertexList.get(i + 4), vertexList.get(i + 5));
					glVertex3f(vertexList.get(i + 6), vertexList.get(i + 7), vertexList.get(i + 8));
					glVertex3f(vertexList.get(i + 9), vertexList.get(i + 10), vertexList.get(i + 11));
					glVertex3f(vertexList.get(i + 12), vertexList.get(i + 13), vertexList.get(i + 14));
					glVertex3f(vertexList.get(i + 15), vertexList.get(i + 16), vertexList.get(i + 17));					
				}
			glEnd();
			glEndList();
		} else {
			System.out.println("Preparing the chunk VBO!");
			
			// generate a VBO if we do not have one, 
			// if we already have one, we will just update it
			if (vertexBufferId == -1) {
				System.out.println("Creating VBO!");
				
				// create the buffer
				vertexBufferId = GL.genVBO();
				
				// prepare the buffer
				float array[] = new float[vertexList.size()];
				for (int i = 0; i < vertexList.size(); i ++)
					array[i] = vertexList.get(i);
				GL.prepareStaticVBO(vertexBufferId, Util.createBuffer(array));
				
			} else {
				System.out.println("Updating the VBO!");
				
				// todo
			}
		}
	}
	
	/**
	 * Renders the chunk
	 */
	public void render() {
		if (GL.isImmediateMode()) {
			// render the display list!
			glCallList(displayList);
		} else {
			// render VBO
			
			glColor3f(redValue, greenValue, blueValue);
			
			// enable drawing
			glEnableClientState(GL_VERTEX_ARRAY);
			
			// bind the VBO and tell openGL the vertex pointer offset
			GL.bindStaticBuffer(vertexBufferId);
			glVertexPointer(3, GL_FLOAT, 0, 0);
			
			// TODO: normals (lighting will look different ATM since normals are not sent)
			
			// draw!
			glDrawArrays(GL_TRIANGLES, 0, vertexList.size() / 3);
			
			// disable drawing
			glDisableClientState(GL_VERTEX_ARRAY);
		}
	}
	
	/**
	 * Sets the cube's material in a chunk
	 * @param position the local position of the cube to modify
	 * @param material the kind of material to use
	 */
	public void setCube(Vector3 position, byte material) {
		cubeList[(int)position.x][(int)position.y][(int)position.z] = material;
		
		// re-create the display list or VBO
		preRenderChunk();
	}
	
	/**
	 * Checks to see if the cube is transparent at the location provided
	 * @param x The local X position
	 * @param y The local Y position
	 * @param z The local Z position
	 * @return true if the cube is transparent at this location
	 */
	public boolean isTransparent(int x, int y, int z) {
		byte cube = cubeList[x][y][z];
		if (cube == Cube.AIR)
			return true;
		if (cube == Cube.WATER)
			return true;
		return false;
	}
	
	/**
	 * Gets the position of the chunk.
	 * @return the 3D position.
	 */
	public Vector3 getChunkPosition() {
		return position;
	}
	
}
