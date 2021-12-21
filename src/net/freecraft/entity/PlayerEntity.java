package net.freecraft.entity;

import net.freecraft.data.Data;
import net.freecraft.util.Vec3D;

public class PlayerEntity extends LivingEntity {
	public PlayerEntity(int typeId, int id) {
		super(typeId, id);
	}
	public PlayerEntity(int typeId, int id, Vec3D pos) {
		super(typeId, id, pos);
	}
	public PlayerEntity(int typeId, int id, Vec3D pos, Vec3D rot) {
		super(typeId, id, pos, rot);
	}
	public PlayerEntity(int id) {
		this(EntityTypes.PLAYER.getId(), id);
	}
	public PlayerEntity(int id, Vec3D pos) {
		this(EntityTypes.PLAYER.getId(), id, pos);
	}
	public PlayerEntity(int id, Vec3D pos, Vec3D rot) {
		this(EntityTypes.PLAYER.getId(), id, pos, rot);
		setSpeed(10);
		setData(new Data("steve").setInventory(new PlayerInventory(36)));
	}
	@Override
	public void tick() {
		super.tick();
	}
}
