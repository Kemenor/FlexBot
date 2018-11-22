package ch.kunkel.discord.command.rss;

import com.google.inject.Inject;

import ch.kunkel.discord.command.Command;
import ch.kunkel.discord.rss.RSSManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class RegisterCommand extends Command {
	@Inject
	private RSSManager rssManager;
	private String[] args;
	private GuildMessageReceivedEvent event;

	protected RegisterCommand() {
	}

	public RegisterCommand(String[] args, GuildMessageReceivedEvent event, RSSManager rssManager) {
		this.args = args;
		this.event = event;
		this.rssManager = rssManager;
	}

	@Override
	public void run() {
		if (args.length >= 4) {
			rssManager.addRSSFeed(args[2], args[3]);
		} else {
			event.getChannel().sendMessage("Not enough urls given!").submit();
		}
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new RegisterCommand(args, event, rssManager);
	}

	@Override
	public String getCommandWord() {
		return "register";
	}

	@Override
	public String getDescription() {
		return "Registers a RSS Feed to the specified webhook.";
	}

	@Override
	public String getUsage() {
		return "register <rssurl> <webhookurl>";
	}

}
