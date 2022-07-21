package net.freecraft.client.recipe.inventory;

import net.freecraft.client.recipe.CraftingRecipe;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.item.ItemStack;

public class CraftingInventory3x3 extends CraftingInventory {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CraftingInventory3x3() {
		super(9);
	}
	@Override
	public ItemStack getResult() {
		for(CraftingRecipe recipe : ClientRegistries.CRAFTING_RECIPES.getAll()) {
			if(recipe.getWidth() <= 3) {
				if(recipe.getHeight() <= 3) {
					for(int ox = 0; ox <= 3-recipe.getWidth(); ox++) {
						for(int oy = 0; oy <= 3-recipe.getHeight(); oy++) {
							int[] matchPattern = new int[9];
							for(int i = 0; i < 3; i++) {
								for(int j = 0; j < 3; j++) {
									matchPattern[j*3+i] = 0;
								}
							}
							for(int i = 0; i < recipe.getWidth(); i++) {
								for(int j = 0; j < recipe.getHeight(); j++) {
									matchPattern[(j+oy)*3+i+ox] = recipe.getPattern()[j*recipe.getWidth()+i];
								}
							}
							boolean matches = true;
							for(int i = 0; i < 9; i++) {
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
		for(int i = 0; i < 9 ; i++) {
			if(get(i).getCount() > 0) {
				ItemStack stack = get(i);
				stack.setCount(stack.getCount()-1);
				set(i, stack);
			}
		}
	}
}
