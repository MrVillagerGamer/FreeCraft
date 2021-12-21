package net.freecraft.net;

import net.freecraft.net.packet.INetPacket;

public interface INetHandler {
	public void send(NetConnection conn, INetPacket packet);
}
