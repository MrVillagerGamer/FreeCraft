package net.freecraft.client.util.input;

import net.freecraft.util.RegistryEntry;

public class KeyBinding extends RegistryEntry {
	private IKeyHandler handler;
	private Key key;
	private KeyState state;
	public KeyBinding(IKeyHandler handler, Key key, KeyState state) {
		this.handler = handler;
		this.key = key;
		this.state = state;
	}
	public IKeyHandler getHandler() {
		return handler;
	}
	public Key getKey() {
		return key;
	}
	public KeyState getState() {
		return state;
	}
}
