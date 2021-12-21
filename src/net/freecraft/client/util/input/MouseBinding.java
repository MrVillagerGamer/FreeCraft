package net.freecraft.client.util.input;

import net.freecraft.util.RegistryEntry;

public class MouseBinding extends RegistryEntry {
	private IMouseHandler handler;
	private int button;
	private MouseState state;
	public MouseBinding(IMouseHandler handler, int button, MouseState state) {
		this.handler = handler;
		this.button = button;
		this.state = state;
	}
	public IMouseHandler getHandler() {
		return handler;
	}
	public int getButton() {
		return button;
	}
	public MouseState getState() {
		return state;
	}
}
