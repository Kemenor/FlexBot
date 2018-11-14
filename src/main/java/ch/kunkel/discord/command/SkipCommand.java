package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SkipCommand implements Command {

	private GuildMessageReceivedEvent event;

	public SkipCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	protected SkipCommand() {
	}

	@Override
	public void run() {
		MusicManager.getInstance().skip(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new SkipCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Skips a song";
	}

}
