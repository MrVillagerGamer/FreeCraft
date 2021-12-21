package net.freecraft.client.util;

public class Atlases {
	public static final Atlas BLOCKS = new Atlas("/terrain.png");
	public static final Atlas ITEMS = new Atlas("/items.png");
	public static final Atlas CLOUDS = new Atlas("/clouds.png");
	public static void init() {
		ClientRegistries.ATLASES.register(0, BLOCKS);
		ClientRegistries.ATLASES.register(1, ITEMS);
		ClientRegistries.ATLASES.register(2, CLOUDS);
	}
}
