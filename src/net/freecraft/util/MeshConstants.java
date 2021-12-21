package net.freecraft.util;

public class MeshConstants {
	public static final Vec3D[] POSITIONS = new Vec3D[] {
			new Vec3D(0.0f, 0.0f, 0.0f),
			new Vec3D(1.0f, 0.0f, 0.0f),
			new Vec3D(1.0f, 1.0f, 0.0f),
			new Vec3D(0.0f, 1.0f, 0.0f),
			new Vec3D(0.0f, 0.0f, 1.0f),
			new Vec3D(1.0f, 0.0f, 1.0f),
			new Vec3D(1.0f, 1.0f, 1.0f),
			new Vec3D(0.0f, 1.0f, 1.0f),
		};
		public static final Vec3D[] NORMALS = new Vec3D[] {
			new Vec3D(0.0f, 0.0f, -1.0f),
			new Vec3D(0.0f, 0.0f, 1.0f),
			new Vec3D(0.0f, 1.0f, 0.0f),
			new Vec3D(0.0f, -1.0f, 0.0f),
			new Vec3D(-1.0f, 0.0f, 0.0f),
			new Vec3D(1.0f, 0.0f, 0.0f)
		};

		public static final int[][] INDICES = new int[][] {
	        // Back, Front, Top, Bottom, Left, Right
			// 0 1 2 2 1 3
			{0, 3, 2, 1}, // Back Face
			{5, 6, 7, 4}, // Front Face
			{3, 7, 6, 2}, // Top Face
			{1, 5, 4, 0}, // Bottom Face
			{4, 7, 3, 0}, // Left Face
			{1, 2, 6, 5} // Right Face
		};
		public static final Vec2D[] TEXCOORDS = new Vec2D[] {
			new Vec2D(0.0f, 0.0f),
			new Vec2D(0.0f, 1.0f),
			new Vec2D(1.0f, 1.0f),
			new Vec2D(1.0f, 0.0f),
		};
}
