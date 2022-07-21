package net.freecraft.client.gui.container;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.client.gui.UIComponent;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.item.ItemSlot;

public class UISlot extends UIComponent {
	public static final int SIZE = 21;
	private ItemSlot slot;
	private boolean selected;
	public UISlot(int x, int y, ItemSlot slot) {
		this.x = x;
		this.y = y;
		this.width = SIZE;
		this.height = SIZE;
		this.slot = slot;
		this.selected = false;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public ItemSlot getSlot() {
		return slot;
	}
	@Override
	public void update() {

	}
	@Override
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, y, width, height);
		if(selected) {
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width, height);
		}
		g.setColor(Color.DARK_GRAY);
		g.drawLine(x+1, y+1, x+width-2, y+1);
		g.drawLine(x+1, y+1, x+1, y+height-2);
		g.setColor(Color.WHITE);
		g.drawLine(x+2, y+height-1, x+width-1, y+height-1);
		g.drawLine(x+width-1, y+2, x+width-1, y+height-1);
		if(!slot.get().isEmpty()) {
			BufferedImage img = ClientRegistries.ITEM_ICONS.get(slot.get().getId()).getAWTImage();
			g.drawImage(img, x + 3, y + 3 , 16, 16, null);
		}
		if(slot.get().getCount() > 0) {
			g.setColor(Color.YELLOW);
			g.drawString("" + slot.get().getCount(), x + width - 10, y + height);
		}
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public boolean isSelected() {
		return selected;
	}
}
