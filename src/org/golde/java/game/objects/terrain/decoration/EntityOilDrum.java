package org.golde.java.game.objects.terrain.decoration;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityOilDrum extends Entity{
	
	private final float UNSCALED_R = 0.3f;
	private final float UNSCALED_H = 0.5f;
	
	
	public EntityOilDrum(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(getModel(loader), new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, scale);
		this.addCollider(new CylinderCollider(new Vector3f(0, 0, 0), UNSCALED_R, UNSCALED_H, 0));
	}
	
	static TexturedModel model;
	
	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/oildrum", loader), new ModelTexture(loader.loadTexture("models/oildrum")));
		}
		return model;
	}
	
}
