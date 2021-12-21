package net.freecraft.client.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.UIButton;
import net.freecraft.client.world.ClientWorld;

public class PauseState extends InGameState {
	private UIButton button1, button2, button3;
	@Override
	public void start() {
		int loc = -24;
		button1 = new UIButton("Resume Play", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = 0;
		button2 = new UIButton("Options", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = 24;
		button3 = new UIButton("Main Menu", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
	}
	@Override
	public void render(BufferedImage target) {
		// UI rendering
		BufferedImage uitarget = new BufferedImage(FreeCraftClient.get().getUIWidth(), FreeCraftClient.get().getUIHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = uitarget.createGraphics();
		g.setColor(new Color(0, true));
		g.fillRect(0, 0, FreeCraftClient.get().getUIWidth(), FreeCraftClient.get().getUIHeight());
		button1.render(g);
		button2.render(g);
		button3.render(g);
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
		if(button1.inBounds(x, y)) { // Resume Play
			State.set(States.PLAY);
		}else if(button2.inBounds(x, y)) { // Options
			// TODO
		}else if(button3.inBounds(x, y)) { // Main Menu
			FreeCraftClient.get().disconnect();
			State.set(States.MENU);
		}
	}
}
