package net.freecraft.data.save;

import net.freecraft.data.Data;

public class SaveDataV1 extends Data {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public SaveDataV1(String name) {
		super(name);
	}
	public SaveDataV1(Data base) {
		super(base.getName());
		setMaxDamage(base.getMaxDamage());
		setDamage(base.getDamage());
		setInventory(base.getInventory());
		setEnchants(base.getEnchants());
	}
}
