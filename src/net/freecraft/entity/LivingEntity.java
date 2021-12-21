package net.freecraft.entity;

import net.freecraft.util.Vec3D;

public class LivingEntity extends Entity {
	private double speed = 1;
	public LivingEntity(int typeId, int id) {
		super(typeId, id);
	}
	public LivingEntity(int typeId, int id, Vec3D pos) {
		super(typeId, id, pos);
	}
	public LivingEntity(int typeId, int id, Vec3D pos, Vec3D rot) {
		super(typeId, id, pos, rot);
	}
	public LivingEntity(int id) {
		this(EntityTypes.PLAYER.getId(), id);
	}
	public LivingEntity(int id, Vec3D pos) {
		this(EntityTypes.PLAYER.getId(), id, pos);
	}
	public LivingEntity(int id, Vec3D pos, Vec3D rot) {
		this(EntityTypes.PLAYER.getId(), id, pos, rot);
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getSpeed() {
		return speed;
	}
	public Vec3D getVel() {
		return velocity;
	}
}
