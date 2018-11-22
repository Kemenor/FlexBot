package ch.kunkel.discord.command.music;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ShuffleCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private GuildMessageReceivedEvent event;

	protected ShuffleCommand() {
	}

	public ShuffleCommand(MusicManager musicManager, GuildMessageReceivedEvent event) {
		this.musicManager = musicManager;
		this.event = event;
	}

	@Override
	public void run() {
		musicManager.shuffle(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new ShuffleCommand(musicManager, event);
	}

	@Override
	public String getCommandWord() {
		return "shuffle";
	}

	@Override
	public String getDescription() {
		return "shuffles the current playlist";
	}

}
