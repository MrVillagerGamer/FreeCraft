package net.freecraft.server.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import net.freecraft.data.Data;
import net.freecraft.entity.Entity;
import net.freecraft.entity.EntityTypes;
import net.freecraft.net.NetConnection;
import net.freecraft.net.packet.SetCameraEntityPacket;
import net.freecraft.net.packet.SetChunkPacket;
import net.freecraft.net.packet.SetTileDataPacket;
import net.freecraft.net.packet.SpawnEntityPacket;
import net.freecraft.server.FreeCraftServer;
import net.freecraft.server.util.ServerRegistries;
import net.freecraft.server.util.WorldSaver;
import net.freecraft.server.world.feature.Feature;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.Logger;
import net.freecraft.util.Side;
import net.freecraft.util.Vec3D;
import net.freecraft.world.Chunk;
import net.freecraft.world.ChunkData;
import net.freecraft.world.World;

public class ServerWorld extends World {
	private class FeatureData {
		private BlockPos pos;
		private int featureId;
		public FeatureData(int featureId, BlockPos pos) {
			this.pos = pos;
			this.featureId = featureId;
		}
		public BlockPos getPos() {
			return pos;
		}
		public int getFeatureId() {
			return featureId;
		}
	}
	private ArrayList<ChunkPos> chunksList  =new ArrayList<>();
	private WorldSaver saver;
	private FeatureData[][][] features = new FeatureData[SIZE][SIZE][0];
	private boolean[][] featuredChunks = new boolean[SIZE][SIZE];
	public ServerWorld() {
		super();
		Logger.debug("Generating world...");
		saver = new WorldSaver("world1");
	}
	@Override
	public Side getSide() {
		return Side.SERVER;
	}
	@Override
	public boolean setChunk(ChunkPos pos, Chunk chunk) {
		chunksList.add(pos);
		return super.setChunk(pos, chunk);
	}
	@Override
	public int spawnEntityLocally(Entity entity) {
		if(entity.getTypeId() == EntityTypes.PLAYER.getId()) {
			Data data = saver.loadPlayer(entity.getData().getName(), entity.getData().getPasswordHash());
			if(data != null) {
				entity.setDataLocally(data);
			}
		}
		return super.spawnEntityLocally(entity);
	}
	@Override
	public int spawnEntity(Entity entity) {
		int id = super.spawnEntity(entity);
		SpawnEntityPacket packet = new SpawnEntityPacket(entity.getTypeId(), id, entity.getPos(), entity.getRot(), entity.getData());
		FreeCraftServer.get().getNetHandler().send(null, packet);
		return id;
	}
	private void addFeature(FeatureData data, ChunkPos pos) {
		int length = features[pos.x][pos.z].length;
		features[pos.x][pos.z] = Arrays.copyOf(features[pos.x][pos.z], length+1);
		features[pos.x][pos.z][length] = data;
	}
	public void addFeature(int featureId, BlockPos bpos) {
		ArrayList<Integer> chunkX = new ArrayList<>();
		ArrayList<Integer> chunkZ = new ArrayList<>();
		Feature feature = ServerRegistries.FEATURES.get(featureId);
		for(int i = feature.getStart().x; i <= feature.getStart().x + feature.getSize().x; i += feature.getSize().x) {
			for(int k = feature.getStart().z; k <= feature.getStart().z + feature.getSize().z; k += feature.getSize().z) {
				ChunkPos pos = new BlockPos(bpos.x+i, bpos.y, bpos.z+k).toChunkPos();
				if(!chunkX.contains(pos.x) || !chunkZ.contains(pos.z)) {
					addFeature(new FeatureData(featureId, bpos), pos);
				}
			}
		}
	}
	public void addFeaturesIntoChunk(ChunkPos pos, ChunkData data) {
		for(FeatureData fdata : features[pos.x][pos.z]) {
			Feature feature = ServerRegistries.FEATURES.get(fdata.getFeatureId());
			BlockPos start = feature.getStart();
			BlockPos size = feature.getSize();
			for(int x = start.x; x < start.x+size.x; x++) {
				for(int z = start.z; z < start.z+size.z; z++) {
					for(int y = start.y; y < start.y+size.y; y++) {
						BlockPos bpos = new BlockPos(x+fdata.getPos().x, y+fdata.getPos().y, z+fdata.getPos().z);
						if(bpos.inside(pos)) {
							int id = feature.getBlockFast(new BlockPos(x, y, z));
							if(id != -1) {
								data.setBlockFast(bpos.toLocal(pos), id);
							}
						}
					}
				}
			}
		}
		features[pos.x][pos.z] = new FeatureData[0];
	}
	public boolean chunkNeighborsExist(ChunkPos pos) {
		for(int i = -1; i <= 1; i++) {
			for(int k = -1; k <= 1; k++) {
				if(pos.x+i >= 0 && pos.x+i < SIZE) {
					if(pos.z+k >= 0 && pos.z+k < SIZE) {
						if(chunks[pos.x+i][pos.z+k] == null) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	@Override
	public int setBlockLocally(BlockPos bpos, int id) {
		return super.setBlockLocally(bpos, id);
	}
	@Override
	public void setTileData(BlockPos pos, Data data) {
		setTileDataLocally(pos, data);
		FreeCraftServer.get().getNetHandler().send(null, new SetTileDataPacket(pos, data));
	}
	public Chunk getOrGenChunk(ChunkPos pos) {
		if(chunks[pos.x][pos.z] == null) {
			ChunkData data = saver.load(pos);
			if(data == null) {
				data = new ChunkData();
				WorldGenerator generator = FreeCraftServer.get().getWorldGenerator();
				generator.generateChunk(pos, data);
				chunks[pos.x][pos.z] = new Chunk(data);
				generator.addFeatures(pos);

				for(int i = -1; i <= 1; i++) {
					for(int k = -1; k <= 1; k++) {
						if(pos.x+i >= 0 && pos.x+i < SIZE) {
							if(pos.z+k >= 0 && pos.z+k < SIZE) {
								if(chunks[pos.x+i][pos.z+k] != null && chunkNeighborsExist(new ChunkPos(pos.x+i, pos.z+k))) {
									chunks[pos.x+i][pos.z+k].getData().buildLighting(new ChunkPos(pos.x+i, pos.z+k));
									if(!featuredChunks[pos.x+i][pos.z+k]) {
										data = chunks[pos.x+i][pos.z+k].getData();
										addFeaturesIntoChunk(new ChunkPos(pos.x+i, pos.z+k), data);
										Chunk chunk = chunks[pos.x+i][pos.z+k];
										featuredChunks[pos.x+i][pos.z+k] = true;
										//SetChunkPacket packet = new SetChunkPacket(new ChunkPos(pos.x+i, pos.z+k), null);
										//packet.compressData(chunk.getData());
										//FreeCraftServer.get().getNetHandler().send(null, packet);
									}
								}
							}
						}
					}
				}
				data.clearLighting();
				data.buildLighting(pos);
				for(int i = -1; i <= 1; i++) {
					for(int k = -1; k <= 1; k++) {
						if(i == 0 && k == 0) continue;
						if(pos.x+i >= 0 && pos.x+i < SIZE) {
							if(pos.z+k >= 0 && pos.z+k < SIZE) {
								if(chunks[pos.x+i][pos.z+k] != null && chunkNeighborsExist(new ChunkPos(pos.x+i, pos.z+k))) {
									chunks[pos.x+i][pos.z+k].getData().buildLighting(new ChunkPos(pos.x+i, pos.z+k));
								}
							}
						}
					}
				}
				saver.save(pos, data);
			}else {
				chunks[pos.x][pos.z] = new Chunk(data);
				data.clearLighting();
				data.buildLighting(pos);
				for(int i = -1; i <= 1; i++) {
					for(int k = -1; k <= 1; k++) {
						if(i == 0 && k == 0) continue;
						if(pos.x+i >= 0 && pos.x+i < SIZE) {
							if(pos.z+k >= 0 && pos.z+k < SIZE) {
								if(chunks[pos.x+i][pos.z+k] != null && chunkNeighborsExist(new ChunkPos(pos.x+i, pos.z+k))) {
									chunks[pos.x+i][pos.z+k].getData().buildLighting(new ChunkPos(pos.x+i, pos.z+k));
								}
							}
						}
					}
				}
			}
			HashMap<BlockPos, Data> tiledatas = saver.loadTileData(pos, data);
			for(Entry<BlockPos, Data> e : tiledatas.entrySet()) {
				//System.out.println(e.getValue());
				setTileData(e.getKey(), e.getValue());
			}
			chunksList.add(pos);
		}
		return chunks[pos.x][pos.z];
	}
	public void save() {
		for(ChunkPos pos : chunksList) {
			Chunk chunk;
			if((chunk = getChunk(pos)) != null) {
				saver.save(pos, chunk.getData());
			}
		}
		for(Entity entity : entities) {
			if(entity.getTypeId() == EntityTypes.PLAYER.getId()) {
				saver.savePlayer(entity.getData());
			}
		}
		for(Entry<BlockPos, Data> e : tileData.entrySet()) {
			saver.saveTileData(e.getKey(), e.getValue());
		}
	}
	@Override
	public void dispose() {
		super.dispose();
		featuredChunks = null;
		features = null;
	}
	public boolean isLogInValid(String user, String passHash) {
		return saver.loadUserInfo(user) == null || saver.loadUserInfo(user).getPassHash().equals(passHash);
	}
	public int logInUser(NetConnection conn, String user, String pass) {
		Logger.debug("Logged In " + user);
		Entity entity = EntityTypes.PLAYER.createNewEntity(0, getSpawnPos(), new Vec3D());
		entity.getData().setName(user);
		entity.getData().setPasswordHash(pass);
		int id = spawnEntityLocally(entity);
		conn.setEntityId(id);
		conn.getPacketQueue().enqueue(new SpawnEntityPacket(entity.getTypeId(), id, entity.getPos(), entity.getRot(), entity.getData()));
		conn.getPacketQueue().enqueue(new SetCameraEntityPacket(id));
		return id;
	}
	public boolean isUserLoggedIn(String user) {
		for(Entity entity : entities) {
			if(entity.getTypeId() == EntityTypes.PLAYER.getId()) {
				if(entity.getData() != null && entity.getData().getName().equals(user)) {
					return true;
				}
			}
		}
		return false;
	}
}
