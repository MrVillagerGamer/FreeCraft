package net.freecraft.net.packet;

import net.freecraft.data.Data;
import net.freecraft.util.Vec3D;

public class SpawnEntityPacket implements INetPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int entityId, typeId;
	private Vec3D pos, rot;
	private Data data;
	public SpawnEntityPacket(int typeId, int entityId, Vec3D pos, Vec3D rot, Data data) {
		this.entityId = entityId;
		this.typeId = typeId;
		this.pos = pos;
		this.rot = rot;
		this.data = data;
	}
	public int getId() {
		return entityId;
	}
	public int getTypeId() {
		return typeId;
	}
	public Vec3D getPos() {
		return pos;
	}
	public Vec3D getRot() {
		return rot;
	}
	public Data getData() {
		return data;
	}
}
