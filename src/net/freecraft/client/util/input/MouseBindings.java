package net.freecraft.client.util.input;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.state.State;
import net.freecraft.client.state.States;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.entity.Entity;
import net.freecraft.util.Vec3D;

public class MouseBindings {
	public static final MouseBinding CLICK = new MouseBinding((x, y) -> {
		State.get().mouseClick(x, y);
	}, 0, MouseState.CLICK);
	public static final MouseBinding PLACE = new MouseBinding((x, y) -> {
		if(State.get() == States.PLAY) {
			States.PLAY.rightClick();
		}
	}, 2, MouseState.CLICK);
	public static final MouseBinding BREAK = new MouseBinding((x, y) -> {
		if(State.get() == States.PLAY) {
			States.PLAY.leftClick();
		}
	}, 0, MouseState.CLICK);
	public static final MouseBinding MOVE = new MouseBinding((x, y) -> {
		if(State.get() == States.PLAY) {
			Entity entity = FreeCraftClient.get().getRenderer().getViewEntity();
			if(entity != null) {
				Vec3D rot = entity.getRot();
				double rx = -y*0.005D, ry = x*0.005D;
				rot = new Vec3D(rot.getX() + rx, rot.getY() + ry, 0);
				rot = new Vec3D(Math.min(Math.max(rot.getX(), Math.toRadians(-89.5)), Math.toRadians(89.5)), rot.getY(), 0);
				entity.setRot(rot);
			}
		}
	}, 0, MouseState.MOVE);
	public static void init() {
		ClientRegistries.MOUSE_BINDINGS.register(0, CLICK);
		ClientRegistries.MOUSE_BINDINGS.register(1, MOVE);
		ClientRegistries.MOUSE_BINDINGS.register(2, BREAK);
		ClientRegistries.MOUSE_BINDINGS.register(3, PLACE);
	}
}
