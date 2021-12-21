package net.freecraft.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import net.freecraft.block.Block;
import net.freecraft.data.Data;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.Registries;
import net.freecraft.util.stream.CompressedInputStream;
import net.freecraft.util.stream.CompressedOutputStream;
import net.freecraft.world.Chunk;
import net.freecraft.world.ChunkData;

public class WorldSaver {
	private String saveDir = "";
	public WorldSaver(String path) {
		saveDir = System.getenv("APPDATA");
		saveDir += "\\.freecraft\\";
		File file = new File(saveDir);
		if(!file.exists()) {
			file.mkdir();
		}
		saveDir += "saves\\";
		file = new File(saveDir);
		if(!file.exists()) {
			file.mkdir();
		}
		saveDir += path + "\\";
		file = new File(saveDir);
		if(!file.exists()) {
			file.mkdir();
		}
	}
	public Data loadPlayer(String name, String hash) {
		String path = saveDir + "players\\data_" + name + "_" + hash;
		File file = new File(path);
		if(!file.exists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			ObjectInputStream ois = new ObjectInputStream(fis);
			Data data = (Data)ois.readObject();
			//Logger.debug("Loading player");
			ois.close();
			fis.close();
			return data;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	public ChunkData load(ChunkPos pos) {
		String path = saveDir + "chunks\\blocks_" + pos.x + "_" + pos.z;
		File file = new File(path);
		if(!file.exists()) {
			return null;
		}
		try {
			ChunkData data = null;
			FileInputStream fis = new FileInputStream(new File(path));
			ObjectInputStream ois = new ObjectInputStream(new CompressedInputStream(fis));
			char[][][] blocks = (char[][][])ois.readObject();
			data = new ChunkData(blocks);
			//Logger.debug("Loading " + pos);
			ois.close();
			fis.close();
			return data;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	public HashMap<BlockPos,Data> loadTileData(ChunkPos pos, ChunkData data) {
		HashMap<BlockPos, Data> tiledatas = new HashMap<>();
		for(int x = 0; x < Chunk.SIZE; x++) {
			for(int z = 0; z < Chunk.SIZE; z++) {
				for(int y = 0; y < Chunk.HEIGHT; y++) {
					BlockPos localPos = new BlockPos(x, y, z);
					int id = data.getBlock(localPos);
					Block b = Registries.BLOCKS.get(id);
					BlockPos globalPos = localPos.fromLocal(pos);
					if(b.hasTileData(globalPos)) {
						Data tiledata = loadTileData(globalPos);
						if(tiledata == null) tiledata = b.genTileData(globalPos, Data.DEFAULT);
						tiledatas.put(globalPos, tiledata);
					}
				}
			}
		}
		return tiledatas;
	}
	public Data loadTileData(BlockPos pos) {
		String path = saveDir + "tiledata\\data_" + pos.x + "_" + pos.y + "_" + pos.z;
		File file = new File(path);
		if(!file.exists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			ObjectInputStream ois = new ObjectInputStream(fis);
			Data data = (Data)ois.readObject();
			//Logger.debug("Loading player");
			ois.close();
			fis.close();
			return data;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void saveTileData(BlockPos pos, Data data) {
		String path = saveDir + "tiledata\\";
		File file = new File(path);
		if(!file.exists()) {
			file.mkdir();
		}
		path = saveDir + "tiledata\\data_" + pos.x + "_" + pos.y + "_" + pos.z;
		file = new File(path);
		if(file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File(path));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.flush();
			oos.close();
			fos.close();
			//Logger.debug("Saving player");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void savePlayer(Data data) {
		String hash = data.getPasswordHash();
		String path = saveDir + "players\\";
		File file = new File(path);
		if(!file.exists()) {
			file.mkdir();
		}
		path = saveDir + "players\\data_" + data.getName() + "_" + hash;
		file = new File(path);
		if(file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File(path));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.flush();
			oos.close();
			fos.close();
			//Logger.debug("Saving player");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void save(ChunkPos pos, ChunkData data) {
		String path = saveDir + "chunks\\";
		File file = new File(path);
		if(!file.exists()) {
			file.mkdir();
		}
		path = saveDir + "chunks\\blocks_" + pos.x + "_" + pos.z;
		file = new File(path);
		if(file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File(path));
			ObjectOutputStream oos = new ObjectOutputStream(new CompressedOutputStream(fos));
			oos.writeObject(data.getBlocks());
			oos.flush();
			oos.close();
			fos.close();
			//Logger.debug("Saving " + pos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public UserInfo loadUserInfo(String userName) {
		return null;
	}
}
