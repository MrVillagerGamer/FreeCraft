package net.freecraft.client.recipe.inventory;

import net.freecraft.item.ItemSlot;

public class CraftingResultItemSlot extends ItemSlot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CraftingInventory craftInv;
	public CraftingResultItemSlot(CraftingInventory craftInv) {
		this.craftInv = craftInv;
	}
	@Override
	public boolean canGive() {
		return false;
	}
	@Override
	public void onTake() {
		super.onTake();
		craftInv.onCraft();
	}
	@Override
	public void onGive() {
		super.onGive();
	}
}
