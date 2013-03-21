package de.nerogar.gameV1;

public class Logger {

	public static final int NORMAL = 0;
	public static final int DEBUG = 1;
	public static final int ALL = 2;

	private static int currentOutput = DEBUG;

	public static void log(String msg, int level) {
		if (level >= currentOutput) {
			System.out.println(msg);
		}
	}

	public static void printThrowable(Throwable error, String comment, boolean forceExit) {
		System.out.println("------------------------------------------------");
		System.out.println("Crashreport:");

		if (comment != null) {
			System.out.println(comment);
		}

		error.printStackTrace();

		if (forceExit || error instanceof Error) {
			System.exit(0);
		}
	}
}
