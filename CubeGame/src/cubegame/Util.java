//-----------------------------------------------------------------------------
// Util.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
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
	 * Creates an int buffer for the given data, all rewound and everything.  Perfect for
	 * passing into OpenGL.
	 * @param data The int[] data to use
	 * @return The converted integer array to buffers
	 */
	public static IntBuffer createBuffer(int data[]) {
		return (IntBuffer)BufferUtils.createIntBuffer(data.length).put(data).rewind();
	}
	
	/**
	 * Creates an short buffer for the given data, all rewound and everything.  Perfect for
	 * passing into OpenGL.
	 * @param data The short[] data to use
	 * @return The converted integer array to buffers
	 */
	public static ShortBuffer createBuffer(short data[]) {
		return (ShortBuffer)BufferUtils.createShortBuffer(data.length).put(data).rewind();
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
