package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class UnpauseCommand implements Command {
	private GuildMessageReceivedEvent event;

	protected UnpauseCommand() {
	}

	public UnpauseCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
		MusicManager.getInstance().unpause(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new UnpauseCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Unpauses the music player";
	}
}