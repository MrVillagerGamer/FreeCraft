package net.freecraft.client.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.UIButton;
import net.freecraft.client.gui.UIComponent;
import net.freecraft.client.gui.UILabel;
import net.freecraft.client.gui.UITextField;

public class LogInState extends State {
	private UIButton button1, button2;
	private UITextField field1, field2;
	private UILabel label1, label2, label3;
	@Override
	public void start() {
		int loc = -84;
		label3 = new UILabel("Must always put same info or lose your stuff!", FreeCraftClient.get().getUIWidth()/2-150, FreeCraftClient.get().getUIHeight()/2-10+loc, 300, 20);
		loc = -52;
		label1 = new UILabel("Username:", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = -36;
		field1 = new UITextField("", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = -4;
		label2 = new UILabel("Password:", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = 12;
		field2 = new UITextField("", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = 36;
		button1 = new UIButton("Log In", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
		loc = 60;
		button2 = new UIButton("Quit Game", FreeCraftClient.get().getUIWidth()/2-100, FreeCraftClient.get().getUIHeight()/2-10+loc, 200, 20);
	}
	@Override
	public void update() {
		field1.update();
		field2.update();
		label1.update();
		label2.update();
		label3.update();
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
		field1.render(g);
		field2.render(g);
		label1.render(g);
		label2.render(g);
		label3.render(g);
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
		return new UIComponent[] {field1, field2, label1, label2, label3, button1, button2};
	}
	@Override
	public void mouseClick(int x, int y) {
		super.mouseClick(x, y);
		if(button1.inBounds(x, y)) {
			FreeCraftClient.get().setUsername(field1.getText());
			FreeCraftClient.get().setPasswordHash(field2.getText());
			State.set(States.MENU);
		}
		if(button2.inBounds(x, y)) {
			FreeCraftClient.get().requestQuit();
		}
		if(field1.inBounds(x, y)) {
			field1.focus();
		}else {
			field1.unfocus();
		}
		if(field2.inBounds(x, y)) {
			field2.focus();
		}else {
			field2.unfocus();
		}
	}
	@Override
	public void escape() {
		FreeCraftClient.get().requestQuit();
	}
}
