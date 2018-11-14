package ch.kunkel.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.kunkel.discord.command.CommandFactory;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private CommandFactory factory = new CommandFactory();
	private CommandExecutor executor = new CommandExecutor();

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		// no bot commands allowed
		if (event.getAuthor().isBot()) {
			return;
		}

		String[] command = event.getMessage().getContentRaw().split(" ");
		if (command[0].equals("!flex")) {
			executor.addCommand(factory.createCommand(command, event));
		}
	}

}
