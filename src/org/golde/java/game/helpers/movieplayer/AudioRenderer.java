package org.golde.java.game.helpers.movieplayer;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.golde.java.game.helpers.movieplayer.craterstudio.ByteList;

public abstract class AudioRenderer implements Closeable {
	public static enum State {
		INIT, BUFFERING, PLAYING, PAUSED, CLOSED;
	}

	protected AudioStream audioStream;
	protected float frameRate;
	private byte[] largest;
	private final int[] samplesInBuffers = new int[2];
	protected final ByteBuffer[] bufferDuo = new ByteBuffer[2];
	private final ByteList bufferIndexList = new ByteList();
	
	private boolean convertToMono;

	public void init(AudioStream audioStream, float frameRate, boolean convertToMono) {
		this.audioStream = audioStream;
		this.frameRate = frameRate;
		this.convertToMono = convertToMono;

		if (this.audioStream.numChannels != 2) {
			throw new IllegalStateException();
		}
		if (this.audioStream.bytesPerSample != 2) {
			throw new IllegalStateException();
		}
		
		int numChannels = convertToMono ? 1 : 2;

		samplesInBuffers[0] = (int) Math.floor(this.audioStream.sampleRate / frameRate);
		samplesInBuffers[1] = (int) Math.ceil(this.audioStream.sampleRate / frameRate);
		double samplesPerSecond = this.audioStream.sampleRate / (double) frameRate;

		this.calcSyncPattern(samplesPerSecond);

		for (int i = 0; i < bufferDuo.length; i++) {
			bufferDuo[i] = ByteBuffer.allocateDirect(samplesInBuffers[i] * (numChannels * this.audioStream.bytesPerSample));
		}

		if (convertToMono) {
		    largest = new byte[2 * Math.max(bufferDuo[0].capacity(), bufferDuo[1].capacity())];
		}
		else {
		    largest = new byte[Math.max(bufferDuo[0].capacity(), bufferDuo[1].capacity())];
		}
	}

	private void calcSyncPattern(double samplesPerSecond) {
		double bestHourlyError = Integer.MAX_VALUE;
		int bestHourlyErrorIndex = -1;

		double prevErr = 1.0;
		for (int i = 0; i < 10_000; i++) {
			double currErr = (samplesPerSecond * (i + 1)) % 1.0;
			int picked = ((currErr > prevErr) ? 0 : 1);
			prevErr = currErr;

			bufferIndexList.add((byte) picked);

			double hourlyError = this.calcHourlyError();
			if (hourlyError < bestHourlyError) {
				bestHourlyError = hourlyError;
				bestHourlyErrorIndex = i;
			}
			if (hourlyError < 0.01) {
				break;
			}
		}

		while (bufferIndexList.size() > bestHourlyErrorIndex + 1) {
			bufferIndexList.removeLast();
		}
	}

	private double calcHourlyError() {
		int totalSamples = 0;
		for (int i = 0; i < bufferIndexList.size(); i++) {
			totalSamples += samplesInBuffers[bufferIndexList.get(i)];
		}

		double desired = (audioStream.sampleRate / frameRate);
		double actual = (double) totalSamples / bufferIndexList.size();
		return (actual - desired) * 3600.0 / audioStream.sampleRate;
	}

	private int loadIndex = 0;

	public abstract State getState();

	//

	public abstract void pause();

	public abstract void resume();

	public abstract void stop();

	//

	public abstract float getVolume();

	public abstract void setVolume(float volume);

	//

	public abstract boolean tick(Movie sync);

	public ByteBuffer loadNextSamples() {
		try {
			
			// switch between big and small buffer
			ByteBuffer buffer = bufferDuo[bufferIndexList.get(loadIndex++ % bufferIndexList.size())];
			audioStream.readSamples(largest, 0, buffer.capacity() * (convertToMono ? 2 : 1));
			buffer.clear();
			if (convertToMono) {
				int samples = buffer.capacity() / 2;
				for (int i = 0; i < samples; ++i) {
					largest[i * 2] = largest[i * 4];
					largest[i * 2 + 1 ]= largest[i * 4 + 1];
				}
			}
			buffer.put(largest, 0, buffer.capacity());
			buffer.flip();

			return buffer;
		} catch (IOException exc) {
			return null;
		}
	}

	public void close() throws IOException {
		this.audioStream.close();
	}
}