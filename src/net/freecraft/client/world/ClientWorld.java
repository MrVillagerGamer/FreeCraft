package net.freecraft.client.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.util.RenderThreadRunner;
import net.freecraft.client.world.cloud.CloudMesh;
import net.freecraft.client.world.cloud.SkyMesh;
import net.freecraft.entity.Entity;
import net.freecraft.net.packet.SetBlockPacket;
import net.freecraft.net.packet.SpawnEntityPacket;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.Side;
import net.freecraft.world.Chunk;
import net.freecraft.world.World;

public class ClientWorld extends World implements Runnable{
	protected CloudMesh cloudMesh;
	protected SkyMesh skyMesh;
	protected List<Chunk> renderChunks = new CopyOnWriteArrayList<Chunk>();
	protected List<Chunk> activeChunks = new CopyOnWriteArrayList<Chunk>();
	protected List<Chunk> rebuildChunks = new CopyOnWriteArrayList<Chunk>();
	private Thread meshingThread;
	public ClientWorld() {
		meshingThread = new Thread(this);
		meshingThread.start();
	}
	
	boolean worldRunning = true;
	@Override
	public void run() {
		while(worldRunning) {
			int idx = 0;
			for(Chunk chunk : activeChunks) {
				
				if(!renderChunks.contains(chunk)) {
					ClientChunk clientChunk = (ClientChunk)chunk;
					boolean doMeshing = true;
					// Only do meshing if neighboring chunks exist
					for(int i = -1; i <= 1; i++) {
						for(int j = -1; j <= 1; j++) {
							ChunkPos pos = clientChunk.getMesh().getPos();
							pos = new ChunkPos(pos.x + i, pos.z + j);
							if(getChunk(pos) == null) {
								doMeshing = false;
							}
						}
					}
					if(doMeshing && idx < 10) {
						((ClientChunk)chunk).getData().buildLighting(((ClientChunk)chunk).getMesh().getPos());
						((ClientChunk)chunk).getMesh().build();
						renderChunks.add(chunk);
						idx++;
					}
				}
			}
		}
	}
	@Override
	public boolean setChunk(ChunkPos pos, Chunk chunk) {
		if(getChunk(pos) != null) {
			Chunk c = getChunk(pos);
				new RenderThreadRunner(((ClientChunk)c).getMesh(), "dispose", new Object[0]);
				renderChunks.remove(c);
				activeChunks.remove(c);
		}
		boolean b = super.setChunk(pos, chunk);
		if(chunk instanceof ClientChunk) {
				activeChunks.add(chunk);
		}
		return b;
	}
	public void updateBlock(BlockPos bpos, int id) {
		List<Chunk> toBuild = new ArrayList<>();
		for(int i = -1; i <= 1; i++) {
			for(int k = -1; k <= 1; k++) {
				Chunk chunk = getChunk(new BlockPos(bpos.x + i, bpos.y, bpos.z + k).toChunkPos());
				if(renderChunks.contains(chunk) && !toBuild.contains(chunk)) {
					toBuild.add(chunk);
				}
			}
		}
		for(Chunk chunk : toBuild) {
			if(chunk instanceof ClientChunk) {
				rebuildChunks.add(chunk);	
				
					/*((ClientChunk)chunk).getMesh().build();
				new RenderThreadRunner(((ClientChunk)chunk).getMesh(), "load", new Object[0]);
					renderChunks.add(chunk);*/
			}
		}
	}
	@Override
	public int setBlockLocally(BlockPos bpos, int id) {
		int ret = super.setBlockLocally(bpos, id);
		updateBlock(bpos, id);
		return ret;
	}
	@Override
	public int setBlock(BlockPos bpos, int id) {
		int oldId = super.setBlock(bpos, id);
		updateBlock(bpos, id);
		FreeCraftClient.get().getNetHandler().send(null, new SetBlockPacket(bpos, id));
		return oldId;
	}
	public void render() {
		if(skyMesh == null) {
			skyMesh = new SkyMesh();
			skyMesh.build();
		}
		if(cloudMesh == null) {
			cloudMesh = new CloudMesh();
			cloudMesh.build();
		}
		skyMesh.render();
		cloudMesh.render();
		
		for(Chunk chunk : renderChunks) {
			if(rebuildChunks.contains(chunk)) {
				((ClientChunk)chunk).getData().buildLighting(((ClientChunk)chunk).getMesh().getPos());
				((ClientChunk)chunk).getMesh().build();
				rebuildChunks.remove(chunk);
			}
			if(((ClientChunk)chunk).getMesh().justBuilt()) {
				((ClientChunk)chunk).getMesh().load();
			}
			((ClientChunk)chunk).getMesh().render();
		}
		FreeCraftClient.get().getRenderer().beginWaterRendering();
		for(Chunk chunk : renderChunks) {
			((ClientChunk)chunk).getMesh().renderTranslucent();
		}
		FreeCraftClient.get().getRenderer().endWaterRendering();
	}
	@Override
	public void dispose() {
		worldRunning = false;
		try {
			meshingThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.dispose();
			for(Chunk chunk : renderChunks) {
				((ClientChunk)chunk).getMesh().dispose();
			}
			renderChunks.clear();
			activeChunks.clear();
		if(cloudMesh != null) {
			cloudMesh.dispose();
		}
		if(skyMesh != null) {
			skyMesh.dispose();
		}
		activeChunks = null;
		renderChunks = null;
		cloudMesh = null;
		skyMesh = null;
	}
	@Override
	public Side getSide() {
		return Side.CLIENT;
	}
	@Override
	public int spawnEntity(Entity entity) {
		int id = super.spawnEntity(entity);
		SpawnEntityPacket packet = new SpawnEntityPacket(entity.getTypeId(), id, entity.getPos(), entity.getRot(), entity.getData());
		FreeCraftClient.get().getNetHandler().send(null, packet);
		return id;
	}
	@Override
	public void tick() {
		super.tick();
	}
}
