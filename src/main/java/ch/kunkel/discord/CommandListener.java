package ch.kunkel.discord;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ch.kunkel.discord.command.CommandFactory;

public class CommandListener extends ListenerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Inject
	private CommandFactory factory;
	@Inject
	private CommandExecutor executor;

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
