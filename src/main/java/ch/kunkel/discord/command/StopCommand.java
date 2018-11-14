package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class StopCommand implements Command {

	private GuildMessageReceivedEvent event;

	public StopCommand(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	protected StopCommand() {
	}

	@Override
	public void run() {
		MusicManager.getInstance().stop(event.getGuild(), event.getChannel());
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new StopCommand(event);
	}

	@Override
	public String getHelpString() {
		return "Stops the currently playing music";
	}

}
