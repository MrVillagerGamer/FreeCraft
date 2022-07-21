package net.freecraft.client.net;

import net.freecraft.FreeCraft;
import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.entity.IRenderEntity;
import net.freecraft.client.render.Renderer;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.client.world.ClientChunk;
import net.freecraft.client.world.ClientWorld;
import net.freecraft.entity.Entity;
import net.freecraft.net.INetListener;
import net.freecraft.net.NetConnection;
import net.freecraft.net.packet.ChatMessagePacket;
import net.freecraft.net.packet.INetPacket;
import net.freecraft.net.packet.SetBlockPacket;
import net.freecraft.net.packet.SetCameraEntityPacket;
import net.freecraft.net.packet.SetChunkPacket;
import net.freecraft.net.packet.SetEntityDataPacket;
import net.freecraft.net.packet.SetEntityPosPacket;
import net.freecraft.net.packet.SetEntityRotPacket;
import net.freecraft.net.packet.SetTileDataPacket;
import net.freecraft.net.packet.SetTimePacket;
import net.freecraft.net.packet.SpawnEntityPacket;
import net.freecraft.util.ChunkPos;
import net.freecraft.world.ChunkData;
import net.freecraft.world.World;

public class ClientNetListener implements INetListener {
	public boolean setBlock(NetConnection info, SetBlockPacket packet) {
		ClientWorld world = (ClientWorld)FreeCraftClient.get().getWorld();
		world.setBlockLocally(packet.getPos(), packet.getBlockId());
		return true;
	}
	public boolean setChunk(NetConnection info, SetChunkPacket packet) {
		ClientWorld world = (ClientWorld)FreeCraftClient.get().getWorld();
		ChunkPos chunkPos = packet.getPos();
		ChunkData chunkData = packet.decompressData();
		world.setChunk(chunkPos, new ClientChunk(chunkPos, chunkData));
		return true;
	}
	public boolean chatMessage(NetConnection info, ChatMessagePacket packet) {
		String msg = packet.getMessage();
		System.out.println("<" + packet.getName() + "> " + msg);
		return true;
	}
	public boolean setEntityPos(NetConnection conn, SetEntityPosPacket packet) {
		World world = FreeCraft.get().getWorld();
		world.getEntity(packet.getId()).setPosLocally(packet.getPos());
		return true;
	}
	public boolean setEntityRot(NetConnection conn, SetEntityRotPacket packet) {
		World world = FreeCraft.get().getWorld();
		world.getEntity(packet.getId()).setRotLocally(packet.getRot());
		return true;
	}
	public boolean spawnEntity(NetConnection conn, SpawnEntityPacket packet) {
		World world = FreeCraft.get().getWorld();
		IRenderEntity entity = ClientRegistries.RENDER_ENTITY_TYPES.get(packet.getTypeId()).createNewRenderEntity(packet.getId(), packet.getPos(), packet.getRot());
		if(entity instanceof Entity) {
			((Entity)entity).setDataLocally(packet.getData());
			world.setEntityLocally(packet.getId(), (Entity)entity);
		}
		return true;
	}
	public boolean setCameraEntity(NetConnection conn, SetCameraEntityPacket packet) {
		FreeCraftClient client = FreeCraftClient.get();
		Renderer renderer = client.getRenderer();
		renderer.setViewEntityId(packet.getEntityId());
		return true;
	}
	public boolean setEntityData(NetConnection conn, SetEntityDataPacket packet) {
		World world = FreeCraft.get().getWorld();
		world.getEntity(packet.getId()).setDataLocally(packet.getData());
		return true;
	}
	public boolean setTime(NetConnection conn, SetTimePacket packet) {
		World world = FreeCraft.get().getWorld();
		world.setTimeLocally(packet.getTime());
		return true;
	}
	public boolean setTileData(NetConnection conn, SetTileDataPacket packet) {
		World world = FreeCraft.get().getWorld();
		world.setTileDataLocally(packet.getPos(), packet.getData());
		return true;
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
		}else if(packet instanceof SetCameraEntityPacket) {
			return setCameraEntity(conn, (SetCameraEntityPacket)packet);
		}else if(packet instanceof SetEntityDataPacket) {
			return setEntityData(conn, (SetEntityDataPacket)packet);
		}else if(packet instanceof SetTimePacket) {
			return setTime(conn, (SetTimePacket)packet);
		}else if(packet instanceof SetTileDataPacket) {
			return setTileData(conn, (SetTileDataPacket)packet);
		}
		return false;
	}
}
