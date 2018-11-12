package ch.kunkel.discord;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.kunkel.discord.command.Command;

public class CommandExecutor {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public void addCommand(Command c) {
		if (c != null) {
			logger.debug("adding command to execution queue");
			executor.execute(c);
		}
	}
}
