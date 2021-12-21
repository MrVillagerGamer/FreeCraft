package net.freecraft.client.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.UIComponent;
import net.freecraft.client.gui.container.UICraftingContainer;
import net.freecraft.client.world.ClientWorld;

public class CraftingState extends InGameState {
	private UICraftingContainer container;
	@Override
	public void start() {
		container = new UICraftingContainer();
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
