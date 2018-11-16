package ch.kunkel.discord.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class UnRegisterCommand implements Command {

	@Override
	public void run() {

	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new UnRegisterCommand();
	}

	@Override
	public String getHelpString() {
		return "unregisters a rss feed either by rssurl or by webhook url";
	}

	@Override
	public int getMaxArgsCount() {
		return 0;
	}

}
