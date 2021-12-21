package net.freecraft.server.world;

import java.util.Random;

import com.flowpowered.noise.module.source.Perlin;

import net.freecraft.FreeCraft;
import net.freecraft.block.Blocks;
import net.freecraft.server.FreeCraftServer;
import net.freecraft.server.util.ServerRegistries;
import net.freecraft.server.world.feature.Feature;
import net.freecraft.util.BlockPos;
import net.freecraft.util.ChunkPos;
import net.freecraft.util.Registries;
import net.freecraft.world.Chunk;
import net.freecraft.world.ChunkData;
import net.freecraft.world.biome.Biome;

public class WorldGenerator {
	public void addFeatures(ChunkPos pos) {
		Random rand = new Random();
		for(Feature feature : ServerRegistries.FEATURES.getAll()) {
			for(int i = 0; i < feature.getNumTries(); i++) {
				BlockPos bpos = new BlockPos(rand.nextInt(Chunk.SIZE), rand.nextInt(Chunk.HEIGHT), rand.nextInt(Chunk.SIZE)).fromLocal(pos);
				int biomeId = FreeCraftServer.get().getWorld().genBiome(bpos);
				if(feature.canGenerateInBiome(biomeId)) {
					float weight = feature.getBiomeWeight(biomeId);
					if(feature.canGenerateAt(bpos) && weight > Math.abs(new Random().nextFloat()%1.0f)) {
						ServerWorld world = (ServerWorld)FreeCraftServer.get().getWorld();
						world.addFeature(feature.getId(), bpos);
					}
				}
			}
		}
	}
	public void generateChunk(ChunkPos pos, ChunkData data) {
		Perlin perlin = new Perlin();
		perlin.setSeed(1337);
		perlin.setFrequency(0.0067f);
		perlin.setOctaveCount(3);
		for(int x = 0; x < Chunk.SIZE; x++) {
			int ax = x + pos.x * Chunk.SIZE;
			for(int z = 0; z < Chunk.SIZE; z++) {
				double div = 2.0D;
				int min = 60, max = 120;
				int seaLevel = (int)((max-min)/2.5f+min);
				int az = z + pos.z * Chunk.SIZE;
				for(int y = 0; y < min; y++) {
					data.setBlock(new BlockPos(x, y, z), 1);
				}
				for(int y = max; y < Chunk.HEIGHT; y++) {
					data.setBlock(new BlockPos(x, y, z), 0);
				}
				for(int y = min; y < max; y++) {
					double h = perlin.getValue(ax, y*2.0, az);
					h += perlin.getValue(ax*2.0, (y-min)*4.0, az*2.0)/2.0;
					h += perlin.getValue(ax*4.0, (y-min)*8.0, az*4.0)/4.0;
					h /= 1.75;
					h += (y - min) / ((max - min) / div);
					if(h < 1) {
						data.setBlock(new BlockPos(x, y, z), 1);
					}
					/*if(y == h) {
						data.setBlock(new BlockPos(x, y, z), 2);
					}else if(y < h) {
						data.setBlock(new BlockPos(x, y, z), 1);
					}else {
						data.setBlock(new BlockPos(x, y, z), 0);
					}*/
				}
				int dist = 0;
				int biomeId = FreeCraft.get().getWorld().genBiome(new BlockPos(pos.x * Chunk.SIZE + x, 0, pos.z * Chunk.SIZE + z));
				Biome biome = Registries.BIOMES.get(biomeId);
				for(int y = max; y >= min-1; y--) {
					if(data.getBlock(new BlockPos(x, y, z)) == 0) {
						dist = -1;
					}else {
						dist++;
					}
					if(dist >= 0) {
						data.setBlock(new BlockPos(x, y, z), biome.getLayerAccessor().get(dist));
					}
					if(dist >= 0 && y < seaLevel+1) {
						/*float weight = (y - seaLevel + 1)/2f;
						if(weight < new Random().nextFloat()) {
							data.setBlock(new BlockPos(x, y, z), 15);
						}*/
						data.setBlock(new BlockPos(x, y, z), Blocks.SAND.getId());
					}
					if(dist < 0 && y < seaLevel) {
						data.setBlock(new BlockPos(x, y, z), Blocks.WATER.getId());
					}
				}
			}
		}
	}
}
