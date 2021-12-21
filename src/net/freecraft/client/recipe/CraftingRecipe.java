package net.freecraft.client.recipe;

import net.freecraft.item.ItemStack;
import net.freecraft.util.RegistryEntry;

public class CraftingRecipe extends RegistryEntry {
	private int[] pattern;
	private int width, height;
	private ItemStack result;
	public CraftingRecipe(int[] pattern, int width, ItemStack result) {
		this.pattern = pattern;
		this.width = width;
		this.height = pattern.length / width;
		this.result = result;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int[] getPattern() {
		return pattern;
	}
	public ItemStack getResult() {
		return result;
	}
}
