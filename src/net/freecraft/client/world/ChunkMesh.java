package net.freecraft.client.world;

import com.jogamp.opengl.GL2;

import net.freecraft.FreeCraft;
import net.freecraft.block.Block;
import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.util.mesh.MeshUtil;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.Registries;
import net.freecraft.util.Vec2D;
import net.freecraft.util.Vec3D;
import net.freecraft.world.Chunk;
import net.freecraft.world.ChunkData;

public class ChunkMesh {
	public static final Vec3D[] POSITIONS = new Vec3D[] {
		new Vec3D(0.0f, 0.0f, 0.0f),
		new Vec3D(1.0f, 0.0f, 0.0f),
		new Vec3D(1.0f, 1.0f, 0.0f),
		new Vec3D(0.0f, 1.0f, 0.0f),
		new Vec3D(0.0f, 0.0f, 1.0f),
		new Vec3D(1.0f, 0.0f, 1.0f),
		new Vec3D(1.0f, 1.0f, 1.0f),
		new Vec3D(0.0f, 1.0f, 1.0f),
	};
	public static final Vec3D[] NORMALS = new Vec3D[] {
		new Vec3D(0.0f, 0.0f, -1.0f),
		new Vec3D(0.0f, 0.0f, 1.0f),
		new Vec3D(0.0f, 1.0f, 0.0f),
		new Vec3D(0.0f, -1.0f, 0.0f),
		new Vec3D(-1.0f, 0.0f, 0.0f),
		new Vec3D(1.0f, 0.0f, 0.0f)
	};

	public static final int[][] INDICES = new int[][] {
        // Back, Front, Top, Bottom, Left, Right
		// 0 1 2 2 1 3
		{0, 3, 2, 1}, // Back Face
		{5, 6, 7, 4}, // Front Face
		{3, 7, 6, 2}, // Top Face
		{1, 5, 4, 0}, // Bottom Face
		{4, 7, 3, 0}, // Left Face
		{1, 2, 6, 5} // Right Face
	};
	public static final Vec2D[] TEXCOORDS = new Vec2D[] {
		new Vec2D(0.0f, 0.0f),
		new Vec2D(0.0f, 1.0f),
		new Vec2D(1.0f, 1.0f),
		new Vec2D(1.0f, 0.0f),
	};
	private ChunkData data;
	private ChunkPos pos;
	private int[] mesh;
	private int[] mesh2; // water
	public ChunkMesh(ChunkPos pos, ChunkData data) {
		this.data = data;
		this.pos = pos;
		this.mesh = new int[2];
		this.mesh2 = new int[2];
		mesh[0] = -1;
		mesh2[0] = -1;
	}
	public ChunkPos getPos() {
		return pos;
	}
	public ChunkData getData() {
		return data;
	}
	public void dispose() {
		GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		synchronized(gl) {
			if(mesh[0] != -1) {
				MeshUtil.delete(mesh[0]);
				mesh[0] = -1;
			}
			if(mesh2[0] != -1) {
				MeshUtil.delete(mesh2[0]);
				mesh2[0] = -1;
			}
		}
	}
	
	// Any variable name ending in a '2'
	// means its a water mesh, similar
	// variables without the 2 represent
	// an opaque block mesh
	
	private Vec3D[] colors;
	private Vec3D[] normals;
	private Vec3D[] vertices;
	private Vec2D[] texcoords;

	private Vec3D[] colors2;
	private Vec3D[] normals2;
	private Vec3D[] vertices2;
	private Vec2D[] texcoords2;
	
	private float[] meshData;
	private float[] meshData2;

