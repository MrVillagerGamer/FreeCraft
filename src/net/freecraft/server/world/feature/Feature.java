package net.freecraft.server.world.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.freecraft.util.BlockPos;
import net.freecraft.util.ListMode;
import net.freecraft.util.RegistryEntry;

public class Feature extends RegistryEntry{
	protected int tries;
	protected ListMode biomeListMode;
	protected List<Integer> listedBiomes;
	protected HashMap<Integer, Float> biomeWeights;
	public Feature() {
		tries = 1;
		biomeListMode = ListMode.BLACKLIST;
		listedBiomes = new ArrayList<>();
		biomeWeights = new HashMap<>();
	}
	public int getNumTries() {
		return tries;
	}
	public Feature setListedBiomes(List<Integer> listedBiomes) {
		this.listedBiomes = listedBiomes;
		return this;
	}
	public Feature addListedBiome(int biomeId, float weight) {
		listedBiomes.add(biomeId);
		biomeWeights.put(biomeId, weight);
		return this;
	}
	public Feature setBiomeListMode(ListMode mode) {
		this.biomeListMode = mode;
		return this;
	}
	public float getBiomeWeight(int i) {
		if(biomeWeights.containsKey(i)) {
			return biomeWeights.get(i);
		}
		return 1;
	}
	public boolean canGenerateInBiome(int i) {
		if(biomeListMode == ListMode.WHITELIST) {
			return listedBiomes.contains(i);
		}
		return !listedBiomes.contains(i);
	}
	public boolean canGenerateAt(BlockPos pos) {
		return true;
	}
	public int getBlock(BlockPos localPos) {
		return 0;
	}
	public int getBlockFast(BlockPos localPos) {
		return 0;
	}
	public BlockPos getStart() {
		return new BlockPos(0, 0, 0);
	}
	public BlockPos getSize() {
		return new BlockPos(0, 0, 0);
	}
}
