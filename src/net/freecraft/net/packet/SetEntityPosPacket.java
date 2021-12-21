package net.freecraft.net.packet;

import net.freecraft.util.Vec3D;

public class SetEntityPosPacket implements INetPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int entityId;
	private Vec3D pos;
	public SetEntityPosPacket(int entityId, Vec3D pos) {
		this.entityId = entityId;
		this.pos = pos;
	}
	public int getId() {
		return entityId;
	}
	public Vec3D getPos() {
		return pos;
	}
}
