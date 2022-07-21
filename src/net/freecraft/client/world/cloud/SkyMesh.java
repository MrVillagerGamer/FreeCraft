package net.freecraft.client.world.cloud;

import com.jogamp.opengl.GL2;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.render.Renderer;
import net.freecraft.client.util.mesh.MeshUtil;
import net.freecraft.util.Vec3D;

public class SkyMesh {
	private int[] data;
	public SkyMesh() {
		data = new int[2];
		data[0] = -1;
	}
	public void dispose() {
		Renderer renderer = FreeCraftClient.get().getRenderer();
		GL2 gl = renderer.getGL2();
		synchronized(gl) {
			if(data[0] != -1) {
				MeshUtil.delete(data[0]);
				data[0] = -1;
			}
		}
	}
	public void build() {
		Renderer renderer = FreeCraftClient.get().getRenderer();
		GL2 gl = renderer.getGL2();
		synchronized(gl) {
			data = MeshUtil.loadCube(new Vec3D(4, 4, 4), new Vec3D(-2, -2, -2));
		}
	}
	public void render() {
		Renderer renderer = FreeCraftClient.get().getRenderer();
		GL2 gl = renderer.getGL2();
		synchronized(gl) {
			renderer.beginSkyRendering();
			MeshUtil.render(data);
			renderer.endSkyRendering();
		}
	}
}
