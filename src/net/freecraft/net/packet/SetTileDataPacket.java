package net.freecraft.net.packet;

import net.freecraft.data.Data;
import net.freecraft.util.BlockPos;

public class SetTileDataPacket implements INetPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BlockPos pos;
	private Data data;
	public SetTileDataPacket(BlockPos pos, Data data) {
		this.pos = pos;
		this.data = data;
	}
	public BlockPos getPos() {
		return pos;
	}
	public Data getData() {
		return data;
	}
}
