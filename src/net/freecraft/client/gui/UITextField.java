package net.freecraft.client.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class UITextField extends UIComponent implements IComponentKeyHandler {
	private String text = "";
	public UITextField(String text, int x, int y, int width, int height) {
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
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(x+1, y+1, x+width-2, y+1);
		g.drawLine(x+1, y+1, x+1, y+height-2);
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(x+2, y+height-1, x+width-1, y+height-1);
		g.drawLine(x+width-1, y+2, x+width-1, y+height-1);
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
		if(focused) {
			if(c == '\b') {
				delChar();
			}else if(c >= 32 && c <= 128){
				addChar(c);
			}
		}
	}
	@Override
	public void keyUp(int code, char c) {

	}
}
