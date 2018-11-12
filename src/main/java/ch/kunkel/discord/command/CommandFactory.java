package ch.kunkel.discord.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandFactory {
	public static final Map<String, Command> defaultCommands = new HashMap<String, Command>() {
		{
			put("help", new HelpCommand());
			put("join", new JoinCommand());
			put("create", new CreateCommand());
		}
	};

	private Logger logger = LoggerFactory.getLogger(CommandFactory.class);
	private Map<String, Command> commandMap = new HashMap<>();
	private Command helpCommand = new HelpCommand();
	private static CommandFactory instance;

	public static CommandFactory getInstance() {
		if (instance == null) {
			instance = new CommandFactory();
			instance.registerAllCommand(defaultCommands);
		}
		return instance;
	}

	private CommandFactory() {
	}

	public void registerCommand(String name, Command command) {
		commandMap.put(name, command);
	}

	public void registerAllCommand(Map<String, Command> commandMap) {
		this.commandMap.putAll(commandMap);
	}

	/**
	 * creates a new command, if no command is registered it will create the help
	 * command
	 * 
	 * @param command the array of the provided command, first index is always !flex
	 * @param event   the messageevent that generated this command
	 * @return a new command that will be executed sometime in the future
	 */
	public Command createCommand(String[] command, MessageReceivedEvent event) {
		logger.debug("creating command {}", Arrays.toString(command));
		Command c = null;

		if (command.length >= 2 && command[1] != null) {
			c = commandMap.get(command[1].toLowerCase());
		}

		if (c == null) {
			logger.debug("command not found, creating help Command");
			event.getChannel().sendMessage("Sorry I didn't understand your command.").complete();
			return helpCommand.newInstance(null, event);
		}
		logger.debug("Command was found");
		return c.newInstance(command, event);
	}

	public Map<String, Command> getCommandMap() {
		return Collections.unmodifiableMap(commandMap);
	}
}
