package net.freecraft.client.gui;

import java.awt.Graphics;

public abstract class UIComponent {
	protected int x, y, width, height;
	protected boolean focused;
	public boolean inBounds(int x, int y) {
		return x >= this.x && x <= this.x + width
				&& y >= this.y && y <= this.y + height;
	}
	public UIComponent() {
		focused = false;
	}
	public abstract void update();
	public abstract void render(Graphics g);
	public void focus() {
		focused = true;
	}
	public void unfocus() {
		focused = false;
	}
}
