package net.freecraft.server.net;

import net.freecraft.FreeCraft;
import net.freecraft.entity.Entity;
import net.freecraft.net.INetHandler;
import net.freecraft.net.INetListener;
import net.freecraft.net.NetConnection;
import net.freecraft.net.packet.ChatMessagePacket;
import net.freecraft.net.packet.INetPacket;
import net.freecraft.net.packet.LogInPacket;
import net.freecraft.net.packet.SetBlockPacket;
import net.freecraft.net.packet.SetChunkPacket;
import net.freecraft.net.packet.SetEntityDataPacket;
import net.freecraft.net.packet.SetEntityPosPacket;
import net.freecraft.net.packet.SetEntityRotPacket;
import net.freecraft.net.packet.SetTileDataPacket;
import net.freecraft.net.packet.SetTimePacket;
import net.freecraft.net.packet.SpawnEntityPacket;
import net.freecraft.server.FreeCraftServer;
import net.freecraft.server.world.ServerWorld;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.Registries;
import net.freecraft.world.Chunk;
import net.freecraft.world.ChunkData;
import net.freecraft.world.World;

public class ServerNetListener implements INetListener {
	public boolean setBlock(NetConnection info, SetBlockPacket packet) {
		ServerWorld world = (ServerWorld)FreeCraftServer.get().getWorld();
		ChunkPos chunkPos = packet.getPos().toChunkPos();
		BlockPos blockPos = packet.getPos().toLocal(chunkPos);
		Chunk chunk = world.getChunk(chunkPos);
		chunk.setBlock(blockPos, packet.getBlockId());
		FreeCraftServer.get().getNetHandler().send(info, new SetBlockPacket(packet.getPos(), packet.getBlockId()));
		return true;
	}
	public boolean setChunk(NetConnection info, SetChunkPacket packet) {
		ServerWorld world = (ServerWorld)FreeCraftServer.get().getWorld();
		ChunkPos chunkPos = packet.getPos();
		ChunkData chunkData = packet.decompressData();
		world.setChunk(chunkPos, new Chunk(chunkData));
		return true;
	}
	public boolean chatMessage(NetConnection info, ChatMessagePacket packet) {
		System.out.println("Client: " + packet.getMessage());
		return true;
	}
	public boolean setEntityPos(NetConnection conn, SetEntityPosPacket packet) {
		World world = FreeCraft.get().getWorld();
		if(conn.getEntityId() == packet.getId()) {
			world.getEntity(conn.getEntityId()).setPosLocally(packet.getPos());
			INetHandler handler = FreeCraft.get().getNetHandler();
			handler.send(conn, packet);
			return true;
		}
		return false;
	}
	public boolean setTileData(NetConnection conn, SetTileDataPacket packet) {
		World world = FreeCraft.get().getWorld();
		world.setTileDataLocally(packet.getPos(), packet.getData());
		INetHandler handler = FreeCraft.get().getNetHandler();
		handler.send(conn, packet);
		return true;
	}
	public boolean setEntityRot(NetConnection conn, SetEntityRotPacket packet) {
		World world = FreeCraft.get().getWorld();
		if(conn.getEntityId() == packet.getId()) {
			world.getEntity(conn.getEntityId()).setRotLocally(packet.getRot());
			INetHandler handler = FreeCraft.get().getNetHandler();
			handler.send(conn, packet);
			return true;
		}
		return false;
	}
	public boolean spawnEntity(NetConnection conn, SpawnEntityPacket packet) {
		World world = FreeCraft.get().getWorld();
		Entity entity = Registries.ENTITY_TYPES.get(packet.getTypeId()).createNewEntity(packet.getId(), packet.getPos(), packet.getRot());
		world.setEntityLocally(packet.getId(), entity);
		INetHandler handler = FreeCraft.get().getNetHandler();
		handler.send(conn, packet);
		return true;
	}
	public boolean setTime(NetConnection conn, SetTimePacket packet) {
		World world = FreeCraft.get().getWorld();
		world.setTimeLocally(packet.getTime());
		INetHandler handler = FreeCraft.get().getNetHandler();
		handler.send(conn, packet);
		return true;
	}
	public boolean setEntityData(NetConnection conn, SetEntityDataPacket packet) {
		World world = FreeCraft.get().getWorld();
		// Only set entity data if the sender is the target
		if(conn.getEntityId() == packet.getId()) {
			world.getEntity(packet.getId()).setDataLocally(packet.getData());
			INetHandler handler = FreeCraft.get().getNetHandler();
			handler.send(conn, packet);
			return true;
		}
		return false;
	}
	public boolean logIn(NetConnection conn, LogInPacket login) {
		String user = login.getUsername();
		String pass = login.getPasswordHash();
		ServerWorld world = (ServerWorld)FreeCraft.get().getWorld();
		boolean b = !world.isUserLoggedIn(user) && world.isLogInValid(user, pass);
		if(!b) {
			conn.signalDisconnect();
		}else {
			world.logInUser(conn, user, pass);
		}
		return b;
	}
	@Override
	public boolean recv(NetConnection conn, INetPacket packet) {
		if(packet instanceof SetBlockPacket) {
			return setBlock(conn, (SetBlockPacket)packet);
		}else if(packet instanceof SetChunkPacket) {
			return setChunk(conn, (SetChunkPacket)packet);
		}else if(packet instanceof ChatMessagePacket) {
			return chatMessage(conn, (ChatMessagePacket)packet);
		}else if(packet instanceof SetEntityPosPacket) {
			return setEntityPos(conn, (SetEntityPosPacket)packet);
		}else if(packet instanceof SetEntityRotPacket) {
			return setEntityRot(conn, (SetEntityRotPacket)packet);
		}else if(packet instanceof SpawnEntityPacket) {
			return spawnEntity(conn, (SpawnEntityPacket)packet);
		}else if(packet instanceof SetEntityDataPacket) {
			return setEntityData(conn, (SetEntityDataPacket)packet);
		}else if(packet instanceof SetTimePacket) {
			return setTime(conn, (SetTimePacket)packet);
		}else if(packet instanceof LogInPacket) {
			return logIn(conn, (LogInPacket)packet);
		}else if(packet instanceof SetTileDataPacket) {
			return setTileData(conn, (SetTileDataPacket)packet);
		}
		return false;
	}
}
