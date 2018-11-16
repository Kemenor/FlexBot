package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class PauseCommand implements Command {
	private GuildMessageReceivedEvent event;

	protected PauseCommand() {
	}

	public PauseCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
		MusicManager.getInstance().pause(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new PauseCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Pauses the music player";
	}

	@Override
	public int getMaxArgsCount() {
		return 0;
	}
}
