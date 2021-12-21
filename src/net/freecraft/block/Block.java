package net.freecraft.block;

import net.freecraft.FreeCraft;
import net.freecraft.data.Data;
import net.freecraft.item.ItemStack;
import net.freecraft.util.BlockPos;
import net.freecraft.util.Registries;
import net.freecraft.util.RegistryEntry;
import net.freecraft.util.Vec3D;

public class Block extends RegistryEntry {
	private Material material;
	private int[] textures;
	private boolean transparent;
	private boolean[] plants;
	private Vec3D fogColor;
	public Block(Material material) {
		this.material = material;
		textures = new int[6];
		plants = new boolean[6];
		fogColor = new Vec3D(1, 1, 1);
	}
	public int[] getTextures() {
		return textures;
	}
	public int getTexture(int face) {
		return textures[face];
	}
	public Material getMaterial() {
		return material;
	}
	public boolean isVisible() {
		return material != Material.AIR;
	}
	public Block setTextures(int top, int side, int bottom) {
		textures = new int[] {side, side, top, bottom, side, side};
		return this;
	}
	public Block setTextures(int tex) {
		textures = new int[] {tex, tex, tex, tex, tex, tex};
		return this;
	}
	public ItemStack getDrop(BlockPos pos) {
		return new ItemStack(getId(), 1);
	}
	public boolean isTransparent() {
		return transparent;
	}
	public Block setTransparent(boolean transparent) {
		this.transparent = transparent;
		return this;
	}
	public boolean isPassable() {
		return material == Material.AIR || material == Material.FLUID;
	}
	public boolean isFluid() {
		return material == Material.FLUID;
	}
	public Block setPlantFaces(boolean[] plants) {
		this.plants = plants;
		return this;
	}
	public Block setPlantFaces(boolean plant) {
		this.plants = new boolean[] {plant, plant, plant, plant, plant, plant};
		return this;
	}
	public Block setPlantFaces(boolean top, boolean side, boolean bottom) {
		this.plants = new boolean[] {side, side, top, bottom, side, side};
		return this;
	}
	public boolean isPlant(int f) {
		return plants[f];
	}
	// Sets the color of the fog within the block
	public Block setFogColor(Vec3D color) {
		this.fogColor = color;
		return this;
	}
	// Gets the color of the fog within the block
	public Vec3D getFogColor(Vec3D pos) {
		BlockPos pos2 = new Vec3D(pos.getX(), pos.getY()+1, pos.getZ()).toBlockPos();
		Block b = Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(pos2));
		if((pos.getY() % 1) >= 0.5f && b.getMaterial() == Material.AIR) {
			return b.getFogColor(pos2.toVec());
		}
		return fogColor;
	}
	// Gets the density of the fog within the block
	public double getFogDensity(Vec3D pos) {
		// If air is above a fluid block, the fluid block is only half high
		// So water fog shouldn't be rendered for the top half in such a case
		BlockPos pos2 = new Vec3D(pos.getX(), pos.getY()+1, pos.getZ()).toBlockPos();
		Block b = Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(pos2));
		if((pos.getY() % 1) >= 0.5f && b.getMaterial() == Material.AIR) {
			return 0;
		}
		return material == Material.FLUID ? 1 : 0;
	}
	// TODO: Call this on server too
	public boolean onActivated(BlockPos pos) {
		return false;
	}
	public boolean hasTileData(BlockPos pos) {
		return false;
	}
	public Data genTileData(BlockPos pos, Data src) {
		return null;
	}
}
