package net.freecraft.client.state;

import java.awt.image.BufferedImage;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.UIComponent;

public abstract class State {
	private static State state;
	public static State get() {
		return state;
	}
	public static void set(State state) {
		if(!FreeCraftClient.get().isRenderThread(Thread.currentThread())) {
			throw new IllegalStateException("void State.set(State) can only be called on the client render thread. Use FreeCraftClient.requestSetState instead.");
		}
		if(State.state != null) {
			State.state.stop();
		}
		State.state = state;
		if(State.state != null) {
			State.state.start();
		}
	}
	public abstract void start();
	public abstract void update();
	public abstract void render(BufferedImage target);
	public boolean isCursorVisible() {
		return true;
	}
	public abstract void stop();
	public void escape() {
		stop();
	}
	public UIComponent[] getUIComponents() {
		return new UIComponent[0];
	}
	public void mouseClick(int x, int y) {
		
	}
}
