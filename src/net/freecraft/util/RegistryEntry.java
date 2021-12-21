package net.freecraft.util;

public class RegistryEntry implements IRegistryEntry {
	protected int id;
	@Override
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int getId() {
		return id;
	}
}
