package net.freecraft.util;

import java.io.Serializable;

public class Vec2D implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private double x, y;
	public Vec2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
}
