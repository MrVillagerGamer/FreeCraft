package net.freecraft.client.gui.container;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.recipe.inventory.CraftingInventory3x3;
import net.freecraft.client.recipe.inventory.CraftingResultItemSlot;
import net.freecraft.entity.Entity;
import net.freecraft.item.inventory.Inventory;

public class UICraftingContainer extends UIContainer {
	private CraftingInventory3x3 craftInv3x3;
	private CraftingResultItemSlot resultSlot;
	public UICraftingContainer() {
		craftInv3x3 = new CraftingInventory3x3();
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
				for(int j = 0; j < 3; j++) {
					for(int i = 0; i < 3; i++) {
						slots.add(new UISlot(x + (4 + i) * (UISlot.SIZE + 2) + 7, y + height - 7 - UISlot.SIZE - (j + 5) * (UISlot.SIZE + 2) - UISlot.SIZE / 2, craftInv3x3.getSlot(j * 3 + i)));
					}
				}
				resultSlot = new CraftingResultItemSlot(craftInv3x3);
				slots.add(new UISlot(x + 8 * (UISlot.SIZE + 2) + 7, y + height - 7 - UISlot.SIZE - (1 + 5) * (UISlot.SIZE + 2) - UISlot.SIZE / 2, resultSlot));
			}
		}
	}
	@Override
	public void update() {
		super.update();
		resultSlot.set(craftInv3x3.getResult());
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
