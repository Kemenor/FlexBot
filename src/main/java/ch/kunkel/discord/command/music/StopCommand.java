package ch.kunkel.discord.command.music;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class StopCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private GuildMessageReceivedEvent event;

	protected StopCommand() {
	}

	public StopCommand(GuildMessageReceivedEvent event, MusicManager musicManager) {
		this.event = event;
		this.musicManager = musicManager;
	}

	@Override
	public void run() {
		musicManager.stop(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new StopCommand(event, musicManager);
	}

	@Override
	public String getCommandWord() {
		return "stop";
	}

	@Override
	public String getDescription() {
		return "Stops the currently playing music";
	}

	@Override
	public String[] getAlias() {
		return new String[] { "clear" };
	}
}
