package net.freecraft.world.biome;

import net.freecraft.block.Blocks;
import net.freecraft.util.Registries;
import net.freecraft.util.Vec3D;

public class Biomes {
	public static final Biome DESERT = new Biome().setPlantColor(new Vec3D(0.0, 1.0, 0.0)).setLayerAccessor((layerDepth) -> {
		if(layerDepth <= 2) return Blocks.SAND.getId();
		return Blocks.STONE.getId();
	});
	public static final Biome DEADLANDS = new Biome().setPlantColor(new Vec3D(0.0, 1.0, 0.0)).setLayerAccessor((layerDepth) -> {
		if(layerDepth <= 2) return Blocks.GRAVEL.getId();
		return Blocks.STONE.getId();
	});
	public static final Biome PLAINS = new Biome().setPlantColor(new Vec3D(0.0, 1.0, 0.0));
	public static final Biome FOREST = new Biome().setPlantColor(new Vec3D(0.0, 1.0, 0.0)).setLayerAccessor((layerDepth) -> {
		if(layerDepth == 0) return Blocks.GRASS.getId();
		if(layerDepth <= 2) return Blocks.DIRT.getId();
		return Blocks.STONE.getId();
	});
	public static final Biome FOREST_EDGE = new Biome().setPlantColor(new Vec3D(0.0, 1.0, 0.0)).setLayerAccessor((layerDepth) -> {
		if(layerDepth == 0) return Blocks.GRASS.getId();
		if(layerDepth <= 2) return Blocks.DIRT.getId();
		return Blocks.STONE.getId();
	});
	public static final Biome TAIGA = new Biome().setPlantColor(new Vec3D(1.0, 1.0, 1.0)).setLayerAccessor((layerDepth) -> {
		if(layerDepth == 0) return Blocks.PERMAGRASS.getId();
		if(layerDepth <= 2) return Blocks.PERMADIRT.getId();
		return Blocks.STONE.getId();
	});
	public static void init() {
		Registries.BIOMES.register(0, PLAINS);
		Registries.BIOMES.register(1, FOREST_EDGE);
		Registries.BIOMES.register(2, FOREST);
	}
}
