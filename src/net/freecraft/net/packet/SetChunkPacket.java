package net.freecraft.net.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import net.freecraft.util.ChunkPos;
import net.freecraft.world.ChunkData;

public class SetChunkPacket implements INetPacket {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChunkPos pos;
	private byte[] data;
	public SetChunkPacket(ChunkPos pos, byte[] data) {
		this.pos = pos;
		this.data = data;
	}
	public SetChunkPacket() {
		this(new ChunkPos(0, 0), null);
	}
	public ChunkPos getPos() {
		return pos;
	}
	public byte[] getData() {
		return data;
	}
	public ChunkData decompressData() {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			InflaterInputStream gzipIn = new InflaterInputStream(bais);
			ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
			ChunkData data = (ChunkData)objectIn.readObject();
			objectIn.close();
			return data;
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void compressData(ChunkData data) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DeflaterOutputStream gzipOut = new DeflaterOutputStream(baos);
			ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
			objectOut.writeObject(data);
			objectOut.flush();
			gzipOut.close();
			this.data = baos.toByteArray();
			objectOut.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
