package ch.kunkel.discord.command;

import ch.kunkel.discord.audio.MusicManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SetVolumeCommand implements Command {

	private GuildMessageReceivedEvent event;
	private int volume = 30;

	public SetVolumeCommand(String[] args, GuildMessageReceivedEvent event) {
		this.event = event;
		if (args.length >= 3) {
			try {
				volume = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
			}
		}
	}

	protected SetVolumeCommand() {
	}

	@Override
	public void run() {
		MusicManager.getInstance().setVolume(event.getGuild(), event.getChannel(), volume);
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new SetVolumeCommand(args, event);
	}

	@Override
	public String getHelpString() {
		return "Sets the volume of the music player";
	}

}
