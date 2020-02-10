package ch.kunkel.discord.command.music;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SetVolumeCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private GuildMessageReceivedEvent event;
	private int volume = 10;

	protected SetVolumeCommand() {
	}

	public SetVolumeCommand(String[] args, GuildMessageReceivedEvent event, MusicManager musicManager) {
		this.event = event;
		this.musicManager = musicManager;
		if (args.length >= 3) {
			try {
				volume = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
			}
		}
	}

	@Override
	public void run() {
		musicManager.setVolume(event.getGuild(), event.getChannel(), volume);
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new SetVolumeCommand(args, event, musicManager);
	}

	@Override
	public String getCommandWord() {
		return "setvolume";
	}

	@Override
	public String getDescription() {
		return "Sets the volume of the music player";
	}

	@Override
	public String getUsage() {
		return "setvolume <1-100>";
	}

	@Override
	public String[] getAlias() {
		return new String[] { "volume" };
	}

}
