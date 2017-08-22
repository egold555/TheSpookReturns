package org.golde.java.game.objects.enemy;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.audio.AudioMaster;
import org.golde.java.game.audio.Source;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.objects.base.entities.EntityEnemy;
import org.golde.java.game.objects.player.EntityPlayer;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntitySlasher extends EntityEnemy{

	Source sfx = new Source();
	static List<Integer> sounds = new ArrayList<Integer>();

	public EntitySlasher(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(getModel(loader), new Vector3f(x, terrain.getHeightOfTerrain(x, z) + 7, z), 0, 0, 0, scale);
		float y = terrain.getHeightOfTerrain(x, z);
		addCollider(new CylinderCollider(new Vector3f(0, 0, 0), 1, 2, 0));
		sfx.setLooping(false);
		sfx.setPosition(x, y, z);
		if(sounds.size() == 0) {
			sounds.add(AudioMaster.loadSound("slasher/girl"));
			sounds.add(AudioMaster.loadSound("slasher/glitch"));
			sounds.add(AudioMaster.loadSound("slasher/slime"));
		}
		addCollider(new CylinderCollider(new Vector3f(0, 0, 0), 1, 1, 0.7F));
		this.setHeightOffGround(scale * 0.9f);
	}

	static TexturedModel model;

	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/slasher", loader), new ModelTexture(loader.loadTexture("models/slasher")));
		}
		return model;
	}

	private int lastSong = -1;
	private int randomSound() {
		int song = sounds.get(rand.nextInt(sounds.size()));
		while(song == lastSong) {
			song = sounds.get(rand.nextInt(sounds.size()));
		}
		lastSong = song;
		return song;
	}

	@Override
	public void onCollision(Entity collidedWith) {
		if(collidedWith instanceof EntityPlayer) {
			if(!sfx.isPlaying()) {
				sfx.play(randomSound());
			}
		}
	}

	@Override
	public int getMaxHealth() {
		return 40;
	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub

	}

}
