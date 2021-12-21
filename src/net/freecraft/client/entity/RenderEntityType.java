package net.freecraft.client.entity;

import net.freecraft.util.RegistryEntry;
import net.freecraft.util.Vec3D;

public abstract class RenderEntityType extends RegistryEntry {
	public abstract IRenderEntity createNewRenderEntity(int id, Vec3D pos, Vec3D rot);
	
}
