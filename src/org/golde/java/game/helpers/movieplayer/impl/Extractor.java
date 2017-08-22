package org.golde.java.game.helpers.movieplayer.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.golde.java.game.GLog;
import org.golde.java.game.helpers.movieplayer.craterstudio.Streams;
import org.golde.java.game.helpers.movieplayer.craterstudio.Text;

public class Extractor {

	public static final boolean isWindows, isMac, isLinux, is64bit;

	static {
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");

		isWindows = osName.contains("Windows");
		isMac = osName.contains("Mac");
		isLinux = !isWindows && !isMac;

		String bits = System.getProperty("sun.arch.data.model");
		if (bits != null) {
			is64bit = Integer.parseInt(bits) == 64;
		} else {
			is64bit = osArch.equals("amd64") || osArch.equals("x86_64");
		}
	}

	public static void extractNativeLibrary(String resourceName) throws IOException {
		String[] paths = Text.split(System.getProperty("java.library.path"), File.pathSeparatorChar);
		String path = paths[paths.length - 1];
		path += '/' + Text.afterLast(resourceName, '/');
		extractResource(resourceName, new File(path));
	}

	public static void extractResource(String resourceName, File dst) throws IOException {
		if (dst.exists()) {
			return;
		}

		File dir = dst.getParentFile();

		if (!dir.exists() && !dir.mkdirs()) {
			throw new IllegalStateException("failed to create dir: " + dir.getAbsolutePath());
		}

		InputStream in = FFmpeg.class.getResourceAsStream(resourceName);
		if (in == null) {
			throw new IllegalStateException("failed to find resource: " + resourceName);
		}

		GLog.info("Extracting " + resourceName + " to " + dst.getAbsolutePath());
		Streams.copy(new BufferedInputStream(in), new BufferedOutputStream(new FileOutputStream(dst)));
	}
}
