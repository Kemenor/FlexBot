package ch.kunkel.discord;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.kunkel.discord.command.CommandFactory;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private CommandFactory factory = CommandFactory.getInstance();
	private CommandExecutor executor = new CommandExecutor();
	private List<VoiceChannel> managedChannel = new ArrayList<>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		logger.debug("received message event");
		// no bot commands allowed
		if (event.getAuthor().isBot()) {
			return;
		}

		String[] command = event.getMessage().getContentStripped().split(" ");
		if (command[0].equals("!flex")) {
			executor.addCommand(factory.createCommand(command, event));
		}
	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		logger.debug("join event");
		// only act if the channel name is ours
		VoiceChannel joinedVC = event.getChannelJoined();
		Member affectedMember = event.getMember();
		if (!creationCheck(joinedVC, affectedMember)) {
			updateCheck(joinedVC);
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		logger.debug("leave event");
		leftCheck(event.getChannelLeft());
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		logger.debug("Move event");
		if (!creationCheck(event.getChannelJoined(), event.getMember())) {
			updateCheck(event.getChannelJoined());
		}
		if (!leftCheck(event.getChannelLeft())) {
			updateCheck(event.getChannelLeft());
		}

	}

	private void updateCheck(VoiceChannel vc) {
		if (managedChannel.contains(vc)) {
			List<Member> members = vc.getMembers();
		}
	}

	private boolean leftCheck(VoiceChannel vc) {
		if (managedChannel.contains(vc)) {
			logger.debug("Someone left a managed channel, checking if channel needs to be deleted");
			if (vc.getMembers().size() == 0) {
				vc.delete().submit();
				return true;
			}
		}
		return false;
	}

	private boolean creationCheck(VoiceChannel joinedVC, Member affectedMember) {
		if (joinedVC.getName().equals(Main.VCANNELNAME)) {
			logger.info("Someone joined the managed voice chat, creating new channel");
			Category cat = joinedVC.getParent();
			if (cat == null) {
				joinedVC.getGuild().getController()
						.createVoiceChannel("FlexChannel " + affectedMember.getEffectiveName()).queue((c) -> {
							VoiceChannel vc = (VoiceChannel) c;
							managedChannel.add(vc);
							joinedVC.getGuild().getController().moveVoiceMember(affectedMember, vc).submit();
						});
			} else {
				cat.createVoiceChannel("FlexChannel " + affectedMember.getEffectiveName()).queue((c) -> {
					VoiceChannel vc = (VoiceChannel) c;
					managedChannel.add(vc);
					joinedVC.getGuild().getController().moveVoiceMember(affectedMember, vc).submit();
				});
			}
			return true;
		}
		return false;
	}

}
