package org.golde.java.game.objects.terrain.plants;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityGrass extends Entity{

	public EntityGrass(Loader loader, float x, float z, Terrain terrain) {
		this(loader, x, z, terrain, 1);
	}

	public EntityGrass(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(getModel(loader), new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, scale);
		this.getModel().getTexture().setUseFakeLightning(true);
		this.getModel().getTexture().setHasTransparency(true);
	}

	static TexturedModel model;

	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/grass", loader), new ModelTexture(loader.loadTexture("models/grass")));
		}
		return model;
	}

}
