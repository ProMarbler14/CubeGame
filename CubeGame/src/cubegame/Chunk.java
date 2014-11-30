//-----------------------------------------------------------------------------
// Chunk.java
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

import java.util.ArrayList;

import math.Vector3;
import static org.lwjgl.opengl.GL11.*;

public class Chunk {	
	public final int CHUNK_SIZE = 16;
	
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
					// for now, just one layer of dirt
					//if (y == 0)
						cubeList[x][y][z] = Cube.DIRT;
					//else
						//cubeList[x][y][z] = Cube.AIR;
				}
			}
		}
		
		System.out.println("Creating a new chunk");
		
		// construct the list
		buildVertexList();
		
		// prep it (build display list or vbo)
		preRenderChunk();
	}
	
	/**
	 * builds the vertex list for the chunk
	 */
	private void buildVertexList() {
		long timeStart = Time.getTime();
		
		vertexList.clear();
		normalList.clear();
		
		// build an optimized, culled vertex list
		byte cube;
		float vertex[];
		float normal[];
		for (int x = 0; x < CHUNK_SIZE; x ++) {
			for (int z = 0; z < CHUNK_SIZE; z ++) {
				for (int y = 0; y < CHUNK_SIZE; y ++) {
					cube = cubeList[x][y][z];
					
					if (cube == Cube.DIRT) {
						// do we have an invisible block above us (or end of chunk)?  
						// If we do, then push the vertices
						
						// top face
						if (((y + 1) == CHUNK_SIZE) || isTransparent(x, y+1, z)) {
							vertex = Cube.vertices[Cube.FACE_TOP];
							for (int i = 0; i < 12; i += 3) {
								vertexList.add(vertex[i    ] + position.x + (float)x);
								vertexList.add(vertex[i + 1] + position.y + (float)y);
								vertexList.add(vertex[i + 2] + position.z + (float)z);
							}
							
							// add normal
							normal = Cube.normals[Cube.FACE_TOP];
							normalList.add(normal[0]);
							normalList.add(normal[1]);
							normalList.add(normal[2]);
						}
						
						// compute bottom face
						if ((y == 0) || isTransparent(x, y-1, z)) {
							vertex = Cube.vertices[Cube.FACE_BOTTOM];
							for (int i = 0; i < 12; i += 3) {
								vertexList.add(vertex[i    ] + position.x + (float)x);
								vertexList.add(vertex[i + 1] + position.y + (float)y);
								vertexList.add(vertex[i + 2] + position.z + (float)z);
							}
							
							// add normal
							normal = Cube.normals[Cube.FACE_BOTTOM];
							normalList.add(normal[0]);
							normalList.add(normal[1]);
							normalList.add(normal[2]);							
						}
						
						// compute left face
						if ((x == 0) || isTransparent(x-1, y, z)) {
							vertex = Cube.vertices[Cube.FACE_LEFT];
							for (int i = 0; i < 12; i += 3) {								
								vertexList.add(vertex[i    ] + position.x + (float)x);
								vertexList.add(vertex[i + 1] + position.y + (float)y);
								vertexList.add(vertex[i + 2] + position.z + (float)z);
							}
						
							// add normal
							normal = Cube.normals[Cube.FACE_LEFT];
							normalList.add(normal[0]);
							normalList.add(normal[1]);
							normalList.add(normal[2]);								
						}
						
						// Compute right face
						if (((x + 1) == CHUNK_SIZE) || isTransparent(x+1, y, z)) {
							vertex = Cube.vertices[Cube.FACE_RIGHT];
							for (int i = 0; i < 12; i += 3) {
								vertexList.add(vertex[i    ] + position.x + (float)x);
								vertexList.add(vertex[i + 1] + position.y + (float)y);
								vertexList.add(vertex[i + 2] + position.z + (float)z);
							}
						
							// add normal
							normal = Cube.normals[Cube.FACE_RIGHT];
							normalList.add(normal[0]);
							normalList.add(normal[1]);
							normalList.add(normal[2]);								
						}
						
						// front face
						if (((z + 1) == CHUNK_SIZE) || isTransparent(x, y, z + 1)) {
							vertex = Cube.vertices[Cube.FACE_FRONT];
							for (int i = 0; i < 12; i += 3) {
								vertexList.add(vertex[i    ] + position.x + (float)x);
								vertexList.add(vertex[i + 1] + position.y + (float)y);
								vertexList.add(vertex[i + 2] + position.z + (float)z);
							}
						
							// add normal
							normal = Cube.normals[Cube.FACE_FRONT];
							normalList.add(normal[0]);
							normalList.add(normal[1]);
							normalList.add(normal[2]); 
						}
						
						// back face
						if ((z == 0) || isTransparent(x, y, z - 1)) {
							vertex = Cube.vertices[Cube.FACE_BACK];
							for (int i = 0; i < 12; i += 3) {
								vertexList.add(vertex[i    ] + position.x + (float)x);
								vertexList.add(vertex[i + 1] + position.y + (float)y);
								vertexList.add(vertex[i + 2] + position.z + (float)z);
							}
						
							// add normal
							normal = Cube.normals[Cube.FACE_BACK];
							normalList.add(normal[0]);
							normalList.add(normal[1]);
							normalList.add(normal[2]); 							
						}
					}
				}
			}
		}
		
		int t = (int)(Time.getTime() - timeStart);
		System.out.println("Took " + t + "ms to generate the chunk");
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
			glBegin(GL_QUADS);
				// with display list, you have to re-do the whole displayList :(
				int size = vertexList.size();
				glColor3f(1.0f, 0.0f, 0.0f);
				int normal = 0;
				for (int i = 0; i < size; i += 12) {
					// ADD THE NORMAL
					glNormal3f(normalList.get(normal), normalList.get(normal + 1), normalList.get(normal + 2));
					normal += 3;
					
					// and the verticies
					glVertex3f(vertexList.get(i), vertexList.get(i + 1), vertexList.get(i + 2));
					glVertex3f(vertexList.get(i + 3), vertexList.get(i + 4), vertexList.get(i + 5));
					glVertex3f(vertexList.get(i + 6), vertexList.get(i + 7), vertexList.get(i + 8));
					glVertex3f(vertexList.get(i + 9), vertexList.get(i + 10), vertexList.get(i + 11));
				}
			glEnd();
			glEndList();
		} else {
			System.out.println("Preparing the chunk VBO!");
			// no VBO yet, TODO (only edit part of VBO that needs changed, not all of it! :)
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
}
