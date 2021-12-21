package net.freecraft.client.util.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.world.ChunkMesh;
import net.freecraft.util.Vec3D;

public class MeshUtil {
	public static void render(int[] data) {
		GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		synchronized(gl) {
			gl.glEnableVertexAttribArray(0);
			gl.glEnableVertexAttribArray(1);
			gl.glEnableVertexAttribArray(2);
			gl.glEnableVertexAttribArray(3);
			
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, data[0]);
			
			gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 11 * 4, 0 * 4);
			gl.glVertexAttribPointer(1, 3, GL2.GL_FLOAT, false, 11 * 4, 3 * 4);
			gl.glVertexAttribPointer(2, 3, GL2.GL_FLOAT, false, 11 * 4, 6 * 4);
			gl.glVertexAttribPointer(3, 2, GL2.GL_FLOAT, false, 11 * 4, 9 * 4);
			
			gl.glDrawArrays(GL2.GL_QUADS, 0, data[1]);
			
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
			
			gl.glDisableVertexAttribArray(0);
			gl.glDisableVertexAttribArray(1);
			gl.glDisableVertexAttribArray(2);
			gl.glDisableVertexAttribArray(3);
		}
	}
	public static int[] load(float[] data) {
		GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		synchronized(gl) {
			IntBuffer ibuf = IntBuffer.allocate(1);
			ibuf.put(0);
			ibuf.flip();
			gl.glGenBuffers(1, ibuf);
			ibuf.rewind();
			int vbo = ibuf.get();
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo);
			FloatBuffer buf = FloatBuffer.allocate(data.length);
			buf.put(data);
			buf.flip();
			gl.glBufferData(GL2.GL_ARRAY_BUFFER, 4 * data.length, buf, GL2.GL_STATIC_DRAW);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
			return new int[] {vbo, data.length / 11};
		}
	}
	public static int[] loadCube(Vec3D s, Vec3D o) {
		MeshCompiler gl = new MeshCompiler();
		gl.glBegin(GL2.GL_QUADS);
		for(int f = 0; f < 6; f++) {
			for(int p = 0; p < 4; p++) {
				Vec3D pos = ChunkMesh.POSITIONS[ChunkMesh.INDICES[f][p]];
				pos = pos.mul(s);
				pos = pos.add(o);
				gl.glVertex3f((float)pos.getX(), (float)pos.getY(), (float)pos.getZ());
			}
		}
		return gl.glEnd();
	}
	public static void delete(int vbo) {
		IntBuffer ibuf = IntBuffer.allocate(1);
		ibuf.put(vbo);
		ibuf.flip();
		GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		synchronized(gl) {
			gl.glDeleteBuffers(1, ibuf);
		}
	}
}
