package net.freecraft.client.entity;

import net.freecraft.client.util.ClientRegistries;
import net.freecraft.entity.EntityTypes;
import net.freecraft.util.Vec3D;

public class RenderEntityTypes {
	public static final RenderEntityType PLAYER = new RenderEntityType() {
		@Override
		public IRenderEntity createNewRenderEntity(int id, Vec3D pos, Vec3D rot) {
			return new PlayerRenderEntity(id, pos, rot);
		}
	};
	public static void init() {
		ClientRegistries.RENDER_ENTITY_TYPES.bind(EntityTypes.PLAYER, PLAYER);
	}
}
