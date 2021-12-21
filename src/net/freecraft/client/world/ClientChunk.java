package net.freecraft.client.world;

import net.freecraft.util.ChunkPos;
import net.freecraft.world.Chunk;
import net.freecraft.world.ChunkData;

public class ClientChunk extends Chunk {
	private ChunkMesh mesh;
	public ClientChunk(ChunkPos pos, ChunkData data) {
		super(data);
		this.mesh = new ChunkMesh(pos, data);
	}
	public ChunkMesh getMesh() {
		return mesh;
	}
}
