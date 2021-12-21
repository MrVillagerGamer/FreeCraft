package net.freecraft.net.packet;

import net.freecraft.data.Data;

public class SetEntityDataPacket implements INetPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int entityId;
	private Data data;
	public SetEntityDataPacket(int entityId, Data data) {
		this.entityId = entityId;
		this.data = data;
	}
	public int getId() {
		return entityId;
	}
	public Data getData() {
		return data;
	}
}
