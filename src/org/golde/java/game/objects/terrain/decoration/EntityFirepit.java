package org.golde.java.game.objects.terrain.decoration;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityFirepit extends Entity{
	
	//ParticleSystem psystem;
	
	public EntityFirepit(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(getModel(loader), new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, scale);
		
		/*psystem = new ParticleSystem(new ParticleTexture(loader.loadTexture("particles/fire"), 5), 10, 0, -0.3f, 4, 5);
		psystem.randomizeRotation();
		psystem.setLifeError(0.1f);
		psystem.setScaleError(0.4f);
		psystem.setScaleError(0.8f);*/
	}
	
	static TexturedModel model;
	
	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/firepit", loader), new ModelTexture(loader.loadTexture("models/firepit")));
		}
		return model;
	}
	
	@Override
	public void onRender() {
		//psystem.generateParticles(getPosition());
		super.onRender();
	}
	
}
