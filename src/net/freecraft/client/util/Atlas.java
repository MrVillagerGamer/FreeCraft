package net.freecraft.client.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.util.RegistryEntry;

public class Atlas extends RegistryEntry {
	private int tex;
	private BufferedImage img;
	public Atlas(String path) {
		try {
			img = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void dispose() {
		GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		IntBuffer buf = IntBuffer.allocate(1);
		buf.put(tex);
		buf.rewind();
		gl.glDeleteTextures(1, buf);
	}
	public void load() {
		GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		IntBuffer texbuf = IntBuffer.allocate(1);
		gl.glGenTextures(1, texbuf);
		tex = texbuf.get(0);
		try {
			TextureData textureData = AWTTextureIO.newTextureData(gl.getGLProfile(), img, true);
			int[] pixels = new int[img.getWidth()*img.getHeight()];
			for(int x = 0; x < img.getWidth(); x++) {
				for(int y = 0; y < img.getHeight(); y++) {
					pixels[y*img.getWidth()+x] = img.getRGB(x, y);
				}
			}
			IntBuffer imgbuf = IntBuffer.allocate(pixels.length);
			imgbuf.put(pixels);
			imgbuf.flip();
			gl.glBindTexture(GL.GL_TEXTURE_2D, tex);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, textureData.getInternalFormat(), textureData.getWidth(), textureData.getHeight(), 0, textureData.getPixelFormat(), textureData.getPixelType(), textureData.getBuffer());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public BufferedImage getAWTImage() {
		return img;
	}
	public int getGLTexture() {
		return tex;
	}
	public int getSizeInTiles() {
		return 16;
	}
	public int getSizeOfTile() {
		return 16;
	}
}
