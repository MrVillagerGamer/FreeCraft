package net.freecraft.net.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import net.freecraft.util.ChunkPos;
import net.freecraft.util.CompressedData;
import net.freecraft.util.Compression;
import net.freecraft.world.ChunkData;

public class SetChunkPacket implements INetPacket {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private ChunkPos pos;
	private CompressedData data;
	public SetChunkPacket(ChunkPos pos, CompressedData data) {
		this.pos = pos;
		this.data = data;
	}
	public SetChunkPacket() {
		this(new ChunkPos(0, 0), null);
	}
	public ChunkPos getPos() {
		return pos;
	}
	public CompressedData getData() {
		return data;
	}
	public ChunkData decompressData() {
		try {
			byte[] decompData = Compression.decompress(data);
			
			ByteArrayInputStream bais = new ByteArrayInputStream(decompData);
			ObjectInputStream objectIn = new ObjectInputStream(bais);
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
			ObjectOutputStream objectOut = new ObjectOutputStream(baos);
			objectOut.writeObject(data);
			objectOut.flush();
			byte[] uncompData = baos.toByteArray();
			this.data = Compression.compress(uncompData);
			objectOut.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
