package org.golde.java.game.objects.enemy;

import org.golde.java.game.models.RawModel;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityDog extends Entity{

	public EntityDog(Loader loader, int type, float x, float z, Terrain terrain) {
		this(loader, type, x, z, terrain, 1);
	}
	
	public EntityDog(Loader loader, int type, float x, float z, Terrain terrain, float scale) {
		super(new TexturedModel(OBJLoader.loadObjectModel("models/dog", loader), new ModelTexture(loader.loadTexture("models/dog_" + type))), new Vector3f(x, terrain.getHeightOfTerrain(x, z) -1, z), -90, 0, 90, scale);
		
		
		//float y = terrain.getHeightOfTerrain(x, z);
		//this.getModel().getTexture().setUseFakeLightning(true);
	}
	
	static RawModel model;
	
	static RawModel getModel(Loader loader)
	{
		if (model == null) {
			model = OBJLoader.loadObjectModel("models/dog", loader);
		}
		return model;
	}
	
}
