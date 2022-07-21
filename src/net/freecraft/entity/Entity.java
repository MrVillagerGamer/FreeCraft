package net.freecraft.entity;

import net.freecraft.FreeCraft;
import net.freecraft.data.Data;
import net.freecraft.net.NetConnection;
import net.freecraft.net.packet.SetEntityDataPacket;
import net.freecraft.net.packet.SetEntityPosPacket;
import net.freecraft.net.packet.SetEntityRotPacket;
import net.freecraft.util.Registries;
import net.freecraft.util.Side;
import net.freecraft.util.Vec3D;
import net.freecraft.world.World;

public class Entity {
	protected boolean moved;
	protected boolean turned;
	private Vec3D pos;
	private Vec3D rot;
	private Vec3D lastPos;
	private Vec3D lastRot;
	private Vec3D lerpPos;
	private Vec3D lerpRot;
	private int typeId;
	protected int id;
	protected Vec3D velocity;
	protected Vec3D rotVelocity;
	protected Vec3D nextTickVel;
	protected Vec3D nextTickRotVel;
	protected Data data;
	private double timeSinceGrounded = 0;
	public Entity(int typeId, int id) {
		this(typeId, id, new Vec3D());
	}
	public Entity(int typeId, int id, Vec3D pos) {
		this(typeId, id, pos, new Vec3D());
	}
	public Entity(int typeId, int id, Vec3D pos, Vec3D rot) {
		this.pos = pos;
		this.rot = rot;
		this.id = id;
		this.lastPos = pos;
		this.lastRot = rot;
		this.lerpPos = pos;
		this.lerpRot = rot;
		this.typeId = typeId;
		velocity = new Vec3D();
		rotVelocity = new Vec3D();
		nextTickVel = new Vec3D();
		nextTickRotVel = new Vec3D();
		data = new Data(getClass().getName());
	}
	public Vec3D getPos() {
		return lerpPos;
	}
	public void setPosLocally(Vec3D pos) {
		this.pos = pos;
	}
	public void setPos(Vec3D pos) {
		this.pos = pos;
		moved = true;
	}
	public Vec3D getRot() {
		return lerpRot;
	}
	public void setRotLocally(Vec3D rot) {
		this.rot = rot;
	}
	public void setRot(Vec3D rot) {
		this.rot = rot;
		turned = true;
	}
	public int getTypeId() {
		return typeId;
	}
	public Vec3D getRight(Vec3D rot) {
		double x = Math.cos(rot.getY());
		double z = Math.sin(rot.getY());
		return new Vec3D(x, 0, z);
	}
	public Vec3D getForward(Vec3D rot) {
		double horizontal = Math.cos(rot.getX());
		double x = Math.sin(rot.getY()) * horizontal;
		double z = -Math.cos(rot.getY()) * horizontal;
		double y = Math.sin(rot.getX());
		return new Vec3D(x, y, z);
	}
	public Vec3D getForward() {
		return getForward(lerpRot);
	}
	public Vec3D getRight() {
		return getRight(lerpRot);
	}
	public void partialTick(double partialTick) {
		lerpPos = Vec3D.lerp(lastPos, pos, partialTick * 1.0D);
		lerpRot = Vec3D.lerp(lastRot, rot, partialTick * 1.0D);
	}
	public void tick() {
		lastPos = pos;
		lastRot = rot;
		timeSinceGrounded+=1.0D/World.TICK_RATE;
		Vec3D fallDir = checkCollide(new Vec3D(0, (World.GRAVITY * timeSinceGrounded + velocity.getY()) * 1.0D / World.TICK_RATE, 0), new Vec3D(pos.getX(), pos.getY(), pos.getZ()));
		if(fallDir.getY() == 0) {
			timeSinceGrounded = 0;
			nextTickVel = new Vec3D(nextTickVel.getX(), 0, nextTickVel.getZ());
		}
		Vec3D pos = new Vec3D(this.pos.getX(), this.pos.getY(), this.pos.getZ());
		if(fallDir.getY() != 0) {
			pos = pos.add(fallDir);
		}
		if(velocity.getX() != 0 || velocity.getZ() != 0) {
			Vec3D resultDir = checkCollide(new Vec3D(velocity.getX(), 0, velocity.getZ()).mul(1.0D/World.TICK_RATE), new Vec3D(pos.getX(), pos.getY(), pos.getZ()));
			pos = pos.add(resultDir);
		}
		if(pos.getX() != lastPos.getX() || pos.getZ() != lastPos.getZ() || pos.getY() != lastPos.getY()) {
			setPos(pos);
		}
		if(rotVelocity.length() != 0) {
			setRot(getRot().add(rotVelocity.mul(1.0D/World.TICK_RATE)));
		}
		velocity = nextTickVel;
		rotVelocity = nextTickRotVel;
		World world = FreeCraft.get().getWorld();
		if(world.getTime() % 1 == 0 && world.getSide() == Side.CLIENT) {
			if(moved) {
				SetEntityPosPacket packet = new SetEntityPosPacket(id, getPos());
				FreeCraft.get().getNetHandler().send(null, packet);
				moved = false;
			}
			if(turned) {
				SetEntityRotPacket packet = new SetEntityRotPacket(id, getRot());
				FreeCraft.get().getNetHandler().send(null, packet);
				turned = false;
			}
		}
	}
	/*
	private Vec3D checkCollidePrecise(Vec3D dir, Vec3D pos1, int steps) {
		Vec3D resultDir = checkCollide(dir.mul(1.0D/Math.pow(2, steps)), pos1);
		if(resultDir.equals(dir)) {
			return dir;
		}else {
			if(steps >= 1) {
				return checkCollidePrecise(dir, pos1, steps-1);
			}else {
				return resultDir;
			}
		}
	}
	*/
	private Vec3D checkCollide(Vec3D dir, Vec3D pos1) {
		Vec3D p = new Vec3D(pos1.getX(), pos1.getY(), pos1.getZ());
		Vec3D min = new Vec3D(-0.1D, -1.6D, -0.1D);
		Vec3D max = new Vec3D(0.1D, 0.2D, 0.1D);
		dir = new Vec3D(dir.getX(), dir.getY(), dir.getZ());
		Vec3D resultDir = new Vec3D(dir.getX(), dir.getY(), dir.getZ());
		Vec3D step = new Vec3D(0.1D, 0.1D, 0.1D);
		for(double x = min.getX(); x <= max.getX(); x += step.getX()) {
			for(double y = min.getY(); y <= max.getY(); y += step.getY()) {
				Vec3D pos = new Vec3D(x+p.getX(), y+p.getY(), p.getZ()+min.getZ()+dir.getZ());
				if(!Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(pos.toBlockPos())).isPassable()) {
					resultDir = new Vec3D(resultDir.getX(), resultDir.getY(), 0);
				}
				pos = new Vec3D(x+p.getX(), y+p.getY(), p.getZ()+max.getZ()+dir.getZ());
				if(!Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(pos.toBlockPos())).isPassable()) {
					resultDir = new Vec3D(resultDir.getX(), resultDir.getY(), 0);
				}
			}
		}
		// Check on top and bottom faces of the bounding box
		for(double x = min.getX(); x <= max.getX(); x += step.getX()) {
			for(double z = min.getZ(); z <= max.getZ(); z += step.getZ()) {
				Vec3D pos = new Vec3D(x+p.getX(), p.getY()+min.getY()+dir.getY(), z+p.getZ());
				if(!Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(pos.toBlockPos())).isPassable()) {
					resultDir = new Vec3D(resultDir.getX(), 0, resultDir.getZ());
				}
				pos = new Vec3D(x+p.getX(), p.getY()+max.getY()+dir.getY(), z+p.getZ());
				if(!Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(pos.toBlockPos())).isPassable()) {
					resultDir = new Vec3D(resultDir.getX(), 0, resultDir.getZ());
				}
			}
		}
		// Check on left and right faces of the bounding box
		for(double y = min.getY(); y <= max.getY(); y += step.getY()) {
			for(double z = min.getZ(); z <= max.getZ(); z += step.getZ()) {
				Vec3D pos = new Vec3D(p.getX()+min.getX()+dir.getX(), y+p.getY(), z+p.getZ());
				if(!Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(pos.toBlockPos())).isPassable()) {
					resultDir = new Vec3D(0, resultDir.getY(), resultDir.getZ());
				}
				pos = new Vec3D(p.getX()+max.getX()+dir.getX(), y+p.getY(), z+p.getZ());
				if(!Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(pos.toBlockPos())).isPassable()) {
					resultDir = new Vec3D(0, resultDir.getY(), resultDir.getZ());
				}
			}
		}
		return resultDir;
	}
	public void setVel(Vec3D velocity) {
		this.velocity = velocity;
		setNextTickVel(velocity);
	}
	public void setVelXZ(Vec3D velocity) {
		this.velocity = new Vec3D(velocity.getX(), this.velocity.getY(), velocity.getZ());
		setNextTickVel(this.velocity);
	}
	public void setRotVel(Vec3D rotVelocity) {
		this.rotVelocity = rotVelocity;
		setNextTickRotVel(rotVelocity);
	}
	public Vec3D getRotVel() {
		return rotVelocity;
	}
	public void setNextTickVel(Vec3D nextTickVel) {
		this.nextTickVel = nextTickVel;
	}
	public void setNextTickRotVel(Vec3D nextTickRotVel) {
		this.nextTickRotVel = nextTickRotVel;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public Data getData() {
		return data;
	}
	public void setDataLocally(Data data) {
		this.data = data;
	}
	public void syncData(NetConnection conn) {
		FreeCraft.get().getNetHandler().send(conn, new SetEntityDataPacket(id, data));
	}
	public Vec3D getForwardXZ() {
		Vec3D v = getForward();
		v = new Vec3D(v.getX(), 0, v.getZ());
		v = new Vec3D(v.getX() / v.length(), 0, v.getZ() / v.length());

		return v;
	}
}
