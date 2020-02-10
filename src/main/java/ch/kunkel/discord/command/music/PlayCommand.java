package ch.kunkel.discord.command.music;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PlayCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private String[] args;
	private GuildMessageReceivedEvent event;

	protected PlayCommand() {
	}

	public PlayCommand(String[] args, GuildMessageReceivedEvent event, MusicManager musicManager) {
		this.args = args;
		this.event = event;
		this.musicManager = musicManager;
	}

	@Override
	public void run() {
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}
		musicManager.play(sb.toString().trim(), this.event.getGuild(), this.event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new PlayCommand(args, event, musicManager);
	}

	@Override
	public String getCommandWord() {
		return "play";
	}

	@Override
	public String getUsage() {
		return "play <UrlToMusic>";
	}

	@Override
	public String getDescription() {
		return "adds the given url to the playlist";
	}
}
