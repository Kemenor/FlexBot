package ch.kunkel.discord.command.music;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SkipCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private GuildMessageReceivedEvent event;

	protected SkipCommand() {
	}

	public SkipCommand(MusicManager musicManager, GuildMessageReceivedEvent event) {
		this.musicManager = musicManager;
		this.event = event;
	}

	@Override
	public void run() {
		musicManager.skip(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new SkipCommand(musicManager, event);
	}

	@Override
	public String getCommandWord() {
		return "skip";
	}

	@Override
	public String getDescription() {
		return "Skips a song";
	}
}
