package net.freecraft.server.world.feature;

import net.freecraft.block.Blocks;
import net.freecraft.server.util.ServerRegistries;
import net.freecraft.util.ListMode;

public class Features {
	public static final Feature OAK_TREE_FEATURE = new TreeFeature(TreeShape.DECIDUOUS, Blocks.WOOD.getId(), Blocks.LEAVES.getId()).setBiomeListMode(ListMode.WHITELIST).addListedBiome(1, 0.1f).addListedBiome(2, 0.4f);
	//public static final Feature OAK_TREE_FEATURE = new TreeFeature(TreeShape.DECIDUOUS, Blocks.WOOD.getId(), Blocks.LEAVES.getId()).setBiomeListMode(ListMode.WHITELIST).addListedBiome(2, 0.1f).addListedBiome(3, 1.0f);
	public static final Feature FIR_TREE_FEATURE = new TreeFeature(TreeShape.CONIFEROUS, Blocks.FIR_WOOD.getId(), Blocks.FIR_LEAVES.getId()).setBiomeListMode(ListMode.WHITELIST).addListedBiome(4, 0.4f);
	public static void init() {
		ServerRegistries.FEATURES.clear();
		ServerRegistries.FEATURES.register(0, OAK_TREE_FEATURE);
	}
}
