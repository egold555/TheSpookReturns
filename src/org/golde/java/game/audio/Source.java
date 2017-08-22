package org.golde.java.game.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

/**
 * Essentally a placiable speaker.
 * @author Eric
 *
 */
public class Source {

	private int sourceId;
	Vector3f pos;
	Vector3f direction;
	
	public Source() {
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 0.5f);
		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 6);
		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 30);
		AudioMaster.sources.add(this);
		setPosition(new Vector3f(0, 0, 0));
	}
	
	/*public void delete() {
		stop();
		AL10.alDeleteSources(sourceId);
	}*/
	
	public void play(int buffer) {
		stop();
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		unpause();
	}
	
	public void setVolume(float volume) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	public Vector3f getPosition() {
		return pos;
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	
	public void setPosition(float x, float y, float z) {
		setPosition(new Vector3f(x, y, z));
	}
	
	public void setPosition(Vector3f newPos) {
		this.pos = newPos;
		AL10.alSource3f(sourceId, AL10.AL_POSITION, newPos.x, newPos.y, newPos.z);
	}
	
	public void setDirection(float x, float y, float z) {
		setDirection(new Vector3f(x, y, z));
	}
	
	public void setDirection(Vector3f newDirection) {
		this.direction = newDirection;
		AL10.alSource3f(sourceId, AL10.AL_DIRECTION, newDirection.x, newDirection.y, newDirection.z);
	}
	
	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}
	
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE:AL10.AL_FALSE);
	}
	
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void pause() {
		AL10.alSourcePause(sourceId);
	}
	
	public void unpause() {
		AL10.alSourcePlay(sourceId);
	}
	
	public void stop() {
		AL10.alSourceStop(sourceId);
	}
	
	float fadeOutTime = -1;
	
	public void fadeOut(float amountOfSeconds) {
		fadeOutTime = amountOfSeconds;
		if(amountOfSeconds <=0) {
			amountOfSeconds = 20;
		}
	}
	
	public void tick() {
		if(fadeOutTime == -1) {return;}
		setPosition(pos.x, pos.y +fadeOutTime, pos.z);
		
		if(pos.y >= 100) {
			//delete();
			stop();
		}
	}
	
}
