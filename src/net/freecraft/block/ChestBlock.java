package net.freecraft.block;

import net.freecraft.FreeCraft;
import net.freecraft.client.state.State;
import net.freecraft.client.state.States;
import net.freecraft.data.Data;
import net.freecraft.item.inventory.ChestInventory;
import net.freecraft.util.BlockPos;
import net.freecraft.util.Side;

public class ChestBlock extends Block {
	public ChestBlock(Material material) {
		super(material);
	}
	@Override
	public boolean hasTileData(BlockPos pos) {
		return true;
	}
	@Override
	public Data genTileData(BlockPos pos, Data src) {
		// TODO: Check for valid ItemStack tile data
		return new Data("Chest").setInventory(new ChestInventory(27));
	}
	@Override
	public boolean onActivated(BlockPos pos) {
		Data data = FreeCraft.get().getWorld().getTileData(pos);
		if(FreeCraft.get().getWorld().getSide() == Side.CLIENT) {
			States.CHEST.set(pos, data);
			State.set(States.CHEST);
			return true;
		}
		return false;
	}
}
