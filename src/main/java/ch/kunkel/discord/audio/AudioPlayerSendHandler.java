package ch.kunkel.discord.audio;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;


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

	/**
	 * If {@link #canProvide()} returns true JDA will call this method in an attempt to retrieve audio data from the
	 * handler. This method need to provide 20 Milliseconds of audio data as a <b>array-backed</b> {@link ByteBuffer}.
	 * Use either {@link ByteBuffer#allocate(int)} or {@link ByteBuffer#wrap(byte[])}.
	 * <p>
	 * Considering this system needs to be low-latency / high-speed, it is recommended that the loading of audio data
	 * be done before hand or in parallel and not loaded from disk when this method is called by JDA. Attempting to load
	 * all audio data from disk when this method is called will most likely cause issues due to IO blocking this thread.
	 * <p>
	 * The provided audio data needs to be in the format: 48KHz 16bit stereo signed BigEndian PCM.
	 * <br>Defined by: {@link AudioSendHandler#INPUT_FORMAT AudioSendHandler.INPUT_FORMAT}.
	 * <br>If {@link #isOpus()} is set to return true, then it should be in pre-encoded Opus format instead.
	 *
	 * @return Should return a {@link ByteBuffer} containing 20 Milliseconds of audio.
	 * @see #isOpus()
	 * @see #canProvide()
	 * @see ByteBuffer#allocate(int)
	 * @see ByteBuffer#wrap(byte[])
	 */
	@Nullable
	@Override
	public ByteBuffer provide20MsAudio() {
		return ByteBuffer.wrap(lastFrame.getData());
	}

	@Override
	public boolean isOpus() {
		return true;
	}
}
