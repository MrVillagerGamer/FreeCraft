package net.freecraft.client.gui;

import java.awt.Color;
import java.awt.Graphics;

import net.freecraft.client.FreeCraftClient;

public class UICrosshair extends UIComponent {
	public UICrosshair() {
		float aspect = FreeCraftClient.get().getUIWidth() / (float)FreeCraftClient.get().getUIHeight();
		this.width = (int)(FreeCraftClient.get().getUIWidth()/aspect/50);
		this.height = FreeCraftClient.get().getUIHeight()/50;
		this.x = FreeCraftClient.get().getUIWidth()/2-width/2;
		this.y = FreeCraftClient.get().getUIHeight()/2-height/2;
	}
	@Override
	public void update() {
		
	}
	@Override
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect(x+width/3, y, width/3, height);
		g.fillRect(x, y+height/3, width, height/3);
	}
}
