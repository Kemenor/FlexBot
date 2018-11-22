package ch.kunkel.discord.command.rss;

import ch.kunkel.discord.command.Command;
import ch.kunkel.discord.rss.RSSManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class RegisterCommand extends Command {

	private String[] args;
	private GuildMessageReceivedEvent event;

	protected RegisterCommand() {
	}

	public RegisterCommand(String[] args, GuildMessageReceivedEvent event) {
		this.args = args;
		this.event = event;
	}

	@Override
	public void run() {
		if (args.length >= 4) {
			RSSManager.getInstance().addRSSFeed(args[2], args[3]);
		} else {
			event.getChannel().sendMessage("Not enough urls given!").submit();
		}
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new RegisterCommand(args, event);
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
