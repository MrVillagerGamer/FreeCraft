package net.freecraft.item;

import net.freecraft.block.Blocks;
import net.freecraft.util.Registries;

public class Items {
	public static final Item STICK = new Item().setTextures(53);
	public static void init() {
		Registries.ITEMS.bind(Blocks.STONE, new BlockItem(Blocks.STONE));
		Registries.ITEMS.bind(Blocks.GRASS, new BlockItem(Blocks.GRASS));
		Registries.ITEMS.bind(Blocks.DIRT, new BlockItem(Blocks.DIRT));
		Registries.ITEMS.bind(Blocks.WOOD, new BlockItem(Blocks.WOOD));
		Registries.ITEMS.bind(Blocks.LEAVES, new BlockItem(Blocks.LEAVES));
		Registries.ITEMS.bind(Blocks.PLANKS, new BlockItem(Blocks.PLANKS));
		Registries.ITEMS.bind(Blocks.WORKBENCH, new BlockItem(Blocks.WORKBENCH));
		Registries.ITEMS.bind(Blocks.SAND, new BlockItem(Blocks.SAND));
		Registries.ITEMS.bind(Blocks.PERMAGRASS, new BlockItem(Blocks.PERMAGRASS));
		Registries.ITEMS.bind(Blocks.PERMADIRT, new BlockItem(Blocks.PERMADIRT));
		Registries.ITEMS.bind(Blocks.GRAVEL, new BlockItem(Blocks.GRAVEL));
		Registries.ITEMS.bind(Blocks.FIR_LEAVES, new BlockItem(Blocks.FIR_LEAVES));
		Registries.ITEMS.bind(Blocks.FIR_PLANKS, new BlockItem(Blocks.FIR_PLANKS));
		Registries.ITEMS.bind(Blocks.FIR_WOOD, new BlockItem(Blocks.FIR_WOOD));
		Registries.ITEMS.bind(Blocks.CHEST, new BlockItem(Blocks.CHEST));
		Registries.ITEMS.register(4096, STICK);
	}
}
