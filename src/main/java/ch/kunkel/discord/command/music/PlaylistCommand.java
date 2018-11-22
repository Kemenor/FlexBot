package ch.kunkel.discord.command.music;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.MessageBuilder.SplitPolicy;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class PlaylistCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private Logger logger = LoggerFactory.getLogger(PlaylistCommand.class);
	private GuildMessageReceivedEvent event;

	public PlaylistCommand(GuildMessageReceivedEvent event, MusicManager musicManager) {
		this.event = event;
		this.musicManager = musicManager;
	}

	protected PlaylistCommand() {
	}

	@Override
	public void run() {
		List<AudioTrack> list = musicManager.getPlaylist(event.getGuild(), event.getChannel());
		if (list != null && !list.isEmpty()) {
			logger.debug("{}", list);
			StringBuilder sb = new StringBuilder("Current playlist:\n");
			for (AudioTrack audioTrack : list) {
				sb.append(audioTrack.getInfo().title).append("\n");
			}

			MessageBuilder mb = new MessageBuilder(sb);
			for (Message message : mb.buildAll(SplitPolicy.NEWLINE)) {
				event.getChannel().sendMessage(message).queue();
			}
		} else {
			event.getChannel().sendMessage("No songs in playlist").queue();
		}
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new PlaylistCommand(event, musicManager);
	}

	@Override
	public String getCommandWord() {
		return "playlist";
	}

	@Override
	public String getDescription() {
		return "lists all tracks in the current playlist";
	}

	@Override
	public String[] getAlias() {
		return new String[] { "showplaylist" };
	}

}
