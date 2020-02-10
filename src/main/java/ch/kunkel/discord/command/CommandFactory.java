package ch.kunkel.discord.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ch.kunkel.discord.command.management.HelpCommand;

public class CommandFactory {
	private Logger logger = LoggerFactory.getLogger(CommandFactory.class);
	private Map<String, Command> commandMap = new HashMap<>();
	private HelpCommand help = new HelpCommand(null, null);
	private Set<Command> commands;

	@Inject
	public CommandFactory(Set<Command> commands) {
		this.commands = commands;
		for (Command command : commands) {
			commandMap.put(command.getCommandWord(), command);
			String[] aliases = command.getAlias();
			if (aliases != null) {
				for (String alias : aliases) {
					commandMap.put(alias, command);
				}
			}
		}
	}

	/**
	 * creates a new command, if no command is registered it will create the help
	 * command
	 * 
	 * @param command the array of the provided command, first index is always !flex
	 * @param event   the messageevent that generated this command
	 * @return a new command that will be executed sometime in the future
	 */
	public Command createCommand(String[] command, GuildMessageReceivedEvent event) {
		logger.debug("creating command {}", Arrays.toString(command));
		Command c = null;

		// Help is a special command
		if (command[1].toLowerCase().equals("help")) {
			return help.newInstance(commands, event.getChannel());
		} else if (command.length >= 2 && command[1] != null) {
			c = commandMap.get(command[1].toLowerCase());
		}

		if (c == null) {
			logger.debug("command not found, creating help Command");
			event.getChannel().sendMessage("Sorry I didn't understand your command. Here is my help:").queue();
			return help.newInstance(commands, event.getChannel());
		}
		logger.debug("Command was found");
		return c.newInstance(command, event);
	}

	public Set<Command> getCommands() {
		return commands;
	}
}
