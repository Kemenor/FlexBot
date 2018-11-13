package ch.kunkel.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.kunkel.discord.command.CommandFactory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private CommandFactory factory = CommandFactory.getInstance();
	private CommandExecutor executor = new CommandExecutor();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		logger.debug("received message event");
		// no bot commands allowed
		if (event.getAuthor().isBot()) {
			return;
		}

		String[] command = event.getMessage().getContentStripped().split(" ");
		if (command[0].equals("!flex")) {
			executor.addCommand(factory.createCommand(command, event));
		}
	}

}