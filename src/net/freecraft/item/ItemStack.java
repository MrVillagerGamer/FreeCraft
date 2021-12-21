package net.freecraft.item;

import java.io.Serializable;

import net.freecraft.data.Data;
import net.freecraft.util.Registries;

public class ItemStack implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int count;
	private Data data;
	public ItemStack(int id, int count) {
		this.id = id;
		this.count = count;
		Item item = Registries.ITEMS.get(id);
		if(item != null) {
			this.data = item.getDefaultData();
		}else {
			this.data = Data.DEFAULT;
		}
	}
	public ItemStack(int id, int count, Data data) {
		this.id = id;
		this.count = count;
		this.data = data;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
		if(count == 0) {
			id = 0;
			data = new Data("");
		}
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public boolean isEmpty() {
		return id == 0 && count == 0;
	}
	public static ItemStack createEmpty() {
		return new ItemStack(0, 0);
	}
	public void set(ItemStack stack) {
		setId(stack.getId());
		setData(stack.getData().copy());
		setCount(stack.getCount());
	}
	public boolean canCombineWith(ItemStack stack) {
		return id == stack.getId() && data.equals(stack.getData()) || (isEmpty() || stack.isEmpty());
	}
	public ItemStack copy() {
		return new ItemStack(id, count, data);
	}
	public int getMaxCount() {
		return Registries.ITEMS.get(id).getMaxStackSize();
	}
}
