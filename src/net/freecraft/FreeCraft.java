package net.freecraft;


import java.io.IOException;

import net.freecraft.block.Blocks;
import net.freecraft.client.FreeCraftClient;
import net.freecraft.entity.EntityTypes;
import net.freecraft.item.Items;
import net.freecraft.net.INetHandler;
import net.freecraft.net.INetListener;
import net.freecraft.server.FreeCraftServer;
import net.freecraft.world.World;
import net.freecraft.world.biome.Biomes;

public abstract class FreeCraft implements Runnable {
	public static FreeCraft get() {
		if(Thread.currentThread().getName().startsWith("CLIENT")) {
			return FreeCraftClient.get();
		}else {
			return FreeCraftServer.get();
		}
	}
	protected World world;
	protected INetHandler netHandler;
	protected INetListener netListener;
	protected boolean running;
	public FreeCraft() {
		running = true;
	}
	public World getWorld() {
		return world;
	}
	public INetHandler getNetHandler() {
		return netHandler;
	}
	public INetListener getNetListener() {
		return netListener;
	}
	public boolean isRunning() {
		return running;
	}
	public void exit() {
		running = false;
		System.exit(0);
	}
	public boolean isNetRunning() {
		return false;
	}
	public static void main(String[] args) throws IOException {
		Blocks.init();
		Items.init();
		EntityTypes.init();
		Biomes.init();
		new FreeCraftClient();
	}
}
