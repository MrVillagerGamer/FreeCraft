package net.freecraft.net.packet;

import net.freecraft.util.BlockPos;

public class SetBlockPacket implements INetPacket {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int blockId;
	private BlockPos pos;
	public SetBlockPacket(BlockPos pos, int blockId) {
		this.pos = pos;
		this.blockId = blockId;
	}
	public SetBlockPacket() {
		this(new BlockPos(0, 0, 0), 0);
	}
	public int getBlockId() {
		return blockId;
	}
	public BlockPos getPos() {
		return pos;
	}
}
