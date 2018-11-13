package ch.kunkel.discord.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class JoinCommand implements Command {

	private GuildMessageReceivedEvent event;

	protected JoinCommand() {
	}

	private JoinCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new JoinCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Joins the users voice channel";
	}
}
