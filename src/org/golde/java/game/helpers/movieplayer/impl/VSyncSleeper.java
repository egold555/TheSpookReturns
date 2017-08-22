package org.golde.java.game.helpers.movieplayer.impl;

import org.golde.java.game.GLog;
import org.golde.java.game.helpers.movieplayer.craterstudio.HighLevel;
import org.golde.java.game.helpers.movieplayer.craterstudio.TextValues;
import org.lwjgl.opengl.Display;

class VSyncSleeper {
	private static final long INTERVAL = 1_000_000_000L / 60;

	private long nextPostSync;
	private long errorMargin;

	public void setSyncErrorMargin(int millis) {
		this.errorMargin = (millis * 1_000_000L);
	}

	public void measureSyncTimestamp(int frames) {
		// ensure GPU cannot use async updates
		nextPostSync = System.nanoTime();
		for (int i = 0; i < frames; i++) {
			Display.update();
			long now = System.nanoTime();
			GLog.info("VSyncSleeper: " + TextValues.formatNumber((now - nextPostSync) / 1_000_000.0, 2) + "ms");
			nextPostSync = now;
		}
	}

	public void sleepUntilBeforeVsync() {
		this.calcNextPostVync(System.nanoTime());

		long wakeupAt = this.nextPostSync - this.errorMargin;
		if (wakeupAt < System.nanoTime()) {
			return;
		}

		while (System.nanoTime() < wakeupAt) {
			HighLevel.sleep(1);
		}
	}

	private void calcNextPostVync(long now) {
		long next = nextPostSync;
		while (next < now) {
			next += INTERVAL;
		}
		nextPostSync = next;
	}
}
