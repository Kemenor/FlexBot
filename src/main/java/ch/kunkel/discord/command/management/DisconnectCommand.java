package ch.kunkel.discord.command.management;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class DisconnectCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private GuildMessageReceivedEvent event;

	protected DisconnectCommand() {
	}

	public DisconnectCommand(GuildMessageReceivedEvent event, MusicManager musicManager) {
		this.event = event;
		this.musicManager = musicManager;
	}

	@Override
	public void run() {
		musicManager.disconnect(event.getGuild());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new DisconnectCommand(event, musicManager);
	}

	@Override
	public String getCommandWord() {
		return "disconnect";
	}

	@Override
	public String getDescription() {
		return "Disconnects the bot from his conencted voice channel";
	}

	@Override
	public String[] getAlias() {
		return new String[] { "bye", "disc" };
	}
}
