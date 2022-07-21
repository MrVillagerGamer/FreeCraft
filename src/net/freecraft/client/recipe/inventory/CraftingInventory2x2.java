package net.freecraft.client.recipe.inventory;

import net.freecraft.client.recipe.CraftingRecipe;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.item.ItemStack;

public class CraftingInventory2x2 extends CraftingInventory {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CraftingInventory2x2() {
		super(4);
	}
	@Override
	public ItemStack getResult() {
		for(CraftingRecipe recipe : ClientRegistries.CRAFTING_RECIPES.getAll()) {
			if(recipe.getWidth() <= 2) {
				if(recipe.getHeight() <= 2) {
					for(int ox = 0; ox <= 2-recipe.getWidth(); ox++) {
						for(int oy = 0; oy <= 2-recipe.getHeight(); oy++) {
							int[] matchPattern = new int[4];
							for(int i = 0; i < 2; i++) {
								for(int j = 0; j < 2; j++) {
									matchPattern[j*2+i] = 0;
								}
							}
							for(int i = 0; i < recipe.getWidth(); i++) {
								for(int j = 0; j < recipe.getHeight(); j++) {
									matchPattern[(j+oy)*2+i+ox] = recipe.getPattern()[j*recipe.getWidth()+i];
								}
							}
							boolean matches = true;
							for(int i = 0; i < 4; i++) {
								if(matchPattern[i] != get(i).getId()) {
									matches = false;
								}
							}
							if(matches) {
								return recipe.getResult();
							}
						}
					}
				}
			}
		}

		return ItemStack.createEmpty();
	}
	@Override
	public void onCraft() {
		for(int i = 0; i < 4; i++) {
			if(get(i).getCount() > 0) {
				ItemStack stack = get(i);
				stack.setCount(stack.getCount()-1);
				set(i, stack);
			}
		}
	}
}
