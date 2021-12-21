package net.freecraft.client.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;

import com.jogamp.opengl.GL2;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.util.Logger;
import net.freecraft.util.Vec3D;

public class GLSLShader {
	private int program;
	private int vertexShader, fragmentShader;
	private GL2 gl;
	public GLSLShader(String vertexPath, String fragmentPath) {
		gl = FreeCraftClient.get().getRenderer().getGL2();
		this.program = gl.glCreateProgram();
		this.vertexShader = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		this.fragmentShader = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
		String src = loadShader(vertexPath);
		IntBuffer buf = IntBuffer.allocate(1);
		buf.put(src.length());
		buf.rewind();
		gl.glShaderSource(vertexShader, 1, new String[] {src}, buf);
		
		src = loadShader(fragmentPath);
		buf = IntBuffer.allocate(1);
		buf.put(src.length());
		buf.rewind();
		gl.glShaderSource(fragmentShader, 1, new String[] {src}, buf);
		
		gl.glCompileShader(vertexShader);
		gl.glCompileShader(fragmentShader);
		
		buf = IntBuffer.allocate(1);
		ByteBuffer str = ByteBuffer.allocate(1024);
		gl.glGetShaderInfoLog(vertexShader, 1024, buf, str);
		String msg = StandardCharsets.UTF_8.decode(str).toString();
		if(msg.trim().length() > 0) {
			Logger.error(msg);
		}
		
		buf = IntBuffer.allocate(1);
		str = ByteBuffer.allocate(1024);
		gl.glGetShaderInfoLog(fragmentShader, 1024, buf, str);
		msg = StandardCharsets.UTF_8.decode(str).toString();
		if(msg.trim().length() > 0) {
			Logger.error(msg);
		}
		
		gl.glAttachShader(program, vertexShader);
		gl.glAttachShader(program, fragmentShader);
		
		gl.glLinkProgram(program);
		gl.glValidateProgram(program);
		
		gl.glUseProgram(program);
		
		int loc = gl.glGetUniformLocation(program, "blockAtlas");
		gl.glUniform1i(loc, 0);
		loc = gl.glGetUniformLocation(program, "itemAtlas");
		gl.glUniform1i(loc, 1);
		loc = gl.glGetUniformLocation(program, "cloudTexture");
		gl.glUniform1i(loc, 2);
	}
	public int getProgramHandle() {
		return program;
	}
	public String loadShader(String path) {
		try {
			String src = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
			String line;
			while((line = br.readLine()) != null) {
				if(line.startsWith("#include ")) {
					src += loadShader(line.substring(9).trim()) + '\n';
				}else{
					src += line + '\n';
				}
			}
			return src;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public void dispose() {
		gl.glUseProgram(0);
		
		gl.glDetachShader(program, vertexShader);
		gl.glDetachShader(program, fragmentShader);
		
		gl.glDeleteShader(vertexShader);
		gl.glDeleteShader(fragmentShader);
		
		gl.glDeleteProgram(program);
	}
	public void updateTime(float time) {
		int loc = gl.glGetUniformLocation(program, "time");
		gl.glUniform1f(loc, time);
	}
	public void updateProjectionMatrix(float[] mat) {
		int loc = gl.glGetUniformLocation(program, "projectionMatrix");
		FloatBuffer buf = FloatBuffer.allocate(16);
		buf.put(mat);
		buf.flip();
		gl.glUniformMatrix4fv(loc, 1, false, buf);
	}
	public void updateModelviewMatrix(float[] mat) {
		int loc = gl.glGetUniformLocation(program, "modelviewMatrix");
		FloatBuffer buf = FloatBuffer.allocate(16);
		buf.put(mat);
		buf.flip();
		gl.glUniformMatrix4fv(loc, 1, false, buf);
	}
	public void updateFog(float density, float[] color) {
		int loc = gl.glGetUniformLocation(program, "fogDensity");
		gl.glUniform1f(loc, density);
		loc = gl.glGetUniformLocation(program, "fogColor");
		gl.glUniform4f(loc, color[0], color[1], color[2], color[3]);
	}
	public void updateViewPos(Vec3D v) {
		int loc = gl.glGetUniformLocation(program, "viewPos");
		gl.glUniform3f(loc, (float)v.getX(), (float)v.getY(), (float)v.getZ());
	}
	public void updateLightDir(Vec3D v) {
		int loc = gl.glGetUniformLocation(program, "lightDir");
		gl.glUniform3f(loc, (float)v.getX(), (float)v.getY(), (float)v.getZ());
	}
}
