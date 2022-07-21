package net.freecraft.util;

import java.io.Serializable;
import java.util.Objects;

import net.freecraft.world.Chunk;

public class BlockPos implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public int x, y, z;
	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public BlockPos() {
		this(0, 0, 0);
	}
	public ChunkPos toChunkPos() {
		return new ChunkPos(x / Chunk.SIZE, z / Chunk.SIZE);
	}
	public BlockPos toLocal(ChunkPos ref) {
		BlockPos base = ref.toBlockPos();
		return new BlockPos(x - base.x, y - base.y, z - base.z);
	}
	public BlockPos fromLocal(ChunkPos ref) {
		BlockPos base = ref.toBlockPos();
		return new BlockPos(x + base.x, y + base.y, z + base.z);
	}
	public Vec3D toVec() {
		return new Vec3D(x, y, z);
	}
	@Override
	public String toString() {
		return x + ", " + y + ", " + z;
	}
	public boolean inside(ChunkPos pos) {
		BlockPos bpos = pos.toBlockPos();
		if(x >= bpos.x && x < bpos.x + Chunk.SIZE) {
			if(z >= bpos.z && z < bpos.z + Chunk.SIZE) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BlockPos) {
			BlockPos pos = (BlockPos)obj;
			return pos.x == x && pos.y == y && pos.z == z;
		}
		return super.equals(obj);
	}
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
}
