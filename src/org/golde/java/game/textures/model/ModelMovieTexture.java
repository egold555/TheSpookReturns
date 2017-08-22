package org.golde.java.game.textures.model;

import java.io.IOException;

import org.golde.java.game.GLog;
import org.golde.java.game.helpers.movieplayer.MoviePlayer;
import org.lwjgl.util.vector.Vector3f;

public class ModelMovieTexture extends ModelTexture{

	private MoviePlayer mp;
	public ModelMovieTexture(String movieFile) {
		
		try {
			mp = new MoviePlayer(movieFile);
		} catch (IOException e) {
			GLog.error("res/movies/" + movieFile + " Does not exist!");
		}
	}
	
	public void setMoviePlayer(MoviePlayer mp) {
		this.mp = mp;
	}
	
	public MoviePlayer getMoviePlayer() {
		return mp;
	}
	
	public void play() {
		mp.resume();
	}
	
	public void pause() {
		mp.pause();
	}
	
	public void stop() {
		mp.stop();
	}
	
	public boolean isPlaying() {
		return mp.isPlaying();
	}
	
	@Override
	public int getTextureID() {
		mp.tick();
		mp.syncTexture(5);
		return mp.textureHandle;
	}
	
	public void setPosition(Vector3f pos)
	{
		mp.setPosition(pos);
	}
}
