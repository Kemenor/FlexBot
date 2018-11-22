package ch.kunkel.discord.command.rss;

import com.google.inject.Inject;

import ch.kunkel.discord.command.Command;
import ch.kunkel.discord.rss.RSSManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class UnRegisterCommand extends Command {
	private String[] args;
	private GuildMessageReceivedEvent event;
	@Inject
	private RSSManager rssManager;

	protected UnRegisterCommand() {
	}

	public UnRegisterCommand(String[] args, GuildMessageReceivedEvent event, RSSManager rssManager) {
		this.args = args;
		this.event = event;
		this.rssManager = rssManager;
	}

	@Override
	public void run() {
		if (args.length >= 3) {
			rssManager.removeRSSFeed(args[2]);
		} else {
			event.getChannel().sendMessage("Not enough urls given!").submit();
		}

	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new UnRegisterCommand(args, event, rssManager);
	}

	@Override
	public String getCommandWord() {
		return "unregister";
	}

	@Override
	public String getDescription() {
		return "Unregisters a rss feed by webhook url";
	}

	@Override
	public String getUsage() {
		return "unregister <webhookurl>";
	}

}
