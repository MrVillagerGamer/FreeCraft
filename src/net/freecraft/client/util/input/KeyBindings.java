package net.freecraft.client.util.input;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.state.State;
import net.freecraft.client.state.States;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.entity.Entity;
import net.freecraft.entity.LivingEntity;
import net.freecraft.util.Vec3D;

public class KeyBindings {
	public static final KeyBinding ESCAPE = new KeyBinding(() -> {
		State.get().escape();
	}, Key.ESC, KeyState.DOWN);
	public static final KeyBinding E_DOWN = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			State.set(States.INVENTORY);
		}
	}, Key.E, KeyState.DOWN);
	public static final KeyBinding W_DOWN = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVelXZ(player.getForwardXZ().mul(player.getSpeed()));
			}
		}
	}, Key.W, KeyState.HOLD);
	public static final KeyBinding S_DOWN = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVelXZ(player.getForwardXZ().negate().mul(player.getSpeed()));
			}
		}
	}, Key.S, KeyState.HOLD);
	public static final KeyBinding D_DOWN = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVelXZ(player.getRight().mul(player.getSpeed()));
			}
		}
	}, Key.D, KeyState.HOLD);
	public static final KeyBinding A_DOWN = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVelXZ(player.getRight().negate().mul(player.getSpeed()));
			}
		}
	}, Key.A, KeyState.HOLD);
	public static final KeyBinding W_UP = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVelXZ(new Vec3D());
			}
		}
	}, Key.W, KeyState.UP);
	public static final KeyBinding S_UP = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVelXZ(new Vec3D());
			}
		}
	}, Key.S, KeyState.UP);
	public static final KeyBinding D_UP = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVelXZ(new Vec3D());
			}
		}
	}, Key.D, KeyState.UP);
	public static final KeyBinding A_UP = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVelXZ(new Vec3D());
			}
		}
	}, Key.A, KeyState.UP);
	public static final KeyBinding SPACE_DOWN = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity instanceof LivingEntity) {
				LivingEntity player = (LivingEntity)entity;
				player.setVel(new Vec3D(player.getVel().getX(), 8, player.getVel().getZ()));
			}
		}
	}, Key.SPACE, KeyState.DOWN);
	public static final KeyBinding SHIFT_DOWN = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			States.PLAY.shift(true);
		}
	}, Key.SHIFT, KeyState.DOWN);
	public static final KeyBinding SHIFT_UP = new KeyBinding(() -> {
		if(State.get() == States.PLAY) {
			States.PLAY.shift(false);
		}
	}, Key.SHIFT, KeyState.UP);

	public static void init() {
		ClientRegistries.KEY_BINDINGS.register(0, ESCAPE);
		ClientRegistries.KEY_BINDINGS.register(1, W_DOWN);
		ClientRegistries.KEY_BINDINGS.register(2, S_DOWN);
		ClientRegistries.KEY_BINDINGS.register(3, D_DOWN);
		ClientRegistries.KEY_BINDINGS.register(4, A_DOWN);
		ClientRegistries.KEY_BINDINGS.register(5, W_UP);
		ClientRegistries.KEY_BINDINGS.register(6, S_UP);
		ClientRegistries.KEY_BINDINGS.register(7, D_UP);
		ClientRegistries.KEY_BINDINGS.register(8, A_UP);
		ClientRegistries.KEY_BINDINGS.register(9, E_DOWN);
		ClientRegistries.KEY_BINDINGS.register(10, SPACE_DOWN);
		ClientRegistries.KEY_BINDINGS.register(11, SHIFT_DOWN);
		ClientRegistries.KEY_BINDINGS.register(12, SHIFT_UP);
	}
}
