package net.freecraft.client.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.net.INetListener;
import net.freecraft.net.NetConnection;
import net.freecraft.net.packet.INetPacket;
import net.freecraft.net.packet.LogInPacket;
import net.freecraft.net.packet.NoActionPacket;

public class ClientSocketHandler implements Runnable {
	private Socket s;
	private NetConnection conn;
	public ClientSocketHandler(String addr) {
		try {
			s = new Socket(addr, 25560);
			conn = new NetConnection(0);
			FreeCraftClient.get().setConnection(conn);
			conn.getPacketQueue().enqueue(new LogInPacket(FreeCraftClient.get().getUsername(), FreeCraftClient.get().getPasswordHash()));
			Thread thread = new Thread(this);
			thread.setName("CLIENT-1");
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(FreeCraftClient.get().isNetRunning()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				INetPacket packet = (INetPacket)ois.readObject();
				INetListener listener = FreeCraftClient.get().getNetListener();
				listener.recv(conn, packet);
				packet = conn.getPacketQueue().dequeue();
				if(packet == null) packet = new NoActionPacket();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(packet);
				oos.flush();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				FreeCraftClient.get().disconnect();
			}
		}
		try {
			s.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
