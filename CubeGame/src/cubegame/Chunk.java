//-----------------------------------------------------------------------------
// Chunk.java
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

import graphics.GL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;

public class Chunk {	
	public final int CHUNK_SIZE = 16;
	
	/**
	 * cubeList holds each cube's type (grass, air, ect)
	 */
	private short[][][] cubeList;
	
	/**
	 * vertexList holds the vertices that are used for rendering the chunk
	 */
	private ArrayList<Float> vertexList;
	
	/**
	 * normalList holds the normals that are used for rendering the chunk
	 */
	private ArrayList<Float> normalList; 
	
	/**
	 * Texture coord list
	 */
	private ArrayList<Float> textureList;
	
	/**
	 * Index list for index'd triangles
	 */
	private ArrayList<Integer> indexList;
	
	/**
	 * The global world position of the chunk
	 */
	private Vector3f position;
	
	/**
	 * The display list id if display lists are used
	 */
	private int displayList = -1;
	
	/**
	 * The Vertex Buffer Object ID that is exposed from openGL
	 */
	private int vertexBufferId = -1;
	
	/**
	 * The Index Buffer Object ID that is exposed from openGL
	 */
	private int indexBufferId = -1;
	
	/**
	 * The indexID when filling the index buffer.  Please note, that this
	 * is also used to act as the highest number inside of the index list
	 * and is used when actually drawing the elements for the max. index id used
	 */
	private int indexID = 0;
	
	// buffers for legacy openGL
	private FloatBuffer vertexArrayBuffer;
	private FloatBuffer normalArrayBuffer;
	private FloatBuffer textureArrayBuffer;
	private IntBuffer indexArrayBuffer;
	
	/**
	 * Creates a chunk of the size CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE
	 * @param position the world position of the cube, representing position
	 * of cube inside of chunk at local position <0, 0, 0>
	 */
	public Chunk(Vector3f position) {
		// someone tell me to optimize this before your computer runs out of ram
		vertexList = new ArrayList<Float>();
		normalList = new ArrayList<Float>();
		textureList = new ArrayList<Float>();
		indexList = new ArrayList<Integer>();
		
		cubeList = new short[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
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
					if (y == CHUNK_SIZE - 1)
						cubeList[x][y][z] = Cube.COBBLE;
					else
						cubeList[x][y][z] = Cube.DIRT;
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
		short material;
		for (x = 0; x < CHUNK_SIZE; x ++) {
			for (z = 0; z < CHUNK_SIZE; z ++) {
				for (y = 0; y < CHUNK_SIZE; y ++) {
					material = cubeList[x][y][z];
					// if it is air, we render no sides!
					if (material == Cube.AIR)
						continue;

					// back face
					if (z == 0 || isTransparent(x, y, z - 1))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_BACK, material);
					
					// front face
					if ((z + 1 == CHUNK_SIZE) || isTransparent(x, y, z + 1))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_FRONT, material);
					
					// left face
					if (x == 0 || isTransparent(x - 1, y, z))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_LEFT, material);
					
					// right face
					if ((x + 1 == CHUNK_SIZE) || isTransparent(x + 1, y, z))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_RIGHT, material);
					
					// bottom face
					if (y == 0 || isTransparent(x, y - 1, z))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_BOTTOM, material);
					
					// top face
					if ((y + 1 == CHUNK_SIZE) || isTransparent(x, y + 1, z))
						buildFace((float)x, (float)y, (float)z, Cube.FACE_TOP, material);
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
	 * @param material the material to render on the face
	 */
	private void buildFace(float x, float y, float z, int side, short material) {
		float buffer[] = Cube.vertices[side];
		
		// normal
		float normals[] = Cube.normals[side];
		normalList.add(normals[0]);
		normalList.add(normals[1]);
		normalList.add(normals[2]);
		
		// build texture coordinates
		Vector2f coords[] = Cube.getUVTextureMapD(material, World.mapTextureWidth, World.mapTextureHeight);
		for (Vector2f textCord : coords) {
			textureList.add(textCord.x);
			textureList.add(textCord.y);
		}
		
		// point 0
		vertexList.add(buffer[0] + position.x + x);
		vertexList.add(buffer[1] + position.y + y);
		vertexList.add(buffer[2] + position.z + z);
		
		// point 1
		vertexList.add(buffer[3] + position.x + x);
		vertexList.add(buffer[4] + position.y + y);
		vertexList.add(buffer[5] + position.z + z);
		
		// point 2 
		vertexList.add(buffer[6] + position.x + x);
		vertexList.add(buffer[7] + position.y + y);
		vertexList.add(buffer[8] + position.z + z);
		
		// point 3
		vertexList.add(buffer[9]  + position.x + x);
		vertexList.add(buffer[10] + position.y + y);
		vertexList.add(buffer[11] + position.z + z);
		
		// index! (6 indices, goes 0 1 2 2 3 0)
		indexList.add(indexID);
		indexList.add(indexID + 1);
		indexList.add(indexID + 2);
		indexList.add(indexID + 2);
		indexList.add(indexID + 3);
		indexList.add(indexID);
		indexID += 4;
	}