	public void load() {
		GL2 ogl = FreeCraftClient.get().getRenderer().getGL2();
		dispose();
		/*if(normals == null || vertices == null || colors == null || texcoords == null
		|| normals2 == null || vertices2 == null || colors2 == null || texcoords2 == null) {
			build();
		}*/
		// Water mesh
		//gl2 = new MeshCompiler();
		//gl2.glBegin(GL2.GL_QUADS);

		// Normal mesh
		//gl = new MeshCompiler();
		//gl.glBegin(GL2.GL_QUADS);
		if(meshData == null) {
			build();
		}

		synchronized(ogl) {
			mesh = MeshUtil.load(meshData);
			mesh2 = MeshUtil.load(meshData2);
			//mesh = gl.glEnd();
			//mesh2 = gl2.glEnd();
		}
		//this.gl = null;
		//this.gl2 = null;
		meshData = null;
		meshData2 = null;
	}
	public void build() {
		int idx = 0;
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int z = 0; z < Chunk.SIZE; z++) {
				for(int y = 0; y < Chunk.HEIGHT; y++) {
					int b = data.getBlock(new BlockPos(x, y, z));
					if(Registries.BLOCKS.get(b).isVisible() && Registries.BLOCKS.get(b).isFluid()) {
						for(int f = 0; f < 6; f++) {
							Vec3D n = NORMALS[f];
							BlockPos npos = new BlockPos(x, y, z);
							npos.x += (int)n.getX();
							npos.y += (int)n.getY();
							npos.z += (int)n.getZ();
							int b2 = 0;
							if(data.inLocalBounds(npos)) {
								b2 = data.getBlock(npos);
							}else {
								b2 = FreeCraftClient.get().getWorld().getBlock(npos.fromLocal(pos));
							}
							if(!Registries.BLOCKS.get(b2).isVisible() || (Registries.BLOCKS.get(b2).isTransparent() && !Registries.BLOCKS.get(b2).isFluid())) {
								idx += 4;
							}
						}
					}
				}
			}
		}
		normals2 = new Vec3D[idx];
		vertices2 = new Vec3D[idx];
		colors2 = new Vec3D[idx];
		texcoords2 = new Vec2D[idx];
		idx = 0;
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int z = 0; z < Chunk.SIZE; z++) {
				for(int y = 0; y < Chunk.HEIGHT; y++) {
					int b = data.getBlock(new BlockPos(x, y, z));
					if(Registries.BLOCKS.get(b).isVisible() && Registries.BLOCKS.get(b).isFluid()) {
						int bAbove = data.getBlock(new BlockPos(x, y+1, z));
						boolean airAbove = !Registries.BLOCKS.get(bAbove).isVisible();
						for(int f = 0; f < 6; f++) {
							Vec3D n = NORMALS[f];
							BlockPos npos = new BlockPos(x, y, z);
							npos.x += (int)n.getX();
							npos.y += (int)n.getY();
							npos.z += (int)n.getZ();
							int b2 = 0;
							if(data.inLocalBounds(npos)) {
								b2 = data.getBlock(npos);
							}else {
								b2 = FreeCraftClient.get().getWorld().getBlock(npos.fromLocal(pos));
							}
							if(!Registries.BLOCKS.get(b2).isVisible() || (Registries.BLOCKS.get(b2).isTransparent() && !Registries.BLOCKS.get(b2).isFluid())) {
								float nx = (float)n.getX();
								float ny = (float)n.getY();
								float nz = (float)n.getZ();
								for(int p = 0; p < 4; p++) {
									normals2[idx] = new Vec3D(nx, ny, nz);
									Vec2D tc = TEXCOORDS[p];
									float tx = (float)tc.getX();
									float ty = (float)tc.getY();
									int tidx = Registries.BLOCKS.get(b).getTexture(f);
									tx = tidx % 16 / 16.0f + tx / 16.0f;
									ty = tidx / 16 / 16.0f + 1.0f / 16.0f - ty / 16.0f;
									texcoords2[idx] = new Vec2D(tx, ty);
									BlockPos pos = POSITIONS[INDICES[f][p]].toBlockPos();
									pos.x += x + this.pos.x * Chunk.SIZE;
									pos.y += y;
									pos.z += z + this.pos.z * Chunk.SIZE;
									BlockPos vpos = POSITIONS[INDICES[f][p]].toBlockPos();
									vpos.x += x;
									vpos.y += y;
									vpos.z += z;
									float br = 8;
									
									int skyLight = 0, blockLight = 0;
									byte ll = 0;
									if(data.inLocalBounds(npos)) {
										ll = data.getLightFast(npos);
									}else {
										ll = FreeCraft.get().getWorld().getLight(npos.fromLocal(getPos()));
									}
									skyLight += (ll&0xF);
									blockLight += (ll&0xF0)>>4;
									/*for(int i = -1; i <= 0; i++) {
										for(int j = -1; j <= 0; j++) {
											for(int k = -1; k <= 0; k++) {
												Block b3 = Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(new BlockPos(getPos().x * Chunk.SIZE + vpos.x + i, vpos.y + j, getPos().z * Chunk.SIZE + vpos.z + k)));
												float val = 0;
												if(!b3.isVisible()||b3.isFluid()) val = 1;
												//else if(b3.isFluid()) val = 0.5f;
												br += val;

												BlockPos nvpos = new BlockPos(vpos.x + i + (int)nx, vpos.y + j + (int)ny, vpos.z + k + (int)nz);
												byte ll = 0;
												if(data.inLocalBounds(nvpos)) {
													ll = data.getLightFast(nvpos);
												}else {
													ll = FreeCraft.get().getWorld().getLight(nvpos.fromLocal(getPos()));
												}
												skyLight += (ll&0xF);
												blockLight += (ll&0xF0)>>4;
											}
										}
									}*/
									br /= 8.0f;
									br = Math.min(br*2, 1);

									colors2[idx] = new Vec3D(skyLight/15.0f/1.0f, blockLight/15.0f/1.0f, br);
									if(airAbove) {
										vertices2[idx] = new Vec3D(pos.x, (pos.y - y) / 2.0 + y, pos.z);
									}else {
										vertices2[idx] = new Vec3D(pos.x, pos.y, pos.z);
									}
									idx++;
								}
							}
						}
					}
				}
			}
		}
		idx = 0;
		// REGULAR BLOCKS NOW!!!!
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int z = 0; z < Chunk.SIZE; z++) {
				for(int y = 0; y < Chunk.HEIGHT; y++) {
					int b = data.getBlock(new BlockPos(x, y, z));
					if(Registries.BLOCKS.get(b).isVisible() && !Registries.BLOCKS.get(b).isFluid()) {
						for(int f = 0; f < 6; f++) {
							Vec3D n = NORMALS[f];
							BlockPos npos = new BlockPos(x, y, z);
							npos.x += (int)n.getX();
							npos.y += (int)n.getY();
							npos.z += (int)n.getZ();
							int b2 = 0;
							if(data.inLocalBounds(npos)) {
								b2 = data.getBlock(npos);
							}else {
								b2 = FreeCraftClient.get().getWorld().getBlock(npos.fromLocal(pos));
							}
							if(!Registries.BLOCKS.get(b2).isVisible() || Registries.BLOCKS.get(b2).isTransparent()) {
								idx += 4;
							}
						}
					}
				}
			}
		}
		normals = new Vec3D[idx];
		vertices = new Vec3D[idx];
		colors = new Vec3D[idx];
		texcoords = new Vec2D[idx];
		idx = 0;
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int z = 0; z < Chunk.SIZE; z++) {
				for(int y = 0; y < Chunk.HEIGHT; y++) {
					int b = data.getBlock(new BlockPos(x, y, z));
					if(Registries.BLOCKS.get(b).isVisible() && !Registries.BLOCKS.get(b).isFluid()) {
						for(int f = 0; f < 6; f++) {
							Vec3D n = NORMALS[f];
							BlockPos npos = new BlockPos(x, y, z);
							npos.x += (int)n.getX();
							npos.y += (int)n.getY();
							npos.z += (int)n.getZ();
							int b2 = 0;
							if(data.inLocalBounds(npos)) {
								b2 = data.getBlock(npos);
							}else {
								b2 = FreeCraftClient.get().getWorld().getBlock(npos.fromLocal(pos));
							}
							float nx = (float)n.getX();
							float ny = (float)n.getY();
							float nz = (float)n.getZ();
							if(!Registries.BLOCKS.get(b2).isVisible() || Registries.BLOCKS.get(b2).isTransparent()) {
								for(int p = 0; p < 4; p++) {
									normals[idx] = new Vec3D(nx, ny, nz);
									Vec2D tc = TEXCOORDS[p];
									float tx = (float)tc.getX();
									float ty = (float)tc.getY();
									int tidx = Registries.BLOCKS.get(b).getTexture(f);
									tx = tidx % 16 / 16.0f + tx / 16.0f;
									ty = tidx / 16 / 16.0f + 1.0f / 16.0f - ty / 16.0f;
									texcoords[idx] = new Vec2D(tx, ty);
									BlockPos pos = POSITIONS[INDICES[f][p]].toBlockPos();
									pos.x += x + this.pos.x * Chunk.SIZE;
									pos.y += y;
									pos.z += z + this.pos.z * Chunk.SIZE;
									BlockPos vpos = POSITIONS[INDICES[f][p]].toBlockPos();
									vpos.x += x;
									vpos.y += y;
									vpos.z += z;
									float br = 8;
									int skyLight = 0, blockLight = 0;
									byte ll = 0;
									if(data.inLocalBounds(npos)) {
										ll = data.getLightFast(npos);
									}else {
										ll = FreeCraft.get().getWorld().getLight(npos.fromLocal(getPos()));
									}
									skyLight += (ll&0xF);
									blockLight += (ll&0xF0)>>4;
									
									/*for(int i = -1; i <= 0; i++) {
										for(int j = -1; j <= 0; j++) {
											for(int k = -1; k <= 0; k++) {
												Block b3 = Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(new BlockPos(getPos().x * Chunk.SIZE + vpos.x + i, vpos.y + j, getPos().z * Chunk.SIZE + vpos.z + k)));
												float val = 0;
												if(!b3.isVisible()||b3.isFluid()) val = 1;
												//else if(b3.isFluid()) val = 0.5f;
												br += val;
												BlockPos nvpos = new BlockPos(vpos.x + i + (int)nx, vpos.y + j + (int)ny, vpos.z + k + (int)nz);
												byte ll = 0;
												if(data.inLocalBounds(nvpos)) {
													ll = data.getLightFast(nvpos);
												}else {
													ll = FreeCraft.get().getWorld().getLight(nvpos.fromLocal(getPos()));
												}
												skyLight += (ll&0xF);
												blockLight += (ll&0xF0)>>4;
											}
										}
									}*/
									br /= 8.0f;
									br = Math.min(br*2, 1);

									colors[idx] = new Vec3D(skyLight/15.0f/1.0f, blockLight/15.0f/1.0f, br);
									vertices[idx] = new Vec3D(pos.x, pos.y, pos.z);
									idx++;
								}
							}
						}
					}
				}
			}
		}
		
		
		meshData = new float[vertices.length*11];
		for(int i = 0; i < vertices.length; i++) {
			meshData[i*11+0] = (float) vertices[i].getX();
			meshData[i*11+1] = (float) vertices[i].getY();
			meshData[i*11+2] = (float) vertices[i].getZ();
			meshData[i*11+3] = (float) normals[i].getX();
			meshData[i*11+4] = (float) normals[i].getY();
			meshData[i*11+5] = (float) normals[i].getZ();
			meshData[i*11+6] = (float) colors[i].getX();
			meshData[i*11+7] = (float) colors[i].getY();
			meshData[i*11+8] = (float) colors[i].getZ();
			meshData[i*11+9] = (float) texcoords[i].getX();
			meshData[i*11+10] = (float) texcoords[i].getY();
		}

		meshData2 = new float[vertices2.length*11];
		for(int i = 0; i < vertices2.length; i++) {
			meshData2[i*11+0] = (float) vertices2[i].getX();
			meshData2[i*11+1] = (float) vertices2[i].getY();
			meshData2[i*11+2] = (float) vertices2[i].getZ();
			meshData2[i*11+3] = (float) normals2[i].getX();
			meshData2[i*11+4] = (float) normals2[i].getY();
			meshData2[i*11+5] = (float) normals2[i].getZ();
			meshData2[i*11+6] = (float) colors2[i].getX();
			meshData2[i*11+7] = (float) colors2[i].getY();
			meshData2[i*11+8] = (float) colors2[i].getZ();
			meshData2[i*11+9] = (float) texcoords2[i].getX();
			meshData2[i*11+10] = (float) texcoords2[i].getY();
		}
		

		this.colors = null;
		this.normals = null;
		this.vertices = null;
		this.texcoords = null;
		this.colors2 = null;
		this.normals2 = null;
		this.vertices2 = null;
		this.texcoords2 = null;
	}
	public boolean justBuilt() {
		return this.meshData != null;
	}
	public void renderTranslucent() {
		GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		synchronized(gl) {
			MeshUtil.render(mesh2);
		}
	}
	public void render() {
		GL2 gl = FreeCraftClient.get().getRenderer().getGL2();
		synchronized(gl) {
			MeshUtil.render(mesh);
		}
	}
}
