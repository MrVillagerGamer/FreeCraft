package net.freecraft.net;

import net.freecraft.net.packet.INetPacket;

public interface INetListener {
	public boolean recv(NetConnection conn, INetPacket packet);
}
