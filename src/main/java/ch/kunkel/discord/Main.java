package ch.kunkel.discord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	private final String TOKEN;

	public static void main(String[] args) {
		try {
			Main m = new Main();
		} catch (IOException | URISyntaxException e) {
			logger.error("Token couldn't be found! Aborting start!", e);
		} catch (LoginException e) {
			logger.error("Token is incorrect! Aborting start!", e);
		}
	}

	public Main() throws IOException, URISyntaxException, LoginException {
		TOKEN = new String(Files.readAllBytes(Paths.get(Main.class.getResource("token").toURI())));
		JDA api = new JDABuilder(TOKEN).build();
		try {
			api.awaitReady();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		api.addEventListener(new CommandListener());
		api.addEventListener(new ChannelManager());
	}
}