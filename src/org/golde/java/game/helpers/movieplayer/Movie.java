package org.golde.java.game.helpers.movieplayer;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.golde.java.game.helpers.movieplayer.impl.FFmpeg;
import org.golde.java.game.helpers.movieplayer.impl.VideoMetadata;

public class Movie implements Closeable {

	public static Movie open(File movieFile) throws IOException {
		return Movie.open(movieFile, 0);
	}

	public static Movie open(File movieFile, int seconds) throws IOException {
		VideoMetadata metadata = FFmpeg.extractMetadata(movieFile);

		InputStream rgb24Stream = FFmpeg.extractVideoAsRGB24(movieFile, seconds);
		InputStream wav16Stream = FFmpeg.extractAudioAsWAV(movieFile, seconds);

		AudioStream audioStream;
		try {
			audioStream = new AudioStream(wav16Stream);
		} catch (IOException exc) {
			audioStream = new AudioStream(); // no audio, feed in dummy samples
		}
		VideoStream videoStream = new VideoStream(rgb24Stream, metadata);

		return new Movie(metadata, videoStream, audioStream);
	}

	private Movie(VideoMetadata metadata, VideoStream videoStream, AudioStream audioStream) {
		this.metadata = metadata;
		this.videoStream = videoStream;
		this.audioStream = audioStream;
	}

	//

	private final VideoMetadata metadata;

	public int width() {
		return metadata.width;
	}

	public int height() {
		return metadata.height;
	}

	public float framerate() {
		return metadata.framerate;
	}

	//

	private final VideoStream videoStream;
	private final AudioStream audioStream;

	public VideoStream videoStream() {
		return videoStream;
	}

	public AudioStream audioStream() {
		return audioStream;
	}

	//

	private long initFrame;
	private long frameInterval;

	public void init() {
		initFrame = System.nanoTime();
		audioIndex = 0;
		videoIndex = 0;
		frameInterval = (long) (1000_000_000L / metadata.framerate);
	}

	//

	private static final int AUDIO_UNAVAILABLE = -1;
	private static final int AUDIO_TERMINATED = -2;

	private int audioIndex;
	private int videoIndex;

	public int getVideoFrame() {
		return videoIndex;
	}

	public float getPlayingTime() {
		return this.getVideoFrame() / this.framerate();
	}

	public void onMissingAudio() {
		audioIndex = AUDIO_UNAVAILABLE;
	}

	public void onEndOfAudio() {
		audioIndex = AUDIO_TERMINATED;

		try {
			this.close();
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	public void onRenderedAudioBuffer() {
		this.audioIndex++;
	}

	public void onUpdatedVideoFrame() {
		this.videoIndex++;
	}

	public boolean hasVideoBacklogOver(int frameCount) {
		switch (audioIndex) {
			case AUDIO_TERMINATED:
				// reached end of audio
				return false;

			case AUDIO_UNAVAILABLE:
				// sync video with clock
				return (videoIndex + frameCount) * frameInterval <= System.nanoTime() - initFrame;

			default:
				// sync video with audio
				return (videoIndex + frameCount) <= audioIndex;
		}
	}

	public boolean isTimeForNextFrame() {

		switch (audioIndex) {
			case AUDIO_TERMINATED:
				// reached end of audio
				return true;

			case AUDIO_UNAVAILABLE:
				// sync video with clock
				return videoIndex * frameInterval <= System.nanoTime() - initFrame;

			default:
				// sync video with audio
				return videoIndex <= audioIndex;
		}
	}

	@Override
	public void close() throws IOException {
		audioStream.close();
		videoStream.close();
	}
}
