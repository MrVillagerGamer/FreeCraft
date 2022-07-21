package net.freecraft.client.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.container.UISlot;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.entity.Entity;
import net.freecraft.item.ItemStack;
import net.freecraft.item.inventory.Inventory;

public class UIHotbar extends UIComponent implements IComponentMouseHandler {
	private int size, sel;
	public UIHotbar(int size) {
		this.size = size;
		this.sel = 0;
		width = size * (UISlot.SIZE + 2) + 3;
		height = (UISlot.SIZE + 2) + 3;
		x = FreeCraftClient.get().getUIWidth() / 2 - width / 2;
		y = FreeCraftClient.get().getUIHeight() - height;
	}
	@Override
	public void update() {

	}
	@Override
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		g.setColor(Color.WHITE);
		g.drawLine(x+1, y+1, x+width-2, y+1);
		g.drawLine(x+1, y+1, x+1, y+height-2);
		g.setColor(Color.DARK_GRAY);
		g.drawLine(x+2, y+height-1, x+width-1, y+height-1);
		g.drawLine(x+width-1, y+2, x+width-1, y+height-1);
		Entity viewEntity = FreeCraftClient.get().getRenderer().getViewEntity();
		if(viewEntity != null) {
			Inventory inv = viewEntity.getData().getInventory();
			if(inv != null) {
				for(int i = 0; i < size; i++) {
					int x = this.x + i * (UISlot.SIZE + 2) + 3;
					int y = this.y + 3;
					int width = UISlot.SIZE;
					int height = UISlot.SIZE;
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(x, y, width, height);
					if(sel == i) {
						g.setColor(Color.BLACK);
						g.drawRect(x, y, width, height);
					}
					g.setColor(Color.DARK_GRAY);
					g.drawLine(x+1, y+1, x+width-2, y+1);
					g.drawLine(x+1, y+1, x+1, y+height-2);
					g.setColor(Color.WHITE);
					g.drawLine(x+2, y+height-1, x+width-1, y+height-1);
					g.drawLine(x+width-1, y+2, x+width-1, y+height-1);
					ItemStack stack = inv.get(i);
					if(!stack.isEmpty()) {
						BufferedImage img = ClientRegistries.ITEM_ICONS.get(stack.getId()).getAWTImage();
						g.drawImage(img, x + 3, y + 3 , 16, 16, null);
					}
				}
			}
		}
	}
	@Override
	public void wheelMove(int amt) {
		sel = (sel + amt + 18) % 9;
	}
	public int getSelectedSlot() {
		return sel;
	}
	@Override
	public void mouseClick(int x, int y, int btn) {

	}
}
