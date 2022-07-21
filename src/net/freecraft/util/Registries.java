package net.freecraft.util;

import net.freecraft.block.Block;
import net.freecraft.entity.EntityType;
import net.freecraft.item.Item;
import net.freecraft.world.biome.Biome;

public class Registries {
	public static final Registry<EntityType> ENTITY_TYPES = new Registry<>();
	public static final Registry<Block> BLOCKS = new Registry<>();
	public static final Registry<Item> ITEMS = new Registry<>();
	public static final Registry<Biome> BIOMES = new Registry<>();
}
