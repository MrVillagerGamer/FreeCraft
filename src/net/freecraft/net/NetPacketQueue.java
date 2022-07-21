package net.freecraft.net;

import java.util.ArrayList;
import java.util.List;

import net.freecraft.net.packet.INetPacket;

public class NetPacketQueue {
	private List<INetPacket> underlyingQueue = new ArrayList<>();
	public void enqueue(INetPacket packet) {
		underlyingQueue.add(packet);
	}
	public INetPacket dequeue() {
		if(underlyingQueue.size() > 0) {
			return underlyingQueue.remove(0);
		}
		return null;
	}
}
