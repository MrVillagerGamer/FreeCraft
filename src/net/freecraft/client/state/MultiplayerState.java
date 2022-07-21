package net.freecraft.client.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.UIButton;
import net.freecraft.client.gui.UIComponent;
import net.freecraft.client.gui.UITextField;

public class MultiplayerState extends State {
	private UIButton button1, button2;
	private UITextField field;
	@Override
	public void start() {
		int loc = -24;
		field = new UITextField("", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = 0;
		button1 = new UIButton("Join Server", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = 24;
		button2 = new UIButton("Main Menu", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
	}
	@Override
	public void update() {
		field.update();
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
		field.render(g);
		button1.render(g);
		button2.render(g);
		g.dispose();
		// Main rendering
		g = target.createGraphics();
		g.drawImage(uitarget, 0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight(), null);
		uitarget.flush();
	}
	@Override
	public void stop() {

	}
	@Override
	public UIComponent[] getUIComponents() {
		return new UIComponent[] {field, button1, button2};
	}
	@Override
	public void mouseClick(int x, int y) {
		super.mouseClick(x, y);
		if(button1.inBounds(x, y)) {
			FreeCraftClient.get().joinServer(field.getText());
		}
		if(button2.inBounds(x, y)) {
			State.set(States.MENU);
		}
		if(field.inBounds(x, y)) {
			field.focus();
		}else {
			field.unfocus();
		}
	}
	@Override
	public void escape() {
		State.set(States.MENU);
	}
}
