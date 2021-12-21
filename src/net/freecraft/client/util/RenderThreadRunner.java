package net.freecraft.client.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderThreadRunner {
	private static List<RenderThreadRunner> runners = Collections.synchronizedList(new ArrayList<>());
	private Method toInvoke;
	private Object instance;
	private Object[] args;
	private Class<?>[] classes;
	public RenderThreadRunner(Object obj, String methodName, Object... args) {
		this.args = args;
		genClasses(args);
		try {
			this.toInvoke = obj.getClass().getDeclaredMethod(methodName, classes);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		this.instance = obj;
		runners.add(this);
	}
	public RenderThreadRunner(Class<?> obj, String methodName, Object... args) {
		this.args = args;
		genClasses(args);
		try {
			this.toInvoke = obj.getDeclaredMethod(methodName, classes);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		this.instance = null;
		runners.add(this);
	}
	private void genClasses(Object[] args) {
		this.classes = new Class[args.length];
		for(int i = 0; i < args.length; i++) {
			classes[i] = args[i].getClass();
			if(classes[i] == Integer.class) classes[i] = int.class;
			if(classes[i] == Long.class) classes[i] = long.class;
			if(classes[i] == Byte.class) classes[i] = byte.class;
			if(classes[i] == Short.class) classes[i] = short.class;
			if(classes[i] == Float.class) classes[i] = float.class;
			if(classes[i] == Double.class) classes[i] = double.class;
			if(classes[i] == Character.class) classes[i] = char.class;
			if(classes[i] == Boolean.class) classes[i] = boolean.class;
		}
	}
	private void run() {
		try {
			toInvoke.setAccessible(true);
			toInvoke.invoke(instance, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public static void runPending() {
		while(runners.size() > 0) {
			RenderThreadRunner runner = runners.remove(0);
			if(runner != null) {
				runner.run();
			}
		}
	}
}
