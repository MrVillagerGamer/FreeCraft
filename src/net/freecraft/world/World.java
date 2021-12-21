package net.freecraft.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.flowpowered.noise.module.source.Perlin;

import net.freecraft.FreeCraft;
import net.freecraft.data.Data;
import net.freecraft.entity.Entity;
import net.freecraft.net.packet.SetTileDataPacket;
import net.freecraft.net.packet.SetTimePacket;
import net.freecraft.net.packet.SpawnEntityPacket;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.Registries;
import net.freecraft.util.Side;
import net.freecraft.util.Vec3D;

public class World {
	public static final int SIZE = 1024;
	public static final int TICK_RATE = 20;
	public static final int DAY_LENGTH = 1200;
	public static final int START_TIME = DAY_LENGTH/8*20;
	public static final double GRAVITY = -20.0;
	protected Chunk[][] chunks = new Chunk[SIZE][SIZE];
	protected List<Entity> entities = new ArrayList<>();
	protected HashMap<Integer, Entity> entityIds = new HashMap<>();
	protected HashMap<BlockPos, Data> tileData = new HashMap<>();
	private long startTime;
	private long currentTick;
	private long time;
	private Vec3D sunDir;
	private double sunRot;
	public World() {
		startTime = System.nanoTime();
		currentTick = 0;
		sunDir = new Vec3D();
		sunRot = 0;
		time = START_TIME;
	}
	public Side getSide() {
		return Side.UNKNOWN;
	}
	public Chunk getChunk(ChunkPos pos) {
		return chunks[pos.x][pos.z];
	}
	public boolean setChunk(ChunkPos pos, Chunk chunk) {
		if(inBounds(pos)) {
			chunks[pos.x][pos.z] = chunk;
			return true;
		}
		return false;
	}
	public boolean inBounds(ChunkPos pos) {
		return pos.x >= 0 && pos.z >= 0 && pos.x < SIZE && pos.z < SIZE;
	}
	public boolean inBounds(BlockPos pos) {
		return pos.x >= 0 && pos.y >= 0 && pos.z >= 0 && pos.x < SIZE * Chunk.SIZE && pos.z < SIZE * Chunk.SIZE && pos.y < Chunk.HEIGHT;
	}
	public int getViewDistanceInBlocks() {
		return getViewDistance() * Chunk.SIZE;
		
	}
	public int getViewDistance() {
		return 10;
	}
	public int findNextEntityId() {
		int id = 0;
		while(entityIds.containsKey(id)) {
			id++;
		}
		return id;
	}
	public Entity setEntityLocally(int id, Entity entity) {
		Entity old = entityIds.get(id);
		if(old != null) entities.remove(old);
		entities.add(entity);
		entityIds.put(id, entity);
		return old;
	}
	public Entity delEntityLocally(int id) {
		Entity old = entityIds.get(id);
		if(old != null) entities.remove(old);
		entityIds.remove(id);
		return old;
	}
	public int spawnEntityLocally(Entity entity) {
		int id = findNextEntityId();
		entity.setId(id);
		entities.add(entity);
		entityIds.put(id, entity);
		return id;
	}
	public int spawnEntity(Entity entity) {
		int id = findNextEntityId();
		entity.setId(id);
		entities.add(entity);
		entityIds.put(id, entity);
		FreeCraft.get().getNetHandler().send(null, new SpawnEntityPacket(entity.getTypeId(), id, entity.getPos(), entity.getRot(), entity.getData()));
		return id;
	}
	public Vec3D getSpawnPos() {
		return new Vec3D(SIZE*Chunk.SIZE/2, 128, SIZE*Chunk.SIZE/2);
	}
	public Entity getEntity(int id) {
		if(id < 0) return null;
		return entityIds.get(id);
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public HashMap<BlockPos, Data> getTileData() {
		return tileData;
	}
	public void setTileDataLocally(BlockPos pos, Data data) {
		if(data == null) {
			tileData.remove(pos);
		}else {
			tileData.put(pos, data);
		}
	}
	public void setTileData(BlockPos pos, Data data) {
		setTileDataLocally(pos, data);
		FreeCraft.get().getNetHandler().send(null, new SetTileDataPacket(pos, data));
	}
	public Data getTileData(BlockPos pos) {
		return tileData.get(pos);
	}
	public HashMap<Integer, Entity> getEntityIds() {
		return entityIds;
	}
	public long getTime() {
		return time;
	}
	public void update() {
		long elapsedTime = System.nanoTime() - startTime;
		long newTick = (int)(elapsedTime/1000000000.0D*TICK_RATE);
		long ticks = newTick - currentTick;
		currentTick = newTick;
		double partialTick = elapsedTime/1000000000.0D*TICK_RATE - newTick;
		for(int i = 0; i < ticks; i++) {
			tick();
		}
		partialTick(partialTick);
	}
	public void partialTick(double partialTick) {
		for(Entity entity : entities) {
			entity.partialTick(partialTick);
		}
	}
	public double getSunRot() {
		return sunRot;
	}
	public Vec3D getSunDir() {
		return sunDir;
	}
	public void tick() {
		for(Entity entity : entities) {
			entity.tick();
		}
		sunRot = time * 6.28D / World.DAY_LENGTH / World.TICK_RATE;
		sunDir = new Vec3D(Math.cos(sunRot), Math.sin(sunRot), 0.33f);
		//System.out.println(getSide().toString() + ": " + getTime());
		time++;
	}
	public int setBlock(BlockPos bpos, int id) {
		int oldId = setBlockLocally(bpos, id);
		return oldId;
	}
	public int setBlockLocally(BlockPos bpos, int id) {
		int oldId = getChunk(bpos.toChunkPos()).getBlock(bpos.toLocal(bpos.toChunkPos()));
		getChunk(bpos.toChunkPos()).setBlock(bpos.toLocal(bpos.toChunkPos()), id);
		return oldId;
	}
	public int genBiome(BlockPos pos) {
		int count = Registries.BIOMES.getAll().size();
		Perlin perlin = new Perlin();
		perlin.setSeed(1337);
		perlin.setFrequency(0.02D/count);
		perlin.setOctaveCount(1);
		Perlin perlin2 = new Perlin();
		perlin2.setSeed(1337);
		perlin2.setFrequency(0.5D);
		perlin2.setOctaveCount(1);
		double randVal = 0;//perlin2.getValue(pos.x/1.0D, 0, pos.z/1.0D);
		return Math.max(Math.min((int)((perlin.getValue(pos.x, 0, pos.z)*0.5+0.5+(randVal*2-1)/(100D/count))*1.0f*Registries.BIOMES.getAll().size()), Registries.BIOMES.getAll().size() - 1), 0);
	}
	public void setLight(BlockPos bpos, byte light) {
		Chunk chunk = getChunk(bpos.toChunkPos());
		if(chunk == null) return;
		chunk.getData().setLight(bpos.toLocal(bpos.toChunkPos()), light);
	}
	public byte getLight(BlockPos bpos) {
		Chunk chunk = getChunk(bpos.toChunkPos());
		if(chunk == null) return 0;
		return chunk.getData().getLight(bpos.toLocal(bpos.toChunkPos()));
	}
	public int getBlock(BlockPos bpos) {
		Chunk chunk = getChunk(bpos.toChunkPos());
		if(chunk != null) {
			return chunk.getBlock(bpos.toLocal(bpos.toChunkPos()));
		}
		return 0;
	}
	public void setTimeLocally(long time) {
		this.time = time;
	}
	public void setTime(long time) {
		this.time = time;
		FreeCraft.get().getNetHandler().send(null, new SetTimePacket(time));
	}
	public void dispose() {
		chunks = null;
		entities.clear();
		entities = null;
		entityIds.clear();
		entityIds = null;
	}
}
