package ch.kunkel.discord.command.music;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UnpauseCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private GuildMessageReceivedEvent event;

	protected UnpauseCommand() {
	}

	public UnpauseCommand(GuildMessageReceivedEvent event, MusicManager musicManager) {
		this.event = event;
		this.musicManager = musicManager;
	}

	@Override
	public void run() {
		musicManager.unpause(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new UnpauseCommand(event, musicManager);
	}

	@Override
	public String getCommandWord() {
		return "unpause";
	}

	@Override
	public String getDescription() {
		return "Unpauses the music player";
	}
}
