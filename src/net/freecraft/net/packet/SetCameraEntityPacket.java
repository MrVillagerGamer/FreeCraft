package net.freecraft.net.packet;

public class SetCameraEntityPacket implements INetPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int entityId;
	public SetCameraEntityPacket(int entityId) {
		this.entityId = entityId;
	}
	public int getEntityId() {
		return entityId;
	}
}
