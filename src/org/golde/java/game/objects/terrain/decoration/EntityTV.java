package org.golde.java.game.objects.terrain.decoration;

import org.golde.java.game.Main;
import org.golde.java.game.helpers.movieplayer.MoviePlayer;
import org.golde.java.game.models.RawModel;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.BoxCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.objects.player.EntityPlayer;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelMovieTexture;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityTV extends Entity{

	EntityTVScreen tvScreen;
	boolean isRunningAnimation = false;
	MoviePlayer mp;

	public EntityTV(Loader loader,float x, float z, Terrain terrain, float scale, String movie) {
		super(getModel(loader), new Vector3f(x, terrain.getHeightOfTerrain(x, z) + 6, z), -90, 0, 90, scale);

		float y = terrain.getHeightOfTerrain(x, z) + 6;

		tvScreen = new EntityTVScreen(loader, x + 0.01f, z, terrain, scale, movie);
		mp = ((ModelMovieTexture)(tvScreen.getModel().getTexture())).getMoviePlayer();
		mp.setPosition(new Vector3f(x,y,z));

		Vector3f start = new Vector3f(-4, -5, -6);
		Vector3f end = new Vector3f(4, 5, 6);
		this.addCollider(new BoxCollider(start, end));
	}

	static TexturedModel model;

	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/tv/tv", loader), new ModelTexture(loader.loadTexture("models/tv/tv")));
		}
		return model;
	}

	@Override
	public void onCollision(Entity collidedWith) {

		if(collidedWith instanceof EntityPlayer) {
			turnTVOn();
		}

		super.onCollision(collidedWith);
	}


	private void turnTVOn() {
		if(isRunningAnimation) {return;}

		mp.setPosition(new Vector3f(getPosition()));
		mp.resume();
		if (!Main.getEntities().contains(tvScreen)) {
			Main.getEntities().add(tvScreen);
		}
		isRunningAnimation = true;
	}

	@Override
	public void onRender() {
		if(!mp.isPlaying() && isRunningAnimation == true) {
			Main.getEntities().remove(tvScreen);
			isRunningAnimation = false;
			mp.resetToBeginning();
		}
		super.onRender();
	}

}


class EntityTVScreen extends Entity {

	public EntityTVScreen(Loader loader,float x, float z, Terrain terrain, float scale, String movie) {
		super(new TexturedModel(getModel(loader), new ModelMovieTexture(movie)), new Vector3f(x, terrain.getHeightOfTerrain(x, z) + 6, z), -90, 0, 90, scale);
	}

	static RawModel model;

	static RawModel getModel(Loader loader)
	{
		if (model == null) {
			model = OBJLoader.loadObjectModel("models/tv/screen", loader);
		}
		return model;
	}

}