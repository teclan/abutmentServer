package by.ywzn.abutmentServer;

import by.ywzn.abutmentServer.main.Main;

public class MainTest {
	public static void main(String[] args) {
		System.setProperty("MY_CONFIG_FILE", "classpath:test-application.properties");
		System.setProperty("log4jConfigLocation", "classpath:test-log4j.properties");
		Main.main(args);
	}
}
