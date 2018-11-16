package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SearchCommand implements Command {

	private String[] args;
	private GuildMessageReceivedEvent event;

	public SearchCommand() {
	}

	public SearchCommand(String[] args, GuildMessageReceivedEvent event) {
		this.args = args;
		this.event = event;

	}

	@Override
	public void run() {
		StringBuilder sb = new StringBuilder("ytsearch: ");
		for (int i = 2; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}
		MusicManager.getInstance().play(sb.toString(), event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new SearchCommand(args, event);
	}

	@Override
	public String getHelpString() {
		return "searches the keywords on youtube";
	}

	@Override
	public int getMaxArgsCount() {
		return 10;
	}
}
