package org.golde.java.game.objects.terrain.plants;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityTree extends Entity {

	final float UNSCALED_RADIUS = 0.6f;
	final float UNSCALED_HEIGHT = 2.4F;

	public EntityTree(Loader loader, float x, float z, Terrain terrain) {
		this(loader, x, z, terrain, 1);
	}

	public EntityTree(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(getModel(loader), new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, scale);
		this.addCollider(new CylinderCollider(new Vector3f(0, 0, 0), UNSCALED_RADIUS, UNSCALED_HEIGHT, 0));
	}

	static TexturedModel model;

	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/tree", loader), new ModelTexture(loader.loadTexture("models/tree")));
		}
		return model;
	}

}
