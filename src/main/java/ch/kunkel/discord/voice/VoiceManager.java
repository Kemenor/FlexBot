package ch.kunkel.discord.voice;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechAligner;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import net.dv8tion.jda.core.audio.AudioReceiveHandler;
import net.dv8tion.jda.core.audio.CombinedAudio;
import net.dv8tion.jda.core.audio.UserAudio;

public class VoiceManager implements AudioReceiveHandler {
	private Logger logger = LoggerFactory.getLogger(VoiceManager.class);
	private PipedOutputStream pop = new PipedOutputStream();
	private PipedInputStream pip = new PipedInputStream();

	public VoiceManager() {
		try {
			pop.connect(pip);
			Configuration configuration = new Configuration();

			configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
			configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
			configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

			SpeechAligner a;

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
						recognizer.startRecognition(pip);
						SpeechResult result;
						while ((result = recognizer.getResult()) != null) {
							System.out.format("Hypothesis: %s\n", result.getHypothesis());
						}
						logger.error("Recognizer stopping");
						recognizer.stopRecognition();
					} catch (IOException e) {
						logger.error("Voice Recognition isn't working", e);
					}
				}
			});
			t.start();

		} catch (IOException e) {
			logger.error("Voice Recognition isn't working", e);
		}

	}

	@Override
	public boolean canReceiveCombined() {
		return false;
	}

	@Override
	public boolean canReceiveUser() {
		return true;
	}

	@Override
	public void handleCombinedAudio(CombinedAudio combinedAudio) {
		throw new UnsupportedOperationException("This handler doesn't accept combined Audio!");
	}
//	// 48KHz 16bit stereo signed BigEndian PCM
//	AudioFormat discordFormat = new AudioFormat(48000.0f, 16, 2, true, true);
//	// RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 16000 Hz
//	AudioFormat sphinxFormat = new AudioFormat(16000.0f, 16, 1, true, false);
//	System.out.println(AudioSystem.isConversionSupported(sphinxFormat, discordFormat));

	@Override
	public void handleUserAudio(UserAudio userAudio) {
		// userAudio
		try {
			byte[] data = userAudio.getAudioData(1.0);

			pop.write(data);
			// JDA: 48KHz 16bit stereo signed BigEndian PCM
			// RIFF (little-endian) data, WAVE audio, Microsoft PCM, 16 bit, mono 16000 Hz

			// 48Khz; 16 bit; stereo; signed BigEndian; PCM
			// 16Khz; 16 bit; mono; little-Endian; PCM
		} catch (IOException e) {
			logger.error("Can't write data to voice recognition", e);
		}
	}
}
