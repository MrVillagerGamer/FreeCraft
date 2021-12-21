package net.freecraft.client.net;

import net.freecraft.net.NetConnection;
import net.freecraft.client.FreeCraftClient;
import net.freecraft.net.INetHandler;
import net.freecraft.net.packet.INetPacket;

public class ClientNetHandler implements INetHandler {
	public ClientNetHandler() {
		
	}
	@Override
	public void send(NetConnection info, INetPacket packet) {
		if(info == null) info = FreeCraftClient.get().getConnection();
		info.getPacketQueue().enqueue(packet);
	}
}
