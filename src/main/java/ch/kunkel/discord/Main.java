package ch.kunkel.discord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ch.kunkel.discord.command.CommandModule;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	private final String TOKEN;

	public static void main(String[] args) {

		try {
			new Main();
		} catch (IOException e) {
			logger.error("Token couldn't be found! Aborting start!", e);
		} catch (LoginException e) {
			logger.error("Token is incorrect! Aborting start!", e);
		}
	}

	public Main() throws IOException, LoginException {
		TOKEN = new String(Files.readAllBytes(Paths.get("token")));
		JDA api = new JDABuilder(TOKEN).build();
		try {
			api.awaitReady();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Injector injector = Guice.createInjector(new CommandModule());
		api.addEventListener(injector.getInstance(CommandListener.class));
		api.addEventListener(injector.getInstance(FlexChannelManager.class));
		// call to create
	}
}
