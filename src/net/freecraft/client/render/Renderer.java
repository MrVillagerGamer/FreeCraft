package net.freecraft.client.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;

import com.jogamp.opengl.DefaultGLCapabilitiesChooser;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.awt.AWTGLReadBufferUtil;

import net.freecraft.FreeCraft;
import net.freecraft.block.Block;
import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.util.Atlas;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.entity.Entity;
import net.freecraft.util.Logger;
import net.freecraft.util.Registries;
import net.freecraft.util.Vec3D;
import net.freecraft.world.World;

public class Renderer {
	private GLProfile profile;
	private GLCapabilities caps;
	private GLAutoDrawable drawable;
	private GL2 gl;
	//private GLU glu;
	private boolean inited = false;
	private GLSLShader shader, cloudShader, waterShader, skyShader;
	private float[] skyColor;
	private Vec3D sunDir;
	public Renderer() {
		Logger.debug("Starting renderer...");
		profile = GLProfile.getDefault();
		caps = new GLCapabilities(profile);
		caps.setHardwareAccelerated(true);
		caps.setDepthBits(24);
		caps.setDoubleBuffered(false);
		caps.setOnscreen(false);
		
		GLDrawableFactory factory = GLDrawableFactory.getFactory(profile);
		drawable = factory.createOffscreenAutoDrawable(factory.getDefaultDevice(), caps, new DefaultGLCapabilitiesChooser(), FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight());
		drawable.display();
		drawable.getContext().makeCurrent();
		gl = drawable.getGL().getGL2();
		//glu = new GLU();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_FASTEST);
		gl.glHint(GL2.GL_POINT_SMOOTH_HINT, GL2.GL_FASTEST);
		gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_FASTEST);
		//gl.glEnable(GL2.GL_BLEND);
		//gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glViewport(0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight());
		skyColor = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
		gl.glClearColor(skyColor[0], skyColor[1], skyColor[2], skyColor[3]);
		gl.glEnable(GL2.GL_FOG);
		FloatBuffer fogColor = FloatBuffer.allocate(4);
		fogColor.put(skyColor);
		fogColor.rewind();
		gl.glFogfv(GL2.GL_FOG_COLOR, fogColor);
		gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_EXP2);
		gl.glFogf(GL2.GL_FOG_DENSITY, 0.05f);
		prepareFrame();
		inited = true;
	}
	public void loadShaders() {
		shader = new GLSLShader("/shader_vertex.txt", "/shader_fragment.txt");
		cloudShader = new GLSLShader("/cloud_shader_vertex.txt", "/cloud_shader_fragment.txt");
		waterShader = new GLSLShader("/water_shader_vertex.txt", "/water_shader_fragment.txt");
		skyShader = new GLSLShader("/sky_shader_vertex.txt", "/sky_shader_fragment.txt");
	}
	public void load() {
		loadShaders();
		for(Atlas atlas : ClientRegistries.ATLASES.getAll()) {
			atlas.load();
		}
		for(Atlas atlas : ClientRegistries.ATLASES.getAll()) {
			gl.glActiveTexture(GL2.GL_TEXTURE0+atlas.getId());
			gl.glBindTexture(GL2.GL_TEXTURE_2D, atlas.getGLTexture());
		}
	}
	private int viewEntityId;
	public void dispose() {
		Logger.debug("Stopping renderer...");
		if(shader != null) shader.dispose();
		if(cloudShader != null) cloudShader.dispose();
		if(waterShader != null) waterShader.dispose();
		if(skyShader != null) skyShader.dispose();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		for(Atlas atlas : ClientRegistries.ATLASES.getAll()) {
			atlas.dispose();
		}
		drawable.getContext().release();
		drawable.getContext().destroy();
		drawable.setRealized(false);
	}
	public void beginCloudRendering() {
		gl.glUseProgram(cloudShader.getProgramHandle());
		setupCloudModelview();
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL2.GL_CULL_FACE);
	}
	public void endCloudRendering() {
		gl.glUseProgram(shader.getProgramHandle());
		setupModelview();
		gl.glDisable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_CULL_FACE);
	}
	private void prepareFrame() {
		World world = FreeCraftClient.get().getWorld();
		sunDir = FreeCraftClient.get().getWorld().getSunDir();
		float br = (float)(Math.signum(sunDir.getY()) * Math.pow(Math.abs(sunDir.getY()), 0.5f) * 0.5f + 0.5f);
		Matrix4f mat = new Matrix4f()
				.identity()
				.perspective(70, FreeCraftClient.get().getGLWidth() / (float)FreeCraftClient.get().getGLHeight(), 0.01f, 1000.0f);
		float[] arr = new float[16];
		mat.get(arr);
		Entity cam = getViewEntity();
		Vec3D pos = FreeCraftClient.get().getWorld().getSpawnPos();
		if(cam != null) {
			pos = cam.getPos();
		}
		Block b = Registries.BLOCKS.get(world.getBlock(pos.toBlockPos()));
		float density = (float) ((0.01f/(world.getViewDistance()/8.0f))*(1-b.getFogDensity(pos))+0.05f*b.getFogDensity(pos));
		float[] color = new float[] {(float)b.getFogColor(pos).getX() * br, (float)b.getFogColor(pos).getY() * br, (float)b.getFogColor(pos).getZ() * br, 1};
		if(cloudShader != null) {
			gl.glUseProgram(cloudShader.getProgramHandle());
			cloudShader.updateTime(world.getTime()/20.0f);
			cloudShader.updateFog(density, color);
			cloudShader.updateProjectionMatrix(arr);
			cloudShader.updateLightDir(sunDir);
			cloudShader.updateViewPos(pos);
		}
		if(skyShader != null) {
			gl.glUseProgram(skyShader.getProgramHandle());
			skyShader.updateTime(world.getTime()/20.0f);
			skyShader.updateFog(density, color);
			skyShader.updateProjectionMatrix(arr);
			skyShader.updateLightDir(sunDir);
			skyShader.updateViewPos(pos);
		}
		if(waterShader != null) {
			gl.glUseProgram(waterShader.getProgramHandle());
			waterShader.updateTime(world.getTime()/20.0f);
			waterShader.updateFog(density, color);
			waterShader.updateProjectionMatrix(arr);
			waterShader.updateViewPos(pos);
			waterShader.updateLightDir(sunDir);
		}
		if(shader != null) {
			gl.glUseProgram(shader.getProgramHandle());
			shader.updateTime(world.getTime()/20.0f);
			shader.updateFog(density, color);
			shader.updateProjectionMatrix(arr);
			shader.updateLightDir(sunDir);
			shader.updateViewPos(pos);
		}
		gl.glClearColor(color[0], color[1], color[2], color[3]);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
		//gl.glMatrixMode(GL2.GL_PROJECTION);
		//gl.glLoadIdentity();
		//glu.gluPerspective(70, FreeCraftClient.get().getGLWidth()/(float)FreeCraftClient.get().getGLHeight(), 0.1f, 1000.0f);
		
		setupModelview();
	}
	public void beginSkyRendering() {
		synchronized(gl) {
			gl.glUseProgram(skyShader.getProgramHandle());
			setupSkyModelview();
			gl.glDisable(GL2.GL_CULL_FACE);
		}
	}
	public void endSkyRendering() {
		synchronized(gl) {
			gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
			gl.glUseProgram(shader.getProgramHandle());
			gl.glEnable(GL2.GL_CULL_FACE);
		}
	}
	public void beginWaterRendering() {
		synchronized(gl) {
			gl.glUseProgram(waterShader.getProgramHandle());
			gl.glEnable(GL2.GL_BLEND);
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			gl.glDisable(GL2.GL_CULL_FACE);
		}
	}
	public void endWaterRendering() {
		synchronized(gl) {
			gl.glUseProgram(shader.getProgramHandle());
			gl.glDisable(GL2.GL_BLEND);
			gl.glEnable(GL2.GL_CULL_FACE);
		}
	}
	private void setupSkyModelview() {
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		Entity cam = getViewEntity();
		Vec3D fw = new Vec3D(0, 0, -1);
		if(cam != null) {
			fw = cam.getForward();
		}
		Matrix4f mat = new Matrix4f()
				.identity()
				.lookAt(0, 0, 0, (float)fw.getX(), (float)fw.getY(), (float)fw.getZ(), 0, 1, 0);
		float[] arr = new float[16];
		mat.get(arr);
		if(skyShader != null) {
			gl.glUseProgram(skyShader.getProgramHandle());
			skyShader.updateModelviewMatrix(arr);
		}
	}
	private void setupCloudModelview() {
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		Entity cam = getViewEntity();
		Vec3D pos = FreeCraftClient.get().getWorld().getSpawnPos();
		Vec3D fw = new Vec3D(0, 0, -1);
		if(cam != null) {
			pos = cam.getPos();
			fw = cam.getForward();
		}
		Matrix4f mat = new Matrix4f()
				.identity()
				.lookAt(0, (float)pos.getY(), 0, (float)fw.getX(), (float)(pos.getY()+fw.getY()), (float)fw.getZ(), 0, 1, 0);
		float[] arr = new float[16];
		mat.get(arr);
		if(cloudShader != null) {
			gl.glUseProgram(cloudShader.getProgramHandle());
			cloudShader.updateModelviewMatrix(arr);
		}
	}
	private void setupModelview() {
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		Entity cam = getViewEntity();
		Vec3D pos = FreeCraftClient.get().getWorld().getSpawnPos();
		Vec3D fw = new Vec3D(0, 0, -1);
		if(cam != null) {
			pos = cam.getPos();
			fw = cam.getForward();
		}
		Matrix4f mat = new Matrix4f()
				.identity()
				.lookAt((float)pos.getX(), (float)pos.getY(), (float)pos.getZ(), (float)(pos.getX()+fw.getX()), (float)(pos.getY()+fw.getY()), (float)(pos.getZ()+fw.getZ()), 0, 1, 0);
		float[] arr = new float[16];
		mat.get(arr);
		if(waterShader != null) {
			gl.glUseProgram(waterShader.getProgramHandle());
			waterShader.updateModelviewMatrix(arr);
		}
		if(shader != null) {
			gl.glUseProgram(shader.getProgramHandle());
			shader.updateModelviewMatrix(arr);
		}
		//glu.gluLookAt(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+fw.getX(), pos.getY()+fw.getY(), pos.getZ()+fw.getZ(), 0, 1, 0);
	}
	public GL2 getGL2() {
		return gl;
	}
	public void swapBuffers(Graphics g) {
		synchronized(gl) {
			gl.glFlush();
			BufferedImage img = new AWTGLReadBufferUtil(drawable.getGLProfile(), true)
					.readPixelsToBufferedImage(drawable.getGL(), 0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight(), true);
			g.drawImage(img, 0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight(), null);
			prepareFrame();
		}
	}
	public void setViewEntityId(int viewEntityId) {
		this.viewEntityId = viewEntityId;
	}
	public int getViewEntityId() {
		return viewEntityId;
	}
	public Entity getViewEntity() {
		return FreeCraft.get().getWorld().getEntity(viewEntityId);
	}
	public boolean inited() {
		return inited;
	}
}
