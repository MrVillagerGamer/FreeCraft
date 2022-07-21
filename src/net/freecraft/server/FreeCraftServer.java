package net.freecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.freecraft.FreeCraft;
import net.freecraft.net.INetHandler;
import net.freecraft.net.INetListener;
import net.freecraft.net.NetConnection;
import net.freecraft.server.net.ServerNetHandler;
import net.freecraft.server.net.ServerNetListener;
import net.freecraft.server.net.ServerSocketHandler;
import net.freecraft.server.world.ServerWorld;
import net.freecraft.server.world.WorldGenerator;
import net.freecraft.server.world.feature.Features;
import net.freecraft.util.Logger;
import net.freecraft.world.World;

public class FreeCraftServer extends FreeCraft {
	private static FreeCraftServer inst;
	private static List<NetConnection> connections;
	private static HashMap<Integer, NetConnection> connectionIds;
	public static FreeCraftServer get() {
		return inst;
	}
	public static void start() {
		new FreeCraftServer();
	}
	private WorldGenerator worldGen;
	private boolean netRunning = true;
	public FreeCraftServer() {
		super();
		Features.init();
		inst = this;
		connections = new ArrayList<>();
		connectionIds = new HashMap<>();
		world = new ServerWorld();
		Thread thread = new Thread(this);
		thread.setName("SERVER-0");
		thread.start();
	}
	@Override
	public INetHandler getNetHandler() {
		if(netHandler == null) netHandler = new ServerNetHandler();
		return super.getNetHandler();
	}
	@Override
	public INetListener getNetListener() {
		if(netListener == null) netListener = new ServerNetListener();
		return super.getNetListener();
	}
	@Override
	public World getWorld() {
		return super.getWorld();
	}
	@Override
	public boolean isNetRunning() {
		return true;
	}
	public List<NetConnection> getConnections() {
		return connections;
	}
	public HashMap<Integer, NetConnection> getConnectionIds() {
		return connectionIds;
	}
	@Override
	public void run() {
		Logger.debug("Starting server...");
		worldGen = new WorldGenerator();
		new ServerSocketHandler();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(netRunning) {
					new ServerSocketHandler();
				}
			}
		});
		thread.setName("SERVER-1");
		thread.start();
		while(netRunning) {
			getWorld().update();
		}
		world.dispose();
		world = null;
		inst = null;
	}
	public WorldGenerator getWorldGenerator() {
		return worldGen;
	}
	public void removeConnection(NetConnection conn) {
		connectionIds.remove(conn.getId());
		connections.remove(conn);
	}
	public void handleShutdown() {
		Logger.debug("Stopping server...");
		((ServerWorld)world).save();
		netRunning = false;
	}
}
