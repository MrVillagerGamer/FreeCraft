package net.freecraft.world;

import java.io.Serializable;

import net.freecraft.FreeCraft;
import net.freecraft.block.Block;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.MeshConstants;
import net.freecraft.util.Registries;
import net.freecraft.util.Vec3D;

public class ChunkData implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private char[][][] blocks;
	private byte[][][] lights;
	public ChunkData(char[][][] blocks) {
		this.blocks = blocks;
		this.lights = new byte[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
	}
	public ChunkData() {
		this.blocks = new char[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
		this.lights = new byte[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
	}
	public char[][][] getBlocks() {
		return blocks;
	}
	public int getBlock(BlockPos pos) {
		if(pos.y < 0 || pos.y >= Chunk.HEIGHT) return 0;
		return blocks[pos.x][pos.y][pos.z];
	}
	public int getBlockFast(BlockPos pos) {
		return blocks[pos.x][pos.y][pos.z];
	}
	public void setBlock(BlockPos pos, int id) {
		if(pos.y < 0 || pos.y >= Chunk.HEIGHT) return;
		blocks[pos.x][pos.y][pos.z] = (char)id;
	}
	public void setBlockFast(BlockPos pos, int id) {
		blocks[pos.x][pos.y][pos.z] = (char)id;
	}
	public byte getLight(BlockPos pos) {
		if(pos.y < 0 || pos.y >= Chunk.HEIGHT) return 0;
		return lights[pos.x][pos.y][pos.z];
	}
	public void setLight(BlockPos pos, byte light) {
		if(pos.y < 0 || pos.y >= Chunk.HEIGHT) return;
		lights[pos.x][pos.y][pos.z] = light;
	}
	public byte getLightFast(BlockPos pos) {
		return lights[pos.x][pos.y][pos.z];
	}
	public void setLightFast(BlockPos pos, byte light) {
		lights[pos.x][pos.y][pos.z] = light;
	}
	public void clearLighting() {
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int z = 0; z < Chunk.SIZE; z++) {
				for(int y = Chunk.HEIGHT-2; y >= 0; y--) {
					if(Registries.BLOCKS.get(blocks[x][y][z]).isVisible()) {
						lights[x][y+1][z] = 0xF;
						break;
					}
				}
			}
		}
	}
	public void buildLighting(ChunkPos cpos) {
		World world = FreeCraft.get().getWorld();
		for(int step = 0; step < 14; step++) {
			for(int x = 0; x < Chunk.SIZE; x++) {
				for(int z = 0; z < Chunk.SIZE; z++) {
					for(int y = 0; y < Chunk.HEIGHT; y++) {
						if(lights[x][y][z] == 15-step) {
							for(int f = 0; f < 6; f++) {
								Vec3D v = MeshConstants.NORMALS[f];
								int nx = (int)v.getX();
								int ny = (int)v.getY();
								int nz = (int)v.getZ();
								BlockPos nworldpos = new BlockPos(x+nx, y+ny, z+nz).fromLocal(cpos);
								Block b = Registries.BLOCKS.get(world.getBlock(nworldpos));
								if(!b.isVisible() || b.isTransparent()) {
									int oldlight = lights[x][y][z];
									int blockLight = oldlight & 0xF0;
									int skyLight = oldlight & 0x0F;

									int noldlight = world.getLight(nworldpos);
									int nblockLight = noldlight & 0xF0;
									int nskyLight = noldlight & 0x0F;

									if((skyLight > 0 || blockLight > 0)
									&& nskyLight < (15-step) && nblockLight < ((15-step-1)<<4)) {
										if(blockLight > 0) blockLight = blockLight - 0x10;
										if(skyLight > 0) skyLight = skyLight - 0x01;
										world.setLight(nworldpos, (byte)(blockLight|skyLight));
										//System.out.println(15-step-1);
									}
								}
							}
						}

					}
				}
			}
		}
	}
	public void addLightSource(BlockPos pos, byte level) {
		if(pos.y < 0 || pos.y >= Chunk.HEIGHT) return;
		//lights[pos.x][pos.y][pos.z] = (byte)(level << 4);
	}
	public boolean inLocalBounds(BlockPos pos) {
		return pos.x >= 0 && pos.x < Chunk.SIZE && pos.y >= 0 && pos.y < Chunk.HEIGHT && pos.z >= 0 && pos.z < Chunk.SIZE;
	}
	@Override
	public Object clone() {
		ChunkData data = new ChunkData();
		data.blocks = new char[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
		data.lights = new byte[Chunk.SIZE][Chunk.HEIGHT][Chunk.SIZE];
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int y = 0; y < Chunk.HEIGHT; y++) {
				data.blocks[x][y] = blocks[x][y].clone();
				data.lights[x][y] = lights[x][y].clone();
			}
		}
		return data;
	}
}
