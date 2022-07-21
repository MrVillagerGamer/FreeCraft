package net.freecraft.block;

import net.freecraft.util.Registries;
import net.freecraft.util.Vec3D;

public class Blocks {
	public static final Block AIR = new Block(Material.AIR).setFogColor(new Vec3D(0.7f, 0.85f, 1.0f));
	public static final Block STONE = new Block(Material.ROCK).setTextures(0);
	public static final Block GRASS = new Block(Material.GROUND).setTextures(1, 2, 3).setPlantFaces(true, false, false);
	public static final Block DIRT = new Block(Material.GROUND).setTextures(3);
	public static final Block WOOD = new Block(Material.WOOD).setTextures(11, 5, 11);
	public static final Block LEAVES = new Block(Material.WOOD).setTextures(6).setPlantFaces(true).setTransparent(true);
	public static final Block PLANKS = new Block(Material.WOOD).setTextures(8);
	public static final Block SAND = new Block(Material.POWDER).setTextures(4);
	public static final Block WORKBENCH = new WorkbenchBlock(Material.WOOD).setTextures(10, 9, 8);
	public static final Block WATER = new Block(Material.FLUID).setTextures(7).setTransparent(true).setFogColor(new Vec3D(0.0f, 0.2f, 0.35f));
	public static final Block PERMAGRASS = new Block(Material.GROUND).setTextures(16, 17, 18).setPlantFaces(true, false, false);
	public static final Block PERMADIRT = new Block(Material.GROUND).setTextures(18);
	public static final Block FIR_WOOD = new Block(Material.WOOD).setTextures(24, 19, 24);
	public static final Block FIR_LEAVES = new Block(Material.WOOD).setTextures(20).setPlantFaces(true).setTransparent(true);
	public static final Block FIR_PLANKS = new Block(Material.WOOD).setTextures(21);
	public static final Block GRAVEL = new Block(Material.POWDER).setTextures(13);
	public static final Block CHEST = new ChestBlock(Material.WOOD).setTextures(10, 9, 8);
	public static void init() {
		Registries.BLOCKS.register(0, AIR);
		Registries.BLOCKS.register(1, STONE);
		Registries.BLOCKS.register(2, GRASS);
		Registries.BLOCKS.register(3, DIRT);
		Registries.BLOCKS.register(4, WOOD);
		Registries.BLOCKS.register(5, LEAVES);
		Registries.BLOCKS.register(6, PLANKS);
		Registries.BLOCKS.register(7, SAND);
		Registries.BLOCKS.register(8, WORKBENCH);
		Registries.BLOCKS.register(9, WATER);
		Registries.BLOCKS.register(15, GRAVEL);
		Registries.BLOCKS.register(16, CHEST);
	}
}
