package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ClearCommand implements Command {

	private GuildMessageReceivedEvent event;

	protected ClearCommand() {
	}

	public ClearCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	@Override
	public void run() {
		MusicManager.getInstance().clear(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new ClearCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Stops the player and clears the playlist";
	}

}
