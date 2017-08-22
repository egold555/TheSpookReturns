package org.golde.java.game.helpers.movieplayer.impl;

import static org.lwjgl.openal.AL10.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.golde.java.game.helpers.movieplayer.AudioRenderer;
import org.golde.java.game.helpers.movieplayer.Movie;
import org.golde.java.game.helpers.movieplayer.craterstudio.EasyMath;
import org.golde.java.game.helpers.movieplayer.craterstudio.HighLevel;
import org.golde.java.game.helpers.movieplayer.craterstudio.SimpleBlockingQueue;
import org.golde.java.game.helpers.movieplayer.craterstudio.Streams;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

public class OpenALAudioRenderer extends AudioRenderer {

	private int lastBuffersProcessed = 0;
	private boolean hasMoreSamples = true;

	private static enum ActionType {
		ADJUST_VOLUME, PAUSE_AUDIO, RESUME_AUDIO, STOP_AUDIO, SET_POSITION
	}

	private static class Action {
		final ActionType type;
		final Object value;

		public Action(ActionType type, Object value) {
			this.type = type;
			this.value = value;
		}

		public float floatValue() {
			return ((Float) value).floatValue();
		}
	}

	final SimpleBlockingQueue<Action> pendingActions = new SimpleBlockingQueue<>();

	//

	private float volume = 1.0f;

	@Override
	public void setVolume(float volume) {
		if (!EasyMath.isBetween(volume, 0.0f, 1.0f)) {
			throw new IllegalArgumentException();
		}
		this.volume = volume;
		pendingActions.put(new Action(ActionType.ADJUST_VOLUME, Float.valueOf(volume)));
	}

	@Override
	public float getVolume() {
		return this.volume;
	}

	//

	@Override
	public void pause() {
		pendingActions.put(new Action(ActionType.PAUSE_AUDIO, null));
	}

	@Override
	public void resume() {
		pendingActions.put(new Action(ActionType.RESUME_AUDIO, null));
	}

	@Override
	public void stop() {
		pendingActions.put(new Action(ActionType.STOP_AUDIO, null));
	}
	
	public void setPosition(Vector3f newPos)
	{
		pendingActions.put(new Action(ActionType.SET_POSITION, newPos));
	}

	private State state = State.INIT;

	@Override
	public State getState() {
		return state;
	}

	private int alSource;

	private void init() {
		this.alSource = alGenSources();
		if (this.alSource == 0) {
			throw new IllegalStateException();
		}
		
		AL10.alSourcef(alSource, AL10.AL_ROLLOFF_FACTOR, 0.5f);
		AL10.alSourcef(alSource, AL10.AL_REFERENCE_DISTANCE, 6);
		AL10.alSourcef(alSource, AL10.AL_MAX_DISTANCE, 30);
		//AL10.alSourcef(alSource, AL10.AL_GAIN, 20);
	}

	private void buffer() {
		// buffer 1sec of audio
		for (int i = 0; i < 5 || i < frameRate * 0.1f; i++) {
			this.enqueueNextSamples();
		}

		alSourcePlay(alSource);
	}

	@SuppressWarnings("incomplete-switch")
	public boolean tick(Movie sync) {

		switch (this.state) {
			case INIT:
				this.init();
				this.state = State.BUFFERING;
				return true;

			case BUFFERING:
				this.buffer();
				this.state = State.PLAYING;
				return true;

			case CLOSED:
				return false;
		}

		if (alSource == 0) {
			throw new IllegalStateException();
		}

		/*
		 * Handle pending actions
		 */

		for (Action action; (action = pendingActions.poll()) != null;) {
			switch (action.type) {
				case ADJUST_VOLUME:
					alSourcef(alSource, AL_GAIN, action.floatValue());
					break;

				case PAUSE_AUDIO:
					alSourcePause(alSource);
					state = State.PAUSED;
					return true;

				case RESUME_AUDIO:
					alSourcePlay(alSource);
					state = State.PLAYING;
					break;

				case STOP_AUDIO:
					alSourceStop(alSource);
					state = State.CLOSED;
					break;
					
				case SET_POSITION:
					Vector3f pos = (Vector3f) action.value;
					AL10.alSource3f(alSource, AL10.AL_POSITION, pos.x, pos.y, pos.z);
					break;

				default:
					throw new IllegalStateException();
			}
		}

		switch (this.state) {
			case PLAYING:
			case CLOSED:
				break;

			case PAUSED:
				return true;

			default:
				throw new IllegalStateException();
		}

		int currentBuffersProcessed = alGetSourcei(alSource, AL_BUFFERS_PROCESSED);
		int toDiscard = currentBuffersProcessed - lastBuffersProcessed;
		lastBuffersProcessed = currentBuffersProcessed;

		
		if (toDiscard == 0) {
			return true;
		}
		if (toDiscard < 0) {
			throw new IllegalStateException();
		}

		for (int i = 0; i < toDiscard; i++) {
			int buffer = alSourceUnqueueBuffers(alSource);
			alDeleteBuffers(buffer);

			this.lastBuffersProcessed--;
			sync.onRenderedAudioBuffer();

			this.enqueueNextSamples();
		}

		int state = alGetSourcei(alSource, AL_SOURCE_STATE);
		switch (state) {
			case AL_PLAYING:
				// ok
				break;

			case AL_STOPPED:
				if (this.state == State.CLOSED) {
					while (true) {
						int buffer = alSourceUnqueueBuffers(alSource);
						if (buffer < 0) {
							break;
						}
						alDeleteBuffers(buffer);
					}
					this.state = State.CLOSED;
				}

				if (this.lastBuffersProcessed != 0) {
					throw new IllegalStateException("should never happen");
				}

				if (this.state != State.CLOSED && this.hasMoreSamples) {
					this.state = State.BUFFERING;
				} else {
					sync.onEndOfAudio();
					Streams.safeClose(this);
					return false;
				}
				break;

			default:
				throw new IllegalStateException("unexpected state");
		}

		return true;
	}

	private void enqueueNextSamples() {
		if (!this.hasMoreSamples) {
			return;
		}

		ByteBuffer samples = super.loadNextSamples();
		if (samples == null) {
			this.hasMoreSamples = false;
			return;
		}

		int buffer = alGenBuffers();
		alBufferData(buffer, AL_FORMAT_MONO16, samples, audioStream.sampleRate);
		alSourceQueueBuffers(this.alSource, buffer);
	}


	public void await() throws IOException {
		while (alGetSourcei(alSource, AL_SOURCE_STATE) == AL_PLAYING) {
			HighLevel.sleep(1);
		}
	}

	public void close() throws IOException {
		if (this.alSource != 0) {
			alSourceStop(this.alSource);
			alDeleteSources(this.alSource);
			this.alSource = 0;
			this.state = State.CLOSED;
		}

		super.close();
	}
}