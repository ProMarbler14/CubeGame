//-----------------------------------------------------------------------------
// GLTest.java
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
