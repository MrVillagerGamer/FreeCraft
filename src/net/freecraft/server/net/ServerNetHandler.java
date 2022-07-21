package net.freecraft.server.net;

import net.freecraft.net.INetHandler;
import net.freecraft.net.NetConnection;
import net.freecraft.net.packet.INetPacket;
import net.freecraft.server.FreeCraftServer;

public class ServerNetHandler implements INetHandler {
	@Override
	public void send(NetConnection info, INetPacket packet) {
		for(NetConnection conn : FreeCraftServer.get().getConnections()) {
			if(info == null || conn.getId() != info.getId()) {
				conn.getPacketQueue().enqueue(packet);
			}
		}
	}
}
