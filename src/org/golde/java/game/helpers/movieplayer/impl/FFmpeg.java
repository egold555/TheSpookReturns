package org.golde.java.game.helpers.movieplayer.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.golde.java.game.helpers.movieplayer.craterstudio.NullOutputStream;
import org.golde.java.game.helpers.movieplayer.craterstudio.RegexUtil;
import org.golde.java.game.helpers.movieplayer.craterstudio.Streams;
import org.golde.java.game.helpers.movieplayer.craterstudio.TextValues;

public class FFmpeg {

	public static String FFMPEG_PATH;
	public static boolean FFMPEG_VERBOSE = false;

	static {
		/*String resourceName = "./lib/ffmpeg/ffmpeg";
		if (Extractor.isMac) {
			resourceName += "-mac";
		} else {
			resourceName += Extractor.is64bit ? "64" : "32";
			if (Extractor.isWindows) {
				resourceName += ".exe";
			}
		}

		FFMPEG_PATH = resourceName;
		*/
		
		FFMPEG_PATH = "ffmpeg.exe";

		if (!new File(FFMPEG_PATH).exists()) {
			throw new IllegalStateException("Failed to find ffmpeg: " + new File(FFMPEG_PATH).getAbsolutePath());
		}
	}

	public static VideoMetadata extractMetadata(File srcMovieFile) throws IOException {
		Process process = new ProcessBuilder().command(//
		   FFMPEG_PATH, //
		   "-i", srcMovieFile.getAbsolutePath(),//
		   "-f", "null"//
		).start();
		Streams.asynchronousTransfer(process.getInputStream(), System.out, true, false);

		int width = -1;
		int height = -1;
		float framerate = -1;

		try {
			InputStream stderr = process.getErrorStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stderr));
			for (String line; (line = br.readLine()) != null;) {

				if (line.trim().startsWith("Stream #") && line.contains("Video:")) {
					framerate = Float.parseFloat(RegexUtil.findFirst(line, Pattern.compile("\\s(\\d+(\\.\\d+)?)\\stbr,"), 1));
					int[] wh = TextValues.parseInts(RegexUtil.find(line, Pattern.compile("\\s(\\d+)x(\\d+)[\\s,]"), 1, 2));
					width = wh[0];
					height = wh[1];
				}
			}

			if (framerate == -1) {
				throw new IllegalStateException("failed to find framerate of video");
			}
			return new VideoMetadata(width, height, framerate);
		} finally {
			Streams.safeClose(process);
		}
	}

	public static InputStream extractVideoAsRGB24(File srcMovieFile, int seconds) throws IOException {
		return streamData(new ProcessBuilder().command(//
		   FFMPEG_PATH, //
		   "-ss", String.valueOf(seconds), //
		   "-i", srcMovieFile.getAbsolutePath(), //		   
		   "-f", "rawvideo", //
		   "-pix_fmt", "rgb24", //
		   "-" //
		));
	}

	//

	public static InputStream extractAudioAsWAV(File srcMovieFile, int seconds) throws IOException {
		return streamData(new ProcessBuilder().command(//
		   FFMPEG_PATH, //
		   "-ss", String.valueOf(seconds), //
		   "-i", srcMovieFile.getAbsolutePath(), //
		   "-acodec", "pcm_s16le", //
		   "-ac", "2", //		    
		   "-f", "wav", //
		   "-" //
		));
	}

	//

	private static InputStream streamData(ProcessBuilder pb) throws IOException {
		Process process = pb.start();
		Streams.asynchronousTransfer(process.getErrorStream(), FFMPEG_VERBOSE ? System.err : new NullOutputStream(), true, false);
		return process.getInputStream();
	}
}
