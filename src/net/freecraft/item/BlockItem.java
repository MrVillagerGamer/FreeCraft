package net.freecraft.item;

import net.freecraft.block.Block;

public class BlockItem extends Item {
	public BlockItem(Block base) {
		setTextures(base.getTextures());
	}
	@Override
	public boolean isBlock() {
		return true;
	}
}
