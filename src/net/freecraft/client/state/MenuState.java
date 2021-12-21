package net.freecraft.client.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.UIButton;

public class MenuState extends State {
	private UIButton button1, button2;
	@Override
	public void start() {
		int loc = -12;
		button1 = new UIButton("Singleplayer", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = 12;
		button2 = new UIButton("Multiplayer", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
	}
	@Override
	public void update() {
		button1.update();
		button2.update();
	}
	@Override
	public void render(BufferedImage target) {
		// UI rendering
		BufferedImage uitarget = new BufferedImage(FreeCraftClient.get().getUIWidth(), FreeCraftClient.get().getUIHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = uitarget.createGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, FreeCraftClient.get().getUIWidth(), FreeCraftClient.get().getUIHeight());
		button1.render(g);
		button2.render(g);
		g.dispose();
		// Main rendering
		g = target.createGraphics();
		g.drawImage(uitarget, 0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight(), null);
		g.dispose();
		uitarget.flush();
	}
	@Override
	public void stop() {
		
	}
	@Override
	public void escape() {
		super.escape();
		FreeCraftClient.get().requestQuit();
	}
	@Override
	public void mouseClick(int x, int y) {
		super.mouseClick(x, y);
		if(button1.inBounds(x, y)) {
			FreeCraftClient.get().joinIntegratedServer();
		}
		if(button2.inBounds(x, y)) {
			State.set(States.MULTIPLAYER);
		}
	}
}
