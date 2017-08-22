package org.golde.java.game.objects.terrain.decoration;

import org.golde.java.game.Main;
import org.golde.java.game.audio.AudioMaster;
import org.golde.java.game.audio.Source;
import org.golde.java.game.helpers.JavaHelper;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.BoxCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.objects.player.EntityPlayer;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityPiano extends Entity{

	//public static final int SONG_ = 1;
	public static enum EnumSongs{
		WET_HANDS("Wet Hands"),
		ADAGIO("Adagio"),
		ALL_STAR("All Star"),
		CANON_IN_D("Canon in D"),
		JOHN_CENA("John Cena"),
		SPOOKY_SCARY_SKELETONS("Spooky Scary Skeletons"),
		THE_ENTERTAINER("The Entertainer"),
		WE_ARE_NUMBER_ONE("We Are Number One"),
		CASIN("Casin"),
		_RANDOM(null),
		_NONE(null),
		;

		public final String mp3;
		public int songID = -1;
		EnumSongs(String theMp3){
			this.mp3 = "piano/" + theMp3; 
		}

	}

	Source sfx = new Source();
	private EnumSongs songToPlay;

	public EntityPiano(Loader loader, float x, float z, Terrain terrain, float scale, EnumSongs songToPlay) {
		super(getModel(loader), new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, scale);
		float y = terrain.getHeightOfTerrain(x, z);

		Vector3f start = new Vector3f(-1, 0, -2.5f);
		Vector3f end = new Vector3f(1, 4, 2.5f);
		this.addCollider(new BoxCollider(start, end));

		sfx.setLooping(false);
		sfx.setPosition(x, y, z);
		this.songToPlay = songToPlay;

		if(EnumSongs._NONE.songID == -1) {
			for(EnumSongs song:EnumSongs.values()) {
				if(song == EnumSongs._NONE || song == EnumSongs._RANDOM) {
					song.songID = -2;
					continue;
				}else {
					song.songID = AudioMaster.loadSound(song.mp3);
				}

			}

		}
		this.setAmbientLighting(0.2f);
	}
	
	static TexturedModel model;
	
	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/piano", loader), new ModelTexture(loader.loadTexture("models/piano")));
		}
		return model;
	}

	private EnumSongs lastSong = EnumSongs._NONE;
	private EnumSongs randomSong() {
		EnumSongs toReturn = EnumSongs._NONE;
		while(toReturn == lastSong) {
			toReturn = JavaHelper.randomEnum(EnumSongs.class);
		}
		lastSong = toReturn;
		return toReturn;
	}

	@Override
	public void onCollision(Entity collidedWith) {
		if(collidedWith instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer)collidedWith;
			if(!sfx.isPlaying()) {
				if(songToPlay == EnumSongs._NONE) {return;}

				if(songToPlay == EnumSongs._RANDOM) {
					songToPlay = randomSong();
				}
				
				sfx.play(songToPlay.songID);


				if(songToPlay == EnumSongs.WET_HANDS) {
					player.getGuiOverlay().chat.rainbow = true;
					player.sendChat("Now Playing: C418 - Wet Hands", 0.001f);
				}
				if(songToPlay == EnumSongs.CASIN) {
					Main.getScheduler().runTaskLater(6250, new Runnable() {

						@Override
						public void run() {
							player.sendChat("Two to the one from the one to the three...");
						}

					});
				}
				
			}
		}
	}

}
