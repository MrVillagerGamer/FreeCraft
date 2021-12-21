package net.freecraft.client.recipe.inventory;

import net.freecraft.item.ItemStack;
import net.freecraft.item.inventory.Inventory;

public abstract class CraftingInventory extends Inventory {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CraftingInventory(int size) {
		super(size);
	}
	public abstract void onCraft();
	public abstract ItemStack getResult();
}
