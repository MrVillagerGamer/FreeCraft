package net.freecraft.client;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import net.freecraft.FreeCraft;
import net.freecraft.client.entity.RenderEntityTypes;
import net.freecraft.client.net.ClientNetHandler;
import net.freecraft.client.net.ClientNetListener;
import net.freecraft.client.net.ClientSocketHandler;
import net.freecraft.client.recipe.CraftingRecipes;
import net.freecraft.client.render.Renderer;
import net.freecraft.client.state.State;
import net.freecraft.client.state.States;
import net.freecraft.client.util.Atlases;
import net.freecraft.client.util.ItemIcons;
import net.freecraft.client.util.RenderThreadRunner;
import net.freecraft.client.util.input.Input;
import net.freecraft.client.util.input.KeyBindings;
import net.freecraft.client.util.input.MouseBindings;
import net.freecraft.client.world.ClientWorld;
import net.freecraft.net.INetHandler;
import net.freecraft.net.INetListener;
import net.freecraft.net.NetConnection;
import net.freecraft.server.FreeCraftServer;
import net.freecraft.util.ThreadUtil;
import net.freecraft.world.World;

public class FreeCraftClient extends FreeCraft {
	private static FreeCraftClient inst;
	private static boolean netRunning = false;
	private static JFrame frame;
	private static Canvas canvas;
	private String username;
	private String passwordHash;
	private int glWidth, glHeight, uiWidth, uiHeight;
	public int getGLWidth() {
		return glWidth;
	}
	public int getGLHeight() {
		return glHeight;
	}
	public int getUIWidth() {
		return uiWidth;
	}
	public int getUIHeight() {
		return uiHeight;
	}
	public static FreeCraftClient get() {
		return inst;
	}
	private Renderer renderer;
	private boolean exiting = false;
	private NetConnection conn;
	public FreeCraftClient() {
		super();
		Atlases.init();
		ItemIcons.init();
		RenderEntityTypes.init();
		CraftingRecipes.init();
		KeyBindings.init();
		MouseBindings.init();
		inst = this;
		Thread thread = new Thread(this);
		thread.setName("CLIENT-0");
		thread.start();
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getUsername() {
		return username;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	@Override
	public World getWorld() {
		return super.getWorld();
	}
	@Override
	public INetHandler getNetHandler() {
		if(netHandler == null) netHandler = new ClientNetHandler();
		return super.getNetHandler();
	}
	@Override
	public INetListener getNetListener() {
		if(netListener == null) netListener = new ClientNetListener();
		return super.getNetListener();
	}
	@Override
	public void run() {
		boolean fullscreen = true;
		frame = new JFrame();
		frame.setTitle("FreeCraft");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int)(dim.getWidth()/1.5f), (int)(dim.getHeight()/1.5f));
		frame.setLocationRelativeTo(null);
		frame.setIgnoreRepaint(true);
		if(fullscreen) {
			frame.setSize(dim);
			frame.setUndecorated(true);
		}
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				FreeCraftClient.get().requestExit();
			}
		});
		canvas = new Canvas();
		canvas.setSize(frame.getSize());
		frame.add(canvas);
		if(!fullscreen) {
			frame.pack();
		}
		frame.setVisible(true);
		canvas.createBufferStrategy(2);
		canvas.setFocusable(true);
		canvas.addKeyListener(new Input());
		canvas.addMouseListener(new Input());
		canvas.addMouseMotionListener(new Input());
		canvas.addMouseWheelListener(new Input());
		Cursor cursor = frame.getContentPane().getCursor();
		Cursor hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(), "blank cursor");
		if(fullscreen) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		}
		glWidth = (int)canvas.getSize().getWidth();
		glHeight = (int)canvas.getSize().getHeight();
		uiWidth = (int)(300*canvas.getSize().getWidth()/canvas.getSize().getHeight());
		uiHeight = 300;
		State.set(States.LOGIN);
		while(!exiting) {
			BufferedImage target = new BufferedImage(getGLWidth(), getGLHeight(), BufferedImage.TYPE_INT_ARGB);
			State.get().render(target);
			BufferStrategy bs = canvas.getBufferStrategy();
			Graphics g = bs.getDrawGraphics();
			g.drawImage(target, 0, 0, (int)canvas.getSize().getWidth(), (int)canvas.getSize().getHeight(), null);
			bs.show();
			g.dispose();
			target.flush();
			if(State.get().isCursorVisible()) {
				frame.getContentPane().setCursor(cursor);
			}else {
				frame.getContentPane().setCursor(hiddenCursor);
			}
			Input.tick();
			RenderThreadRunner.runPending();
			State.get().update();
			Thread.yield();
		}
		disconnect();
		try {
			Thread socketHandlerThread = ThreadUtil.get("SERVER-0");
			if(socketHandlerThread != null) {
				socketHandlerThread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(renderer != null) {
			renderer.dispose();
		}
		frame.dispose();
		System.exit(0);
		
	}
	public void joinIntegratedServer() {
		FreeCraftServer.start();
		joinServer("127.0.0.1");
	}
	public void joinServer(String addr) {
		netRunning = true;
		world = new ClientWorld();
		State.set(States.PLAY);
		new ClientSocketHandler(addr);
	}
	public void disconnect() {
		netRunning = false;
		try {
			Thread socketHandlerThread = ThreadUtil.get("CLIENT-1");
			if(socketHandlerThread != null) {
				socketHandlerThread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(world != null) {
			world.dispose();
			world = null;
		}
	}
	@Override
	public boolean isNetRunning() {
		return netRunning;
	}
	public Renderer getRenderer() {
		if(renderer == null) {
			renderer = new Renderer();
			renderer.load();
		}
		return renderer;
	}
	public void requestExit() {
		exiting = true;
	}
	public void requestQuit() {
		requestExit();
	}
	public JFrame getJFrame() {
		return frame;
	}
	public Canvas getCanvas() {
		return canvas;
	}
	public boolean isRenderThread(Thread thread) {
		return thread.getName().equals("CLIENT-0");
	}
	public void setConnection(NetConnection conn) {
		this.conn = conn;
	}
	public NetConnection getConnection() {
		return conn;
	}
}
