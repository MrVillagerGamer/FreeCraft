package net.freecraft.util;

import java.io.PrintStream;

public class Logger {
	private static void print(String marker, String msg, PrintStream stream) {
		stream.println("[" + Thread.currentThread().getName() + "/" + marker + "] " + msg);
	}
	public static void debug(String msg) {
		print("DEBUG", msg, System.out);
	}
	public static void error(String msg) {
		print("ERROR", msg, System.err);
	}
}
