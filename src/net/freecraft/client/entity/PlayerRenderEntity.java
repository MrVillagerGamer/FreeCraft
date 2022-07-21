package net.freecraft.client.entity;

import net.freecraft.entity.PlayerEntity;
import net.freecraft.util.Vec3D;

public class PlayerRenderEntity extends PlayerEntity implements IRenderEntity {
	public PlayerRenderEntity(int id, Vec3D pos, Vec3D rot) {
		super(id, pos, rot);
	}
	@Override
	public void dispose() {

	}
	@Override
	public void render() {

	}
}
