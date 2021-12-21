package net.freecraft.util;

import java.io.Serializable;

import net.freecraft.world.Chunk;

public class ChunkPos implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x, z;
	public ChunkPos(int x, int z) {
		this.x = x;
		this.z = z;
	}
	public ChunkPos() {
		this(0, 0);
	}
	public BlockPos toBlockPos() {
		return new BlockPos(x * Chunk.SIZE, 0, z * Chunk.SIZE);
	}
	@Override
	public String toString() {
		return x + ", " + z;
	}
}
