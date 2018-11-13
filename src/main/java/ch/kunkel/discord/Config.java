package ch.kunkel.discord;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	private Logger logger = LoggerFactory.getLogger(Config.class);
	private static Config instance;
	private Properties prop = new Properties();

	private Config() {
		// set defaults
		prop.setProperty("Channel.Prefix", "FlexChannel ");
		prop.setProperty("Channel.Manager", "join to create FlexChannel");
		prop.setProperty("Channel.updateInterval", "60");
		// overwrite from config file
		try (FileInputStream fis = new FileInputStream("config.properties")) {
			prop.load(fis);
		} catch (IOException e) {
			logger.warn("Couldn't load config file using default configs", e);
		}

	}

	public String getProperty(String name) {
		return prop.getProperty(name);
	}

	public String getProperty(String name, String defaultValue) {
		return prop.getProperty(name, defaultValue);
	}

	public int getProperty(String name, int defaultValue) {
		String s = prop.getProperty(name, String.valueOf(defaultValue));
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public static synchronized Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
}
