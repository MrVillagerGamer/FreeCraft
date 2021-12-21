package net.freecraft.client.util;

import java.awt.image.BufferedImage;

import net.freecraft.item.Item;
import net.freecraft.util.RegistryEntry;

public class ItemIcon extends RegistryEntry {
	private BufferedImage img;
	public ItemIcon(Item item) {
		Atlas atlas = item.isBlock() ? Atlases.BLOCKS : Atlases.ITEMS;
		BufferedImage atlas1 = atlas.getAWTImage();
		int tx = item.getTexture(0) % atlas.getSizeInTiles();
		int ty = item.getTexture(0) / atlas.getSizeInTiles();
		int scale = atlas.getSizeOfTile();
		img = atlas1.getSubimage(tx * scale, ty * scale, scale, scale);
	}
	public BufferedImage getAWTImage() {
		return img;
	}
}
