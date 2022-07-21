package net.freecraft.client.util.input;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.IComponentKeyHandler;
import net.freecraft.client.gui.IComponentMouseHandler;
import net.freecraft.client.gui.UIComponent;
import net.freecraft.client.state.State;
import net.freecraft.client.state.States;
import net.freecraft.client.util.ClientRegistries;
import net.freecraft.client.util.RenderThreadRunner;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private Robot robot;
	private static List<Integer> downKeys = Collections.synchronizedList(new ArrayList<>());
	public Input() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {

	}
	@Override
	public void mousePressed(MouseEvent e) {
		Dimension size = FreeCraftClient.get().getCanvas().getSize();
		int x = (int) (e.getX()/size.getWidth()*FreeCraftClient.get().getUIWidth());
		int y = (int) (e.getY()/size.getHeight()*FreeCraftClient.get().getUIHeight());
		for(UIComponent comp : State.get().getUIComponents()) {
			if(comp instanceof IComponentMouseHandler) {
				new RenderThreadRunner(comp, "mouseClick", x, y, e.getButton() - 1);
			}
		}
		List<MouseBinding> bindings = ClientRegistries.MOUSE_BINDINGS.getAll();
		for(MouseBinding binding : bindings) {
			if(binding.getButton() == e.getButton() - 1) {
				if(binding.getState() == MouseState.CLICK) {
					new RenderThreadRunner(binding.getHandler(), "onEvent", x, y);
				}
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {

	}
	@Override
	public void mouseEntered(MouseEvent e) {

	}
	@Override
	public void mouseExited(MouseEvent e) {

	}
	@Override
	public void keyTyped(KeyEvent e) {

	}
	@Override
	public void keyPressed(KeyEvent e) {
		synchronized(downKeys) {
			if(!downKeys.contains(e.getKeyCode())) {
				downKeys.add(e.getKeyCode());
			}
		}
		for(UIComponent comp : State.get().getUIComponents()) {
			if(comp instanceof IComponentKeyHandler) {
				new RenderThreadRunner(comp, "keyDown", e.getKeyCode(), e.getKeyChar());
			}
		}
		List<KeyBinding> bindings = ClientRegistries.KEY_BINDINGS.getAll();
		for(KeyBinding binding : bindings) {
			if(binding.getKey().getCode() == e.getKeyCode()) {
				if(binding.getState() == KeyState.DOWN) {
					new RenderThreadRunner(binding.getHandler(), "onEvent", new Object[0]);
				}
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		synchronized(downKeys) {
			if(downKeys.contains(e.getKeyCode())) {
				downKeys.remove((Integer)e.getKeyCode());
			}
		}
		for(UIComponent comp : State.get().getUIComponents()) {
			if(comp instanceof IComponentKeyHandler) {
				new RenderThreadRunner(comp, "keyUp", e.getKeyCode(), e.getKeyChar());
			}
		}
		List<KeyBinding> bindings = ClientRegistries.KEY_BINDINGS.getAll();
		for(KeyBinding binding : bindings) {
			if(binding.getKey().getCode() == e.getKeyCode()) {
				if(binding.getState() == KeyState.UP) {
					new RenderThreadRunner(binding.getHandler(), "onEvent", new Object[0]);
				}
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {

	}
	private static int dx = 0, dy = 0;
	private boolean ignoreMouseMove = false;
	@Override
	public void mouseMoved(MouseEvent e) {
		FreeCraftClient inst = FreeCraftClient.get();
		if(ignoreMouseMove) {
			ignoreMouseMove = false;
			return;
		}
		dx += e.getX()-inst.getGLWidth()/2;
		dy += e.getY()-inst.getGLHeight()/2;
		
		if(State.get() != States.PLAY) {
			//lastX = e.getXOnScreen();
			//lastY = e.getYOnScreen();
		}else {
			//ignoreMouseMove = true;
			//robot.mouseMove(lastX, lastY);
			robot.mouseMove(inst.getGLWidth()/2, inst.getGLHeight()/2);
		}
		
		List<MouseBinding> bindings = ClientRegistries.MOUSE_BINDINGS.getAll();
		for(MouseBinding binding : bindings) {
			if(binding.getState() == MouseState.MOVE) {
				//Dimension size = FreeCraftClient.get().getCanvas().getSize();
				new RenderThreadRunner(binding.getHandler(), "onEvent", dx, dy);
			}
		}
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		for(UIComponent comp : State.get().getUIComponents()) {
			if(comp instanceof IComponentMouseHandler) {
				new RenderThreadRunner((IComponentMouseHandler)comp, "wheelMove", e.getWheelRotation());
			}
		}
	}
	public static void resetMouseDelta() {
		dx = 0; dy = 0;
	}
	public static void tick() {
		synchronized(downKeys) {
			for(int key : downKeys) {
				List<KeyBinding> bindings = ClientRegistries.KEY_BINDINGS.getAll();
				for(KeyBinding binding : bindings) {
					if(binding.getKey().getCode() == key) {
						if(binding.getState() == KeyState.HOLD) {
							new RenderThreadRunner(binding.getHandler(), "onEvent", new Object[0]);
						}
					}
				}
			}
		}
	}
}