	/**
	 * Prepares the chunk for rendering (display list or VBO)
	 * This is only called upon chunk creation and whenever a new cube is set in the chunk
	 * (as the displayList / VBO needs updated)
	 */
	private void preRenderChunk() {
		if (GL.isLegacy()) {
			System.out.println("Legacy GL: Preparing the chunk Vertex Array!");
			
			// vertex array
			float vertexArray[] = new float[vertexList.size()];
			for (int i = 0; i < vertexList.size(); i ++)
				vertexArray[i] = vertexList.get(i);
			
			// index array
			int indexArray[] = new int[indexList.size()];
			for (int i = 0; i < indexList.size(); i ++)
				indexArray[i] = indexList.get(i);
			
			// normal array
			int normalIndex = 0;
			int tracker = 0;
			float normalArray[] = new float[normalList.size() * 4];
			for (int i = 0; i < normalList.size() * 4; i += 3) {
				normalArray[i]     = normalList.get(normalIndex);
				normalArray[i + 1] = normalList.get(normalIndex + 1);
				normalArray[i + 2] = normalList.get(normalIndex + 2);
				
				// we have to duplicate normals for 4 verts
				tracker ++;
				if (tracker == 4) {
					normalIndex += 3;
					tracker = 0;
				}
			}
			
			// texture array
			float textureArray[] = new float[textureList.size()];
			for (int i = 0; i < textureList.size(); i ++)
				textureArray[i] = textureList.get(i);
			
			// now create the buffers
			vertexArrayBuffer = Util.createBuffer(vertexArray);
			normalArrayBuffer = Util.createBuffer(normalArray);
			textureArrayBuffer = Util.createBuffer(textureArray);
			indexArrayBuffer = Util.createBuffer(indexArray);
		} else {
			System.out.println("Preparing the chunk VBO!");
			
			// generate a VBO if we do not have one, 
			// if we already have one, we will just update it
			if (vertexBufferId == -1) {
				System.out.println("Creating VBO!");
				
				// create the buffer
				vertexBufferId = GL.genVBO();
				indexBufferId = GL.genVBO();
				
				// prepare the vertex buffer
				int bufferSize = vertexList.size() + (normalList.size() * 4) + textureList.size();
				float buffer[] = new float[bufferSize];
				int index = 0;
				int normalIndex = 0;
				int textureIndex = 0;
				int tracker = 0;
				for (int i = 0; i < bufferSize; i += 8) {
					// first the vertex
					buffer[i]     = vertexList.get(index);
					buffer[i + 1] = vertexList.get(index + 1);
					buffer[i + 2] = vertexList.get(index + 2);
					
					// and now, the normal
					buffer[i + 3] = normalList.get(normalIndex);
					buffer[i + 4] = normalList.get(normalIndex + 1);
					buffer[i + 5] = normalList.get(normalIndex + 2);
					
					// and now the textures!
					buffer[i + 6] = textureList.get(textureIndex);
					buffer[i + 7] = textureList.get(textureIndex + 1);
					
					index += 3;
					textureIndex += 2;
					
					// we have to duplicate normals for 4 verts
					tracker ++;
					if (tracker == 4) {
						normalIndex += 3;
						tracker = 0;
					}
				}
				GL.prepareStaticVBO(vertexBufferId, Util.createBuffer(buffer));
				
				// prepare the buffer for indices
				int indexArray[] = new int[indexList.size()];
				for (int i = 0; i < indexList.size(); i ++)
					indexArray[i] = indexList.get(i);
				GL.prepareStaticVBO(indexBufferId, Util.createBuffer(indexArray));
			} else {
				System.out.println("Updating the VBO!");
				
				// TODO
			}
		}
	}
	
	/**
	 * Renders the chunk
	 */
	public void render() {
		if (GL.isLegacy()) {
			glVertexPointer(3, 0, vertexArrayBuffer); // 3 floats per vertex, not interleaved
			glNormalPointer(0, normalArrayBuffer); // not interleaved
			glTexCoordPointer(2, 0, textureArrayBuffer); // 2 floats per vertex, not interleaved

			glDrawElements(GL_TRIANGLES, indexArrayBuffer);
		} else {
			// render VBO			
			// bind the VBO and tell openGL the vertex pointer offset
			GL.bindStaticBuffer(vertexBufferId);
			
			// 32 bytes per vertex
			// sends 3 vertex floats, then 3 normal floats, then 2 texcoord floats
			// (that's how we get 0, 12, 24 all out of 32)
			glVertexPointer(3, GL_FLOAT, 32, 0);
			glNormalPointer(GL_FLOAT, 32, 12);
			glTexCoordPointer(2, GL_FLOAT, 32, 24);
			
			// draw!
			GL.bindStaticIndexBuffer(indexBufferId);
			glDrawRangeElements(GL_TRIANGLES, 0, indexID, indexList.size(), GL_UNSIGNED_INT, 0);
		}
	}
	
	/**
	 * Sets the cube's material in a chunk
	 * @param position the local position of the cube to modify
	 * @param material the kind of material to use
	 */
	public void setCube(Vector3f position, short material) {
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
		short cube = cubeList[x][y][z];
		if (cube == Cube.AIR)
			return true;
		if (cube == Cube.WATER)
			return true;
		return false;
	}
}
