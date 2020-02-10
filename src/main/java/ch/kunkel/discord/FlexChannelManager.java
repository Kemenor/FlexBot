package ch.kunkel.discord;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public class FlexChannelManager extends ListenerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Map<VoiceChannel, String> managedChannel = new HashMap<>();
	private Config config = Config.getInstance();
	private Timer t = new Timer(true);
	private TimerTask updateChannel = new TimerTask() {
		@Override
		public void run() {
			logger.debug("updating channel names");
			for (VoiceChannel vc : managedChannel.keySet()) {
				updateChannelName(vc);
			}
		}
	};

	public FlexChannelManager() {
		t.schedule(updateChannel, 0, config.getProperty("Channel.updateInterval", 60) * 1000);
	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		// only act if the channel name is ours
		VoiceChannel joinedVC = event.getChannelJoined();
		Member affectedMember = event.getMember();
		if (!creationCheck(joinedVC, affectedMember)) {
			updateCheck(joinedVC);
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		leftCheck(event.getChannelLeft());
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		if (!creationCheck(event.getChannelJoined(), event.getMember())) {
			updateCheck(event.getChannelJoined());
		}
		if (!leftCheck(event.getChannelLeft())) {
			updateCheck(event.getChannelLeft());
		}

	}

	private void updateCheck(VoiceChannel vc) {
		if (managedChannel.containsKey(vc)) {
			updateChannelName(vc);
		}
	}

	private void updateChannelName(VoiceChannel vc) {
		// theres at least one member in the channel here
		// because the other function would have deleted it
		List<Member> members = vc.getMembers();
		// still checking it to make sure
		if (members.size() > 0) {
			Map<String, AtomicInteger> gameCounter = new HashMap<>();
			for (Member member : members) {
				List<Activity> activities = member.getActivities();
					for(Activity activity : activities){
							if (gameCounter.containsKey(activity.getName())) {
								gameCounter.get(activity.getName()).incrementAndGet();
							} else {
								gameCounter.put(activity.getName(), new AtomicInteger(1));
							}
					}
			}
			String name;
			if (gameCounter.isEmpty()) {
				// no one is playing games this is so sad
				name = config.getProperty("Channel.Prefix") + managedChannel.get(vc);
				if (name.equals(config.getProperty("Channel.Prefix") + "Beckicious")) {
					name = "Flexy Becksy";
				}
			} else {
				String mostplayedGame = gameCounter
						.entrySet().stream().max((Entry<String, AtomicInteger> e,
								Entry<String, AtomicInteger> y) -> e.getValue().get() - y.getValue().get())
						.get().getKey();
				name = config.getProperty("Channel.Prefix") + mostplayedGame;
			}
			vc.getManager().setName(name).queue();
		}
	}

	private boolean leftCheck(VoiceChannel vc) {
		if (managedChannel.containsKey(vc)) {
			logger.debug("Someone left a managed channel, checking if channel needs to be deleted");
			if (vc.getMembers().size() == 0) {
				managedChannel.remove(vc);
				vc.delete().queue();
				return true;
			}
		}
		return false;
	}

	private boolean creationCheck(VoiceChannel joinedVC, Member affectedMember) {
		if (joinedVC.getName().equals(config.getProperty("Channel.Manager"))) {
			logger.info("Someone joined the managed voice chat, creating new channel");
			Category cat = joinedVC.getParent();
			String name = config.getProperty("Channel.Prefix") + affectedMember.getEffectiveName();
			// very important code
			if (name.equals(config.getProperty("Channel.Prefix") + "Beckicious")) {
				name = "Flexy Becksy";
			}
			if (cat == null) {
				joinedVC.getGuild().createVoiceChannel(name).queue((c) -> {
					VoiceChannel vc = (VoiceChannel) c;
					managedChannel.put(vc, affectedMember.getEffectiveName());
					joinedVC.getGuild().moveVoiceMember(affectedMember, vc).queue();
				});
			} else {
				cat.createVoiceChannel(name).queue((vc) -> {
					managedChannel.put(vc, affectedMember.getEffectiveName());
					joinedVC.getGuild().moveVoiceMember(affectedMember, vc).queue();
				});
			}
			return true;
		}
		return false;
	}
}
