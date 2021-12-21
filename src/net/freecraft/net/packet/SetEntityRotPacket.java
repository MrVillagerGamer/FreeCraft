package net.freecraft.net.packet;

import net.freecraft.util.Vec3D;

public class SetEntityRotPacket implements INetPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int entityId;
	private Vec3D rot;
	public SetEntityRotPacket(int entityId, Vec3D rot) {
		this.entityId = entityId;
		this.rot = rot;
	}
	public int getId() {
		return entityId;
	}
	public Vec3D getRot() {
		return rot;
	}
}
