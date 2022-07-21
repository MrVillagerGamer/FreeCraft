package net.freecraft.net.packet;

public class LogInPacket implements INetPacket {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String passwordHash;
	private String username;
	public LogInPacket(String username, String passwordHash) {
		this.username = username;
		this.passwordHash = passwordHash;
	}
	public String getUsername() {
		return username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
}
