package net.freecraft.server.util;

public class UserInfo {
	private String userName, passHash;
	public UserInfo(String userName, String passHash) {
		this.userName = userName;
		this.passHash = passHash;
	}
	public String getPassHash() {
		return passHash;
	}
	public String getUserName() {
		return userName;
	}
}
