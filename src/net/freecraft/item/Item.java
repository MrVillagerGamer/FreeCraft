package net.freecraft.item;

import net.freecraft.data.Data;
import net.freecraft.util.RegistryEntry;

public class Item extends RegistryEntry {
	private int[] textures;
	private Data defaultData;
	public Item() {
		textures = new int[6];
		defaultData = new Data(getClass().getName());
	}
	public int[] getTextures() {
		return textures;
	}
	public int getTexture(int face) {
		return textures[face];
	}
	public Item setTextures(int top, int side, int bottom) {
		textures = new int[] {side, side, top, bottom, side, side};
		return this;
	}
	public Item setTextures(int tex) {
		textures = new int[] {tex, tex, tex, tex, tex, tex};
		return this;
	}
	public boolean isBlock() {
		return false;
	}
	public Data getDefaultData() {
		return defaultData;
	}
	public Item setDefaultData(Data data) {
		this.defaultData = data;
		return this;
	}
	public Item setTextures(int[] texes) {
		this.textures = texes;
		return this;
	}
	public int getMaxStackSize() {
		return 64;
	}
}
