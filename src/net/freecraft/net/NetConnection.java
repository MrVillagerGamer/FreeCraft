package net.freecraft.net;

public class NetConnection {
	private int id;
	private NetPacketQueue packetQueue;
	private int entityId;
	private boolean disconnecting;
	public NetConnection(int id) {
		this(id, -1);
	}
	public NetConnection(int id, int entityId) {
		this.id = id;
		this.packetQueue = new NetPacketQueue();
		this.entityId = entityId;
	}
	public int getId() {
		return id;
	}
	public int getEntityId() {
		return entityId;
	}
	public NetPacketQueue getPacketQueue() {
		return packetQueue;
	}
	public void signalDisconnect() {
		disconnecting = true;
	}
	public boolean shouldDisconnect() {
		return disconnecting;
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
}
