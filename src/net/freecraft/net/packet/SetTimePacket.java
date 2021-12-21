package net.freecraft.net.packet;

public class SetTimePacket implements INetPacket {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long tick;
	public SetTimePacket(long tick) {
		this.tick = tick;
	}
	public long getTime() {
		return tick;
	}
}
