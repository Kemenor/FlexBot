package ch.kunkel.discord.command.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class JoinCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private GuildMessageReceivedEvent event;
	private Logger logger = LoggerFactory.getLogger(JoinCommand.class);

	protected JoinCommand() {
	}

	private JoinCommand(GuildMessageReceivedEvent event, MusicManager musicManager) {
		this.event = event;
		this.musicManager = musicManager;
	}

	@Override
	public void run() {
		VoiceChannel vc = event.getMember().getVoiceState().getChannel();
		if (vc == null) {
			event.getChannel().sendMessage("It seems you are not connected to a voice channel!").queue();
			return;
		}
		logger.debug("{}", musicManager);
		// manager is shared
		musicManager.join(vc);
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new JoinCommand(event, musicManager);
	}

	@Override
	public String getCommandWord() {
		return "join";
	}

	@Override
	public String getDescription() {
		return "Joins the users voice channel";
	}

}
