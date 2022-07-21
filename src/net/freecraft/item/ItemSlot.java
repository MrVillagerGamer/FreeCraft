package net.freecraft.item;

import java.io.Serializable;

public class ItemSlot implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private ItemStack stack;
	public ItemSlot() {
		stack = ItemStack.createEmpty();
	}
	public void set(ItemStack stack) {
		this.stack = stack.copy();
	}
	public ItemStack get() {
		return stack;
	}
	public boolean canGive() {
		return true;
	}
	public boolean canTake() {
		return true;
	}
	public void onTake() {

	}
	public void onGive() {

	}
}
