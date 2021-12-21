package net.freecraft.entity;

import net.freecraft.util.Registries;
import net.freecraft.util.Vec3D;

public class EntityTypes {
	public static final EntityType PLAYER = new EntityType() {
		@Override
		public Entity createNewEntity(int id, Vec3D pos, Vec3D rot) {
			return new PlayerEntity(id, pos, rot);
		}
	};
	public static void init() {
		Registries.ENTITY_TYPES.register(0, PLAYER);
	}
}
