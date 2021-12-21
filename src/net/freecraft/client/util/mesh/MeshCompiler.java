package net.freecraft.client.util.mesh;

import java.util.ArrayList;

public class MeshCompiler {
	private ArrayList<Float> data = new ArrayList<>();
	private float r = 0, g = 0, b = 0, s = 0, t = 0, u = 0, v = 1, w = 0;
	public void glBegin(int mode) {}
	public void glNormal3f(float u, float v, float w) {
		this.u = u;
		this.v = v;
		this.w = w;
	}
	public void glColor3f(float r, float g, float b) {
		this.r = r; this.g = g; this.b = b;
	}
	public void glTexCoord2f(float s, float t) {
		this.s = s; this.t = t;
	}
	public void glVertex3f(float x, float y, float z) {
		this.data.add(x);
		this.data.add(y);
		this.data.add(z);
		this.data.add(u);
		this.data.add(v);
		this.data.add(w);
		this.data.add(r);
		this.data.add(g);
		this.data.add(b);
		this.data.add(s);
		this.data.add(t);
	}
	public int[] glEnd() {
		float[] fdata = new float[this.data.size()];
		Float[] data = this.data.toArray(new Float[this.data.size()]);
		for(int i = 0; i < data.length; i++) {
			fdata[i] = data[i];
		}
		this.data.clear();
		this.data = null;
		data = null;
		return MeshUtil.load(fdata);
	}
}
