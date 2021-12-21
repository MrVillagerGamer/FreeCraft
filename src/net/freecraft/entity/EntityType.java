package net.freecraft.entity;

import net.freecraft.util.RegistryEntry;
import net.freecraft.util.Vec3D;

public abstract class EntityType extends RegistryEntry {
	public EntityType() {
		
	}
	public abstract Entity createNewEntity(int id, Vec3D pos, Vec3D rot);
}
