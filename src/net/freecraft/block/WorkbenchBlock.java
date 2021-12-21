package net.freecraft.block;

import net.freecraft.FreeCraft;
import net.freecraft.client.state.State;
import net.freecraft.client.state.States;
import net.freecraft.util.BlockPos;
import net.freecraft.util.Side;

public class WorkbenchBlock extends Block{
	public WorkbenchBlock(Material material) {
		super(material);
	}
	@Override
	public boolean onActivated(BlockPos pos) {
		if(FreeCraft.get().getWorld().getSide() == Side.CLIENT) {
			State.set(States.CRAFTING);
			return true;
		}
		return false;
	}
}
