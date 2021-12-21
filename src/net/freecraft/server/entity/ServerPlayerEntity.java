package net.freecraft.server.entity;

import net.freecraft.entity.PlayerEntity;
import net.freecraft.net.NetConnection;
import net.freecraft.util.Vec3D;

public class ServerPlayerEntity extends PlayerEntity {
	private NetConnection conn;
	public ServerPlayerEntity(NetConnection conn) {
		super(conn.getEntityId());
		this.conn = conn;
	}
	public ServerPlayerEntity(NetConnection conn, Vec3D pos) {
		super(conn.getEntityId(), pos);
		this.conn = conn;
	}
	public ServerPlayerEntity(NetConnection conn, Vec3D pos, Vec3D rot) {
		super(conn.getEntityId(), pos, rot);
		this.conn = conn;
	}
	public NetConnection getConnection() {
		return conn;
	}
}
