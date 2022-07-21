package net.freecraft.client.gui.container;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.data.Data;
import net.freecraft.entity.Entity;
import net.freecraft.item.inventory.ChestInventory;
import net.freecraft.item.inventory.Inventory;
import net.freecraft.util.BlockPos;

public class UIChestContainer extends UIContainer{
	public UIChestContainer(BlockPos pos, Data data) {
		width = 9 * (UISlot.SIZE + 2) + 12;
		height = 8 * (UISlot.SIZE + 2) + 12 + UISlot.SIZE / 2;
		x = FreeCraftClient.get().getUIWidth() / 2 - width / 2;
		y = FreeCraftClient.get().getUIHeight() / 2 - height / 2;
		Entity viewEntity = FreeCraftClient.get().getRenderer().getViewEntity();
		if(viewEntity != null) {
			Inventory inv = viewEntity.getData().getInventory();
			if(inv != null) {
				for(int i = 0; i < 9; i++) {
					slots.add(new UISlot(x + i * (UISlot.SIZE + 2) + 7, y + height - 7 - UISlot.SIZE, inv.getSlot(i)));
				}
				for(int j = 0; j < 3; j++) {
					for(int i = 0; i < 9; i++) {
						slots.add(new UISlot(x + i * (UISlot.SIZE + 2) + 7, y + height - 7 - UISlot.SIZE - (j + 1) * (UISlot.SIZE + 2) - UISlot.SIZE / 2, inv.getSlot(j * 9 + i + 9)));
					}
				}
				ChestInventory chestInv = (ChestInventory) data.getInventory();
				for(int j = 0; j < 3; j++) {
					for(int i = 0; i < 9; i++) {
						slots.add(new UISlot(x + i * (UISlot.SIZE + 2) + 7, y + height - 7 - UISlot.SIZE - (j + 4) * (UISlot.SIZE + 2) - UISlot.SIZE / 2, chestInv.getSlot(j * 9 + i)));
					}
				}
			}
		}

	}
	@Override
	public void update() {
		super.update();
	}
	@Override
	public void mouseClick(int x, int y, int btn) {
		super.mouseClick(x, y, btn);
	}
	@Override
	public void wheelMove(int amt) {
		super.wheelMove(amt);
	}
}
