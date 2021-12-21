package net.freecraft.data;

import java.io.Serializable;
import java.util.Arrays;

import net.freecraft.item.inventory.Inventory;

// Data for Version 1: DO NOT ADD OR REARRANGE INSTANCE VARIABLES
public class Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Data DEFAULT = new Data("");
	private String name;
	private EnchantData[] enchants;
	private int damage;
	private int maxDamage;
	private Inventory inventory;
	private String passwordHash;
	public Data(String name) {
		this.name = name;
		this.enchants = new EnchantData[0];
		this.damage = 0;
		this.maxDamage = 1;
		this.inventory = null;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setEnchants(EnchantData[] enchants) {
		this.enchants = enchants;
	}
	public EnchantData[] getEnchants() {
		return enchants;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public int getDamage() {
		return damage;
	}
	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}
	public int getMaxDamage() {
		return maxDamage;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Data) {
			Data o = (Data)obj;
			return o.maxDamage == maxDamage && o.damage == damage && Arrays.equals(enchants, o.enchants) && o.name.equals(name);
		}
		return super.equals(obj);
	}
	public Data setInventory(Inventory inventory) {
		this.inventory = inventory;
		return this;
	}
	public Inventory getInventory() {
		return inventory;
	}
	public Data copy() {
		Data data = new Data(name + "");
		data.setEnchants(getEnchants().clone());
		data.setDamage(getDamage());
		data.setMaxDamage(getMaxDamage());
		if(getInventory() != null) {
			data.setInventory(getInventory().copy());
		}else {
			data.setInventory(null);
		}
		return data;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
}
