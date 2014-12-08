//-----------------------------------------------------------------------------
// GLTest.java
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
package test;

import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;

import cubegame.Cube;
import cubegame.Time;

public class GLTest {
	
	private static ArrayList<Integer> displayList = new ArrayList<Integer>();
	
	public static void prepareImmediateMode() {
		long t = Time.getTime();
		
		int list = glGenLists(1);
		
		glNewList(list, GL_COMPILE);
			glBegin(GL_QUADS);
			{
				// render each cube
				for (int x = 0; x < 16; x ++) {
					for (int z = 0; z < 16; z ++) {
						for (int y = 0; y < 16; y ++) {
							float colorX = 1.0f - (((float)x) / 255.0f) * 1.0f;
							float colorY = 1.0f - (((float)y) / 255.0f) * 2.0f;
							float colorZ = 1.0f - (((float)z) / 255.0f) * 3.0f;
							glColor3f(colorX, colorY, colorZ);
							// render each face
							for (float []vert : Cube.vertices) {
								// render each vertex
								for (int i = 0; i < 12; i += 3) {
									// render each coord
									glVertex3f(vert[i] + x, vert[i + 1] + y, vert[i + 2] + z);
								}
							}
						}
					}
				}
			}
			glEnd();
		glEndList();
		
		displayList.add(list);
		
		long elapsed = Time.getTime() - t;
		System.out.println("System took " + elapsed + "ms to create the display list");
	}
	
	public static void renderImmediateMod() {
		for (int id : displayList) {
			glCallList(id);
		}
	}
	
	public static void cleanUpImmediateMode() {
		for (int id : displayList) {
			glDeleteLists(id, 1);
		}
	}
}
