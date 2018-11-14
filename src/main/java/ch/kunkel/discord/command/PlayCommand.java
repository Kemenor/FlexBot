package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class PlayCommand implements Command {

	private String[] args;
	private GuildMessageReceivedEvent event;

	protected PlayCommand() {
	}

	public PlayCommand(String[] args, GuildMessageReceivedEvent event) {
		this.args = args;
		this.event = event;
	}

	@Override
	public void run() {
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}
		MusicManager.getInstance().play(sb.toString().trim(), this.event.getGuild(), this.event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new PlayCommand(args, event);
	}

	@Override
	public String getHelpString() {
		return "tries to find a song matching the identifier. Usage: !flex play <identifier>";
	}

}
