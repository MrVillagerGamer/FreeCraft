package net.freecraft.client.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class UILabel extends UIComponent implements IComponentKeyHandler {
	private String text = "";
	public UILabel(String text, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}
	private int getStringWidth(String str, FontMetrics mets) {
		int len = 0;
		for(int i = 0; i < str.length(); i++) {
			len += mets.charWidth(str.charAt(i));
		}
		return len;
	}
	@Override
	public void update() {
		
	}
	@Override
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		FontMetrics mets = g.getFontMetrics();
		int w = getStringWidth(text, mets);
		g.drawString(text, x+width/2-w/2, y+height/2+mets.getHeight()/4);
	}
	public void addChar(char c) {
		text += c;
	}
	public String getText() {
		return text;
	}
	public void delChar() {
		if(text.length() > 0) {
			text = text.substring(0, text.length()-1);
		}
	}
	@Override
	public void keyDown(int code, char c) {
		
	}
	@Override
	public void keyUp(int code, char c) {
		
	}
}
