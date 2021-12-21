package net.freecraft.util;

public class ThreadUtil {
	public static void print() {
		Thread[] threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		for(Thread thread : threads) {
			System.out.println(thread.getName());	
		}
	}
	public static Thread get(String name) {
		Thread[] threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		for(Thread thread : threads) {
			if(thread.getName().equals(name)) {
				return thread;
			}
		}
		return null;
	}
}
