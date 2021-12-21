package net.freecraft.world;

import net.freecraft.util.BlockPos;

public class Chunk {
	public static final int SIZE = 16;
	public static final int HEIGHT = 128;
	protected ChunkData data;
	public Chunk(ChunkData data) {
		this.data = data;
	}
	public ChunkData getData() {
		return data;
	}
	public void setBlock(BlockPos pos, int id) {
		data.setBlock(pos, id);
	}
	public int getBlock(BlockPos pos) {
		return data.getBlock(pos);
	}
	public int getBlockFast(BlockPos pos) {
		return data.getBlockFast(pos);
	}
}
