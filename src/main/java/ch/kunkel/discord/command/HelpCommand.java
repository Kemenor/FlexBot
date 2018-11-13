package ch.kunkel.discord.command;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand implements Command {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private GuildMessageReceivedEvent event;

	protected HelpCommand() {
	}

	private HelpCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
		logger.debug("Running help command");
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Command> entry : CommandFactory.getInstance().getCommandMap().entrySet()) {
			sb.append(entry.getKey());
			sb.append(" - ");
			sb.append(entry.getValue().getHelpString());
			sb.append("\n");
		}
		event.getChannel().sendMessage(sb).submit();
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new HelpCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Shows this Help";
	}

}
