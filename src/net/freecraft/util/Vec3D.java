package net.freecraft.util;

import java.io.Serializable;

public class Vec3D implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double x, y, z;
	public Vec3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vec3D() {
		this(0, 0, 0);
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public BlockPos toBlockPos() {
		return new BlockPos((int)x, (int)y, (int)z);
	}
	@Override
	public String toString() {
		return x + ", " + y + ", " + z;
	}
	public double length() {
		double sqr = x * x + y * y + z * z;
		if(sqr == 0) return 0;
		return Math.sqrt(sqr);
	}
	public Vec3D negate() {
		return new Vec3D(-x, -y, -z);
	}
	public Vec3D add(Vec3D v) {
		return new Vec3D(x + v.x, y + v.y, z + v.z);
	}
	public Vec3D add(double v) {
		return new Vec3D(x + v, y + v, z + v);
	}
	public Vec3D mul(Vec3D v) {
		return new Vec3D(x * v.x, y * v.y, z * v.z);
	}
	public Vec3D mul(double v) {
		return new Vec3D(x * v, y * v, z * v);
	}
	public static Vec3D lerp(Vec3D a, Vec3D b, double f) {
		double x = b.x * f + a.x * (1 - f);
		double y = b.y * f + a.y * (1 - f);
		double z = b.z * f + a.z * (1 - f);
		return new Vec3D(x, y, z);
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vec3D) {
			Vec3D v = (Vec3D)obj;
			return v.x == x && v.y == y && v.z == z;
		}
		return super.equals(obj);
	}
}
