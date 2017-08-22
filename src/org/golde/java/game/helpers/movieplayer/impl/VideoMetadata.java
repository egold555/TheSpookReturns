package org.golde.java.game.helpers.movieplayer.impl;

public class VideoMetadata {
	public final int width, height;
	public final float framerate;

	public VideoMetadata(int width, int height, float framerate) {
		this.width = width;
		this.height = height;
		this.framerate = framerate;
	}

	@Override
	public String toString() {
		return "VideoMetadata[" + width + "x" + height + " @ " + framerate + "fps]";
	}
}