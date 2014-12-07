//-----------------------------------------------------------------------------
// Chunk.java
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

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;

import math.Vector3;
import static org.lwjgl.opengl.GL11.*;

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
	private Vector3 position;
	
	/**
	 * The display list id if display lists are used
	 */
	private int displayList = -1;
	
	private int vertexBufferId = -1;
	private int indexBufferId = -1;
	private int indexID = 0;
	
	/**
	 * Creates a chunk of the size CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE
	 * @param position the world position of the cube, representing position
	 * of cube inside of chunk at local position <0, 0, 0>
	 */
	public Chunk(Vector3 position) {
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
			
			// build texture coordinates
			Vector2f coords[] = Cube.getUVTextureMap(material, World.mapTextureWidth, World.mapTextureHeight);
			for (Vector2f textCord : coords) {
				textureList.add(textCord.x);
				textureList.add(textCord.y);
			}
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
			
			// point 2 
			vertexList.add(buffer[6] + position.x + x);
			vertexList.add(buffer[7] + position.y + y);
			vertexList.add(buffer[8] + position.z + z);
			
			// point 3
			vertexList.add(buffer[9]  + position.x + x);
			vertexList.add(buffer[10] + position.y + y);
			vertexList.add(buffer[11] + position.z + z);
			
			// index! (6 indicies, goes 0 1 2 2 3 0)
			indexList.add(indexID);
			indexList.add(indexID + 1);
			indexList.add(indexID + 2);
			indexList.add(indexID + 2);
			indexList.add(indexID + 3);
			indexList.add(indexID);
			indexID += 4;
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
				int normal = 0;
				int textCoords = 0;
				for (int i = 0; i < size; i += 18) {					
					// ADD THE NORMAL	
					glNormal3f(normalList.get(normal), normalList.get(normal + 1), normalList.get(normal + 2));
					normal += 3;
					
					// and the vertices and coords
					glTexCoord2f(textureList.get(textCoords), textureList.get(textCoords + 1));
					glVertex3f(vertexList.get(i), vertexList.get(i + 1), vertexList.get(i + 2));
					glTexCoord2f(textureList.get(textCoords + 2), textureList.get(textCoords + 3));
					glVertex3f(vertexList.get(i + 3), vertexList.get(i + 4), vertexList.get(i + 5));
					glTexCoord2f(textureList.get(textCoords + 4), textureList.get(textCoords + 5));
					glVertex3f(vertexList.get(i + 6), vertexList.get(i + 7), vertexList.get(i + 8));
					glTexCoord2f(textureList.get(textCoords + 6), textureList.get(textCoords + 7));
					glVertex3f(vertexList.get(i + 9), vertexList.get(i + 10), vertexList.get(i + 11));
					glTexCoord2f(textureList.get(textCoords + 8), textureList.get(textCoords + 9));
					glVertex3f(vertexList.get(i + 12), vertexList.get(i + 13), vertexList.get(i + 14));
					glTexCoord2f(textureList.get(textCoords + 10), textureList.get(textCoords + 11));
					glVertex3f(vertexList.get(i + 15), vertexList.get(i + 16), vertexList.get(i + 17));
					
					textCoords += 12;
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
				indexBufferId = GL.genVBO();
				
				// prepare the buffer for vertices
				float array[] = new float[vertexList.size()];
				for (int i = 0; i < vertexList.size(); i ++)
					array[i] = vertexList.get(i);
				GL.prepareStaticVBO(vertexBufferId, Util.createBuffer(array));
				
				// prepare the buffer for indices
				int indexArray[] = new int[indexList.size()];
				for (int i = 0; i < indexList.size(); i ++)
					indexArray[i] = indexList.get(i);
				GL.prepareStaticVBO(indexBufferId, Util.createBuffer(indexArray));
				System.out.println(indexList.toString());
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
		if (GL.isImmediateMode()) {
			// render the display list!
			glCallList(displayList);
		} else {
			// render VBO
			
			glColor3f(1.0f, 0.0f, 0.0f);
			
			// enable drawing
			glEnableClientState(GL_VERTEX_ARRAY);
			
			// bind the VBO and tell openGL the vertex pointer offset
			GL.bindStaticBuffer(vertexBufferId);
			glVertexPointer(3, GL_FLOAT, 0, 0);
			
			// TODO: normals (lighting will look different ATM since normals are not sent)
			
			// draw!
			//glDrawArrays(GL_TRIANGLES, 0, vertexList.size() / 3);
			GL.bindStaticIndexBuffer(indexBufferId);
			glDrawElements(GL_TRIANGLES, indexList.size(), GL_UNSIGNED_INT, 0);
			
			// disable drawing
			glDisableClientState(GL_VERTEX_ARRAY);
		}
	}
	
	/**
	 * Sets the cube's material in a chunk
	 * @param position the local position of the cube to modify
	 * @param material the kind of material to use
	 */
	public void setCube(Vector3 position, short material) {
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
