package ch.kunkel.discord.command.music;

import com.google.inject.Inject;

import ch.kunkel.discord.audio.MusicManager;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SearchCommand extends Command {
	@Inject
	private MusicManager musicManager;
	private String[] args;
	private Guild guild;
	private TextChannel response;

	protected SearchCommand() {
	}

	public SearchCommand(String[] args, GuildMessageReceivedEvent event, MusicManager musicManager) {
		this.args = args;
		this.musicManager = musicManager;
		this.guild = event.getGuild();
		this.response = event.getChannel();
	}

	@Override
	public void run() {
		StringBuilder sb = new StringBuilder("ytsearch: ");
		for (int i = 2; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}
		musicManager.play(sb.toString(), guild, response);
	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new SearchCommand(args, event, musicManager);
	}

	@Override
	public String getCommandWord() {
		return "search";
	}

	@Override
	public String getDescription() {
		return "searches the keywords on youtube";
	}

	@Override
	public String getUsage() {
		return "search <identifier> {<identifier> ...}";
	}
}
