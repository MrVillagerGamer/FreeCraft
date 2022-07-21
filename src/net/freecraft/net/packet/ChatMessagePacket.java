package net.freecraft.net.packet;

public class ChatMessagePacket implements INetPacket {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String name, message;
	public ChatMessagePacket(String message) {
		this(message, "unknown");
	}
	public ChatMessagePacket(String message, String name) {
		this.message = message;
		this.name = "unknown";
	}
	public String getMessage() {
		return message;
	}
	public String getName() {
		return name;
	}
}
