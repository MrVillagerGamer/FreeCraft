package net.freecraft.world.biome;

import net.freecraft.block.Blocks;
import net.freecraft.util.LayerAccessor;
import net.freecraft.util.RegistryEntry;
import net.freecraft.util.Vec3D;

public class Biome extends RegistryEntry {
	private Vec3D plantColor, skyColor, waterColor;
	private LayerAccessor layerAccessor;
	public Biome() {
		this.plantColor = new Vec3D(0.0D, 0.67D, 0.0D);
		this.skyColor = new Vec3D(0.4D, 0.7D, 1.0D);
		this.waterColor = new Vec3D(0.0D, 0.0D, 1.0D);
		this.layerAccessor = this::defaultLayerAccessor;
	}
	public int defaultLayerAccessor(int layerDepth) {
		if(layerDepth == 0) return Blocks.GRASS.getId();
		if(layerDepth <= 2) return Blocks.DIRT.getId();
		return Blocks.STONE.getId();
	}
	public LayerAccessor getLayerAccessor() {
		return layerAccessor;
	}
	public Biome setLayerAccessor(LayerAccessor layerAccessor) {
		this.layerAccessor = layerAccessor;
		return this;
	}
	public Biome setPlantColor(Vec3D plantColor) {
		this.plantColor = plantColor;
		return this;
	}
	public Biome setSkyColor(Vec3D skyColor) {
		this.skyColor = skyColor;
		return this;
	}
	public Biome setWaterColor(Vec3D waterColor) {
		this.waterColor = waterColor;
		return this;
	}
	public Vec3D getPlantColor() {
		return plantColor;
	}
	public Vec3D getSkyColor() {
		return skyColor;
	}
	public Vec3D getWaterColor() {
		return waterColor;
	}
}
