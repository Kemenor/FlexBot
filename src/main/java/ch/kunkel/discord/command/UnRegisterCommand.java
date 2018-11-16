package ch.kunkel.discord.command;

import ch.kunkel.discord.rss.RSSManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class UnRegisterCommand implements Command {

	private String[] args;
	private GuildMessageReceivedEvent event;

	public UnRegisterCommand(String[] args, GuildMessageReceivedEvent event) {
		this.args = args;
		this.event = event;
	}
	
	protected UnRegisterCommand() {
	}

	@Override
	public void run() {
		if (args.length >= 3) {
			RSSManager.getInstance().removeRSSFeed(args[2]);
		} else {
			event.getChannel().sendMessage("Not enough urls given!").submit();
		}

	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new UnRegisterCommand(args, event);
	}

	@Override
	public String getHelpString() {
		return "unregisters a rss feed by webhook url";
	}

	@Override
	public int getMaxArgsCount() {
		return 0;
	}

}
