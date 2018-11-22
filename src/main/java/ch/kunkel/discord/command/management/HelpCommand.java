package ch.kunkel.discord.command.management;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand extends Command {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Set<Command> commands;
	private TextChannel text;

	protected HelpCommand() {
	}

	public HelpCommand(Set<Command> commands, TextChannel text) {
		this.commands = commands;
		this.text = text;
	}

	@Override
	public void run() {
		logger.debug("Running help command");
		StringBuilder sb = new StringBuilder();
		for (Command entry : commands) {
			sb.append(entry.toString());
			sb.append("\n");
		}
		this.text.sendMessage(sb).queue();
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCommandWord() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Shows this Help";
	}

	public Command newInstance(Set<Command> commands, TextChannel text) {
		return new HelpCommand(commands, text);
	}

}
