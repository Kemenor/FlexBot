package ch.kunkel.discord.audio;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration.ResamplingQuality;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import edu.cmu.sphinx.api.Configuration;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class MusicManager {
	private static MusicManager instance = new MusicManager();
	private AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	private Map<Guild, TrackScheduler> map = new HashMap<>();
	private Logger logger = LoggerFactory.getLogger(MusicManager.class);
	private Configuration configuration = new Configuration();

	public MusicManager() {
		logger.debug("registering remote sources");
		playerManager.getConfiguration().setResamplingQuality(ResamplingQuality.HIGH);
		AudioSourceManagers.registerRemoteSources(playerManager);

		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
	}

	public void join(VoiceChannel vc) {
		logger.debug("join called");
		if (map.containsKey(vc.getGuild())) {
			logger.debug("already connected switching channel.");
			AudioManager am = vc.getGuild().getAudioManager();
			am.openAudioConnection(vc);
		} else {
			logger.debug("not connected to guild creating guild connection");
			TrackScheduler trackScheduler = new TrackScheduler(playerManager);

			AudioManager am = vc.getGuild().getAudioManager();
			am.openAudioConnection(vc);
			am.setSendingHandler(new AudioPlayerSendHandler(trackScheduler));
//			try {
//				logger.debug("setting receiving handler");
//				am.setReceivingHandler(new VoiceManager(configuration));
//			} catch (IOException e) {
//				logger.warn("Voice Recognition not working!", e);
//			}

			am.openAudioConnection(vc);
			map.put(vc.getGuild(), trackScheduler);
			logger.debug("Guild id: {}", vc.getGuild().getId());
		}
	}

	public void disconnect(Guild guild) {
		logger.debug("disconnect called");
		AudioManager am = guild.getAudioManager();
		am.setSendingHandler(null);
		am.setReceivingHandler(null);
		am.closeAudioConnection();
//		try {
//			// TODO uncomment when voice works
//			VoiceManager vc = (VoiceManager) am.getReceiveHandler();
//			vc.close();
//		} catch (IOException e) {
//			logger.warn("Couldn't close receive handler", e);
//		}
		map.remove(guild);
	}

	public void play(String identifier, Guild guild, TextChannel text) {
		logger.debug("play called");
		logger.debug("Guild id: {}", guild.getId());
		logger.debug("{}", map.size());
		if (map.containsKey(guild)) {
			logger.debug("play accepted");
			if ("".equals(identifier)) {
				map.get(guild).start();
			} else {
				logger.debug("trying to find {}", identifier);
				playerManager.loadItem(identifier, new AudioLoadResultHandler() {
					@Override
					public void trackLoaded(AudioTrack track) {
						map.get(guild).queue(track);
						text.sendMessage("Added a song with identifier \"" + identifier + "\"").queue();
					}

					@Override
					public void playlistLoaded(AudioPlaylist playlist) {
						for (AudioTrack audioTrack : playlist.getTracks()) {
							map.get(guild).queue(audioTrack);
						}
						text.sendMessage("Added a playlist with identifier \"" + identifier + "\"").queue();
					}

					@Override
					public void noMatches() {
						text.sendMessage("Couldn't find a song with identifier \"" + identifier + "\"").queue();
					}

					@Override
					public void loadFailed(FriendlyException exception) {
						text.sendMessage("Couldn't load song with \"" + identifier + "\" " + exception.getMessage())
								.queue();
					}
				});
			}
		} else {
			text.sendMessage("It seems I'm not connected to your Server!");
		}
	}

	public void stop(Guild guild, TextChannel text) {
		logger.debug("stop called");
		if (map.containsKey(guild)) {
			map.get(guild).stop();
		}
	}

	public void pause(Guild guild, TextChannel text) {
		logger.debug("pause called");
		if (map.containsKey(guild)) {
			map.get(guild).pause();
		}
	}

	public void unpause(Guild guild, TextChannel text) {
		logger.debug("unpause called");
		if (map.containsKey(guild)) {
			map.get(guild).unpause();
		}
	}

	public void setVolume(Guild guild, TextChannel text, int volume) {
		logger.debug("setvolume called");
		if (map.containsKey(guild)) {
			map.get(guild).setVolume(volume);
		}
	}

	public void skip(Guild guild, TextChannel text) {
		logger.debug("skip called");
		if (map.containsKey(guild)) {
			map.get(guild).skip();
		}
	}

	public void clear(Guild guild, TextChannel text) {
		logger.debug("clear called");
		if (map.containsKey(guild)) {
			map.get(guild).clear();
		}
	}

	public static MusicManager getInstance() {
		return instance;
	}
}
