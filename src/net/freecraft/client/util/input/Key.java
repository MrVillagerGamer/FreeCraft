package net.freecraft.client.util.input;

import java.awt.event.KeyEvent;

public enum Key {
	W(KeyEvent.VK_W, 'w'),
	S(KeyEvent.VK_S, 's'),
	A(KeyEvent.VK_A, 'a'),
	D(KeyEvent.VK_D, 'd'),
	ESC(KeyEvent.VK_ESCAPE, '\0'),
	E(KeyEvent.VK_E, 'e'),
	SHIFT(KeyEvent.VK_SHIFT, ' '),
	SPACE(KeyEvent.VK_SPACE, ' ');
	private int code;
	private char chr;
	private Key(int code, char chr) {
		this.code = code;
		this.chr = chr;
	}
	public int getCode() {
		return code;
	}
	public char getChar() {
		return chr;
	}
}
