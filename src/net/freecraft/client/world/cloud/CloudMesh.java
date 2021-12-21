package net.freecraft.client.world.cloud;

import com.jogamp.opengl.GL2;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.render.Renderer;
import net.freecraft.client.util.mesh.MeshCompiler;
import net.freecraft.client.util.mesh.MeshUtil;
import net.freecraft.util.Vec2D;
import net.freecraft.util.Vec3D;

public class CloudMesh {
	public static final Vec3D[] POSITIONS = new Vec3D[] {
		new Vec3D(0.5f, 0.0f, -0.5f),
		new Vec3D(0.5f, 0.0f, 0.5f),
		new Vec3D(-0.5f, 0.0f, 0.5f),
		new Vec3D(-0.5f, 0.0f, -0.5f),
	};
	public static final Vec2D[] TEXCOORDS = new Vec2D[] {
		new Vec2D(0.0f, 0.0f),
		new Vec2D(0.0f, 1.0f),
		new Vec2D(1.0f, 1.0f),
		new Vec2D(1.0f, 0.0f),
	};
	private MeshCompiler gl;
	private int[] data;
	public CloudMesh() {
		data = new int[2];
		data[0] = -1;
	}
	public void dispose() {
		synchronized(gl) {
			if(data[0] != -1) {
				MeshUtil.delete(data[0]);
				data[0] = -1;
			}
		}
	}
	public void build() {
		//GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		gl = new MeshCompiler();
		GL2 ogl = FreeCraftClient.get().getRenderer().getGL2();
		synchronized(ogl) {
			dispose();
			//gl.glNewList(list, GL2.GL_COMPILE);
			gl.glBegin(GL2.GL_QUADS);
			int size = FreeCraftClient.get().getWorld().getViewDistanceInBlocks() * 2;
			for(int p = 0; p < 4; p++) {
				gl.glTexCoord2f((float)TEXCOORDS[p].getX() * size / 256, (float)TEXCOORDS[p].getY() * size / 256);
				gl.glVertex3f((float)POSITIONS[p].getX() * size, 128, (float)POSITIONS[p].getZ() * size);
			}
			for(int p = 0; p < 4; p++) {
				gl.glTexCoord2f((float)TEXCOORDS[p].getX() * size / 256, (float)TEXCOORDS[p].getY() * size / 256);
				gl.glVertex3f((float)POSITIONS[p].getX() * size, 132, (float)POSITIONS[p].getZ() * size);
			}
			this.data = gl.glEnd();
			//gl.glEndList();
		}
	}
	public void render() {
		Renderer renderer = FreeCraftClient.get().getRenderer();
		GL2 gl = renderer.getGL2();
		synchronized(gl) {
			renderer.beginCloudRendering();
			MeshUtil.render(data);
			renderer.endCloudRendering();
		}
	}
}
