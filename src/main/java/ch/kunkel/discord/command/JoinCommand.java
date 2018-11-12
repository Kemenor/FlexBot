package ch.kunkel.discord.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class JoinCommand implements Command {

	private MessageReceivedEvent event;

	protected JoinCommand() {
	}

	private JoinCommand(MessageReceivedEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
		// TODO this command
	}

	@Override
	public Command newInstance(String[] args, MessageReceivedEvent event) {
		return new JoinCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Joins the users voice channel";
	}
}
