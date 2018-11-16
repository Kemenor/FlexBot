package ch.kunkel.discord.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandFactory {
	public static final Map<String, Command> defaultCommands = new HashMap<String, Command>() {
		{
			put("help", new HelpCommand());
			put("create", new CreateCommand());
			put("join", new JoinCommand());
			put("disconnect", new DisconnectCommand());
			put("play", new PlayCommand());
			put("stop", new StopCommand());
			put("skip", new SkipCommand());
			put("pause", new PauseCommand());
			put("unpause", new UnpauseCommand());
			put("setvolume", new SetVolumeCommand());
			put("search", new SearchCommand());
		}
	};

	private Logger logger = LoggerFactory.getLogger(CommandFactory.class);
	private Map<String, Command> commandMap = new HashMap<>();
	private Command helpCommand = new HelpCommand();

	public CommandFactory() {
		this.commandMap.putAll(defaultCommands);
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

		if (command.length >= 2 && command[1] != null) {
			c = commandMap.get(command[1].toLowerCase());
		}

		if (c == null) {
			logger.debug("command not found, creating help Command");
			event.getChannel().sendMessage("Sorry I didn't understand your command.").queue();
			return helpCommand.newInstance(null, event);
		}
		logger.debug("Command was found");
		return c.newInstance(command, event);
	}
}
