package ch.kunkel.discord.audio;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {
	private AudioFrame lastFrame;
	private final TrackScheduler trackscheduler;

	public AudioPlayerSendHandler(TrackScheduler trackscheduler) {
		this.trackscheduler = trackscheduler;
	}

	@Override
	public boolean canProvide() {
		lastFrame = trackscheduler.provide();
		return lastFrame != null;
	}

	@Override
	public byte[] provide20MsAudio() {
		return lastFrame.getData();
	}

	@Override
	public boolean isOpus() {
		return true;
	}
}