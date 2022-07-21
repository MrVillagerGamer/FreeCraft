package net.freecraft.client.state;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.world.World;

public abstract class InGameState extends State {
	@Override
	public void stop() {

	}
	@Override
	public void update() {
		World world = FreeCraftClient.get().getWorld();
		world.update();
	}
}
