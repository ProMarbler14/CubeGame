//-----------------------------------------------------------------------------
// Util.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Util {
	/**
	 * Creates a float buffer for the given data, all rewound and everything. Perfect for
	 * passing into OpenGL.
	 * @param data The float[] data to use
	 * @return A FloatBuffer that *doesn't* make you want to eviscerate the creators of nio
	 */
	public static FloatBuffer createBuffer(float data[]) {
		//Fuck nio for everything except their one-liners
		return (FloatBuffer)BufferUtils.createFloatBuffer(data.length).put(data).rewind();
	}
	
	/**
	 * Loads a texture
	 * @param file the image file to load
	 * @param extension the file extension to load
	 * @return the texture object, or null if one could not be created
	 */
	public static Texture loadTexture(String file, String extension) {
		Texture text = null;
		try {
			File f = new File(file);
			FileInputStream stream = new FileInputStream(f);
			text = TextureLoader.getTexture(extension, stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File " + file + " is not found!");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("loadTexture() io error.");
		}
		return text;
	}
}
