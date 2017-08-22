package org.golde.java.game.objects.terrain.structures;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelAnimatedTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityChurch extends Entity{

	public EntityChurch(Loader loader, float x, float z, Terrain terrain) {
		this(loader, x, z, terrain, 1);
	}
	
	public EntityChurch(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(new TexturedModel(OBJLoader.loadObjectModel("models/church", loader), new ModelAnimatedTexture(loader, "models/churchImage", 20, false)), new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, scale);
		
		//float y = terrain.getHeightOfTerrain(x, z);

	}
	
}
