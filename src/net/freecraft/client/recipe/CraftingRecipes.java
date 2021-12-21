package net.freecraft.client.recipe;

import net.freecraft.block.Blocks;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.item.ItemStack;
import net.freecraft.item.Items;

public class CraftingRecipes {
	public static final CraftingRecipe PLANKS = new CraftingRecipe(new int[] {
		Blocks.WOOD.getId()
	}, 1, new ItemStack(Blocks.PLANKS.getId(), 4));
	public static final CraftingRecipe STICK = new CraftingRecipe(new int[] {
		Blocks.PLANKS.getId(),
		Blocks.PLANKS.getId()
	}, 1, new ItemStack(Items.STICK.getId(), 4));
	public static final CraftingRecipe WORKBENCH = new CraftingRecipe(new int[] {
		Blocks.PLANKS.getId(),
		Blocks.PLANKS.getId(),
		Blocks.PLANKS.getId(),
		Blocks.PLANKS.getId()
	}, 2, new ItemStack(Blocks.WORKBENCH.getId(), 1));
	public static final CraftingRecipe CHEST = new CraftingRecipe(new int[] {
		Blocks.WOOD.getId(),
		Blocks.WOOD.getId(),
		Blocks.WOOD.getId(),
		Blocks.WOOD.getId()
	}, 2, new ItemStack(Blocks.CHEST.getId(), 1));
	public static void init() {
		ClientRegistries.CRAFTING_RECIPES.register(0, PLANKS);
		ClientRegistries.CRAFTING_RECIPES.register(1, STICK);
		ClientRegistries.CRAFTING_RECIPES.register(2, WORKBENCH);
		ClientRegistries.CRAFTING_RECIPES.register(3, CHEST);
		
	}
}
