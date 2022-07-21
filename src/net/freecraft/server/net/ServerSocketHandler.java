package net.freecraft.server.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.freecraft.FreeCraft;
import net.freecraft.entity.Entity;
import net.freecraft.net.INetListener;
import net.freecraft.net.NetConnection;
import net.freecraft.net.packet.INetPacket;
import net.freecraft.net.packet.NoActionPacket;
import net.freecraft.net.packet.SetChunkPacket;
import net.freecraft.net.packet.SetTimePacket;
import net.freecraft.net.packet.SpawnEntityPacket;
import net.freecraft.server.FreeCraftServer;
import net.freecraft.server.world.ServerWorld;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.Vec3D;
import net.freecraft.world.Chunk;
import net.freecraft.world.World;

public class ServerSocketHandler implements Runnable {
	private static int curId = 0;
	private static ServerSocket ss;
	private Socket s;
	private NetConnection conn;
	private List<Chunk> sentChunks = new ArrayList<>();
	public ServerSocketHandler() {
		try {
			if(ss == null) {
				ss = new ServerSocket(25560);
			}
			s = ss.accept();
			World world = FreeCraftServer.get().getWorld();

			conn = new NetConnection(curId);
			curId++;
			conn.getPacketQueue().enqueue(new SetTimePacket(world.getTime()));

			FreeCraftServer.get().getConnections().add(conn);
			initChunks(world.getSpawnPos());
			initEntities();
			Thread thread = new Thread(this);
			thread.setName("SERVER-" + (conn.getId() + 2));
			thread.start();
		} catch(SocketException e) {
			e.printStackTrace();
			// Do not print, socket exceptions are expected on server shutdown
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void initChunks(Vec3D entityPos) {
		BlockPos pos = entityPos.toBlockPos();
		ServerWorld world = (ServerWorld)FreeCraftServer.get().getWorld();
		for(int x = -world.getViewDistance() - 1; x <= world.getViewDistance() + 1; x++) {
			for(int z = -world.getViewDistance() - 1; z <= world.getViewDistance() + 1; z++) {
				ChunkPos cpos = pos.toChunkPos();
				cpos.x += x;
				cpos.z += z;
				world.getOrGenChunk(cpos);

			}
		}
		for(int x = -world.getViewDistance(); x <= world.getViewDistance(); x++) {
			for(int z = -world.getViewDistance(); z <= world.getViewDistance(); z++) {
				ChunkPos cpos = pos.toChunkPos();
				cpos.x += x;
				cpos.z += z;
				Chunk chunk = world.getOrGenChunk(cpos);
				if(!sentChunks.contains(chunk)) {
					SetChunkPacket packet = new SetChunkPacket(cpos, null);
					packet.compressData(chunk.getData());
					conn.getPacketQueue().enqueue(packet);
					sentChunks.add(chunk);
				}
			}
		}
	}
	private void initEntities() {
		HashMap<Integer, Entity> entityIds = FreeCraft.get().getWorld().getEntityIds();
		for(Entry<Integer, Entity> e : entityIds.entrySet()) {
			Entity entity = e.getValue();
			SpawnEntityPacket packet = new SpawnEntityPacket(entity.getTypeId(), e.getKey(), entity.getPos(), entity.getRot(), entity.getData());
			conn.getPacketQueue().enqueue(packet);
		}
	}
	@Override
	public void run() {
		while(FreeCraftServer.get().isNetRunning() && !conn.shouldDisconnect()) {
			try {
				INetPacket packet = conn.getPacketQueue().dequeue();
				if(packet == null) packet = new NoActionPacket();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(packet);
				oos.flush();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				packet = (INetPacket)ois.readObject();
				INetListener listener = FreeCraftServer.get().getNetListener();
				listener.recv(conn, packet);
				Entity e = FreeCraftServer.get().getWorld().getEntity(conn.getEntityId());
				if(e != null) {
					initChunks(e.getPos());
				}
			} catch (IOException | ClassNotFoundException e) {
				//e.printStackTrace();
				try {
					s.close();
					FreeCraftServer.get().removeConnection(conn);
					if(FreeCraftServer.get().getConnections().size() == 0) {
						ss.close();
						ss = null;
						FreeCraftServer.get().handleShutdown();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		}
		//e.printStackTrace();
		try {
			s.close();
			FreeCraftServer.get().removeConnection(conn);
			if(FreeCraftServer.get().getConnections().size() == 0) {
				ss.close();
				ss = null;
				FreeCraftServer.get().handleShutdown();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return;
	}
}
