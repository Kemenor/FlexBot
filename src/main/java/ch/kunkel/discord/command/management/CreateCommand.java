package ch.kunkel.discord.command.management;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.kunkel.discord.Config;
import ch.kunkel.discord.command.Command;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CreateCommand extends Command {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String[] args;
	private Config config = Config.getInstance();
	private GuildMessageReceivedEvent event;

	public CreateCommand(String[] args, GuildMessageReceivedEvent event) {
		this.args = args;
		this.event = event;
	}

	protected CreateCommand() {
	}

	@Override
	public void run() {
		Guild guild = event.getGuild();
		if (guild == null) {
			event.getChannel().sendMessage("this command can't be executed in private chat!").queue();
		}
		logger.debug("create {}", Arrays.toString(args));
		String category = null;
		if (args.length >= 3) {
			StringBuilder catName = new StringBuilder();
			for (int i = 2; i < args.length; i++) {
				catName.append(args[i]);
				catName.append(" ");
			}
			category = catName.toString().trim();
		}
		logger.debug("running create command with category {}", category);
		if (category != null) {
			List<Category> categoriesByName = guild.getCategoriesByName(category, true);
			if (!categoriesByName.isEmpty()) {
				logger.debug("creating category voice channel");
				categoriesByName.get(0).createVoiceChannel(config.getProperty("Channel.Manager")).queue((channel) -> {
					event.getChannel().sendMessage("Created channel \"" + config.getProperty("Channel.Manager")
							+ "\" at category \"" + categoriesByName.get(0).getName() + "\".").queue();
				}, (throwable) -> {
					event.getChannel().sendMessage("Couldn't create channel \"" + config.getProperty("Channel.Manager")
							+ "\" at category \"" + categoriesByName.get(0).getName() + "\". " + throwable.getMessage())
							.queue();
				});

				return;
			}
		}
		logger.debug("creating guild voice channel");
		guild.getController().createVoiceChannel(config.getProperty("Channel.Manager")).queue((channel) -> {
			event.getChannel()
					.sendMessage("Created channel " + config.getProperty("Channel.Manager") + " at topplevel.").queue();
		}, (throwable) -> {
			event.getChannel().sendMessage("Couldn't create channel " + config.getProperty("Channel.Manager")
					+ " at topplevel. " + throwable.getMessage()).queue();
		});

	}

	@Override
	public Command newInstance(String[] args, GuildMessageReceivedEvent event) {
		return new CreateCommand(args, event);
	}

	@Override
	public String getCommandWord() {
		return "create";
	}

	@Override
	public String getDescription() {
		return "creates the flex voice channel if it doesn't exists.";
	}

	@Override
	public String getUsage() {
		return "create {<category>}";
	}

}
