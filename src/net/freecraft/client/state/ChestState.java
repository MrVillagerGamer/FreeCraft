package net.freecraft.client.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.FreeCraft;
import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.UIComponent;
import net.freecraft.client.gui.container.UIChestContainer;
import net.freecraft.client.world.ClientWorld;
import net.freecraft.data.Data;
import net.freecraft.util.BlockPos;

public class ChestState extends InGameState {
	private UIChestContainer container;
	private BlockPos pos;
	private Data data;
	public void set(BlockPos pos, Data data) {
		this.pos = pos;
		this.data = data;
	}
	@Override
	public void start() {
		container = new UIChestContainer(pos, data);
	}
	@Override
	public void update() {
		super.update();
		container.update();
	}
	@Override
	public void render(BufferedImage target) {
		// UI rendering
		BufferedImage uitarget = new BufferedImage(FreeCraftClient.get().getUIWidth(), FreeCraftClient.get().getUIHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = uitarget.createGraphics();
		g.setColor(new Color(0, true));
		g.fillRect(0, 0, FreeCraftClient.get().getUIWidth(), FreeCraftClient.get().getUIHeight());
		container.render(g);
		g.dispose();
		// Main rendering
		g = target.createGraphics();
		g.clearRect(0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight());
		ClientWorld world = (ClientWorld)FreeCraftClient.get().getWorld();
		world.render();
		FreeCraftClient.get().getRenderer().swapBuffers(g);
		g.drawImage(uitarget, 0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight(), null);
		g.dispose();
		uitarget.flush();
	}
	@Override
	public void stop() {
		super.stop();
		FreeCraft.get().getWorld().setTileData(pos, data);
	}
	@Override
	public void escape() {
		super.escape();
		State.set(States.PLAY);
	}
	@Override
	public void mouseClick(int x, int y) {
		super.mouseClick(x, y);
	}
	@Override
	public UIComponent[] getUIComponents() {
		return new UIComponent[] {container};
	}
}
