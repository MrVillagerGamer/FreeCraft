package net.freecraft.item.inventory;

import java.io.Serializable;

import net.freecraft.data.Data;
import net.freecraft.item.ItemSlot;
import net.freecraft.item.ItemStack;
import net.freecraft.util.Registries;

public class Inventory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ItemSlot[] slots;
	public Inventory(int size) {
		slots = new ItemSlot[size];
		for(int i = 0; i < size; i++) {
			slots[i] = new ItemSlot();
		}
	}
	public Inventory(ItemSlot[] slots) {
		this.slots = slots;
	}
	public boolean onChange(int slot, ItemStack newStack) {
		return true;
	}
	public int size() {
		return slots.length;
	}
	public void set(int slot, ItemStack stack) {
		if(onChange(slot, stack)) {
			slots[slot].set(stack.copy());
		}
	}
	public ItemStack get(int slot) {
		return slots[slot].get().copy();
	}
	private boolean add(int id, Data data) {
		for(int i = 0; i < size(); i++) {
			ItemStack stack = get(i);
			if(stack.getId() == id && stack.getData().equals(data)) {
				int maxStackSize = Registries.ITEMS.get(stack.getId()).getMaxStackSize();
				if(stack.getCount() < maxStackSize) {
					stack.setCount(stack.getCount() + 1);
					set(i, stack);
					return true;
				}
			}
		}
		for(int i = 0; i < size(); i++) {
			ItemStack stack = get(i);
			if(stack.isEmpty()) {
				set(i, new ItemStack(id, 1, data));
				return true;
			}
		}
		return false;
	}
	public int add(ItemStack stack) {
		for(int i = 0; i < stack.getCount(); i++) {
			if(!add(stack.getId(), stack.getData())) {
				return i;
			}
		}
		return stack.getCount();
	}
	public Inventory copy() {
		return new Inventory(slots);
	}
	public ItemSlot getSlot(int i) {
		return slots[i];
	}
}
