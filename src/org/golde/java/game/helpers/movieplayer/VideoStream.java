package org.golde.java.game.helpers.movieplayer;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.golde.java.game.helpers.movieplayer.craterstudio.SimpleBlockingQueue;
import org.golde.java.game.helpers.movieplayer.impl.VideoMetadata;

public class VideoStream implements Closeable {
	final DataInputStream videoStream;
	private final VideoMetadata metadata;
	private final byte[] tmp1, tmp2;
	private final SimpleBlockingQueue<ByteBuffer> emptyQueue, filledQueue;

	public VideoStream(InputStream rgbStream, VideoMetadata metadata) {
		this.videoStream = new DataInputStream(rgbStream);
		this.metadata = metadata;

		this.tmp1 = new byte[64 * 1024];
		this.tmp2 = new byte[(metadata.width * metadata.height * 3) % tmp1.length];

		this.emptyQueue = new SimpleBlockingQueue<>();
		this.filledQueue = new SimpleBlockingQueue<>();

		for (int i = 0; i < 3; i++) {
			this.emptyQueue.put(ByteBuffer.allocateDirect(metadata.width * metadata.height * 3));
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!closed) {
					if (!pumpFramesInto()) {
						break;
					}
				}

				filledQueue.put(EOF);
			}
		}).start();
	}

	public ByteBuffer pollFrameData() {
		return filledQueue.poll();
	}

	public void freeFrameData(ByteBuffer bb) {
		if (bb == null) {
			throw new IllegalArgumentException();
		}
		bb.clear();
		emptyQueue.put(bb);
	}

	public static final ByteBuffer EOF = ByteBuffer.allocateDirect(1);

	private boolean pumpFramesInto() {
		ByteBuffer rgbBuffer = emptyQueue.take();
		if (metadata.width * metadata.height * 3 != rgbBuffer.remaining()) {
			throw new IllegalArgumentException();
		}

		/*
		 * Using DataInputStream(ffmpeg.stdin).readFully(byte[64*1024]) instead of
		 * DataInputStream(BufferedInputStream(ffmpeg.stdin, 64*1024)) as it is
		 * about 50x slower... ~8ms vs. ~330ms per frame. wtf?!
		 */

		int cnt1 = rgbBuffer.remaining() / tmp1.length;
		int cnt2 = tmp2.length > 0 ? 1 : 0;

		try {
			/*
			 * Wouldn't that be easy...
			 * 
			 * videoStream.readFully(rgbArray);
			 */

			for (int i = 0; i < cnt1; i++) {
				videoStream.readFully(tmp1);
				rgbBuffer.put(tmp1);
			}
			for (int i = 0; i < cnt2; i++) {
				videoStream.readFully(tmp2);
				rgbBuffer.put(tmp2);
			}
			rgbBuffer.flip();
			
			filledQueue.put(rgbBuffer);

			return true;
		} catch (IOException exc) {
			return false;
		}
	}

	volatile boolean closed;

	@Override
	public void close() throws IOException {
		this.closed = true;
		this.videoStream.close();
	}
}