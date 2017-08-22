package org.golde.java.game.objects.terrain.structures;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.BoxCollider;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityFarmHouse extends Entity{

	public EntityFarmHouse(Loader loader, float x, float z, Terrain terrain) {
		this(loader, x, z, terrain, 1);
	}
	
	public EntityFarmHouse(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(new TexturedModel(OBJLoader.loadObjectModel("models/farmhouse", loader), new ModelTexture(loader.loadTexture("models/farmhouse"))), new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, scale);
		
		Vector3f start = new Vector3f(-9, 0, -8);
		Vector3f end = new Vector3f(9, 22, 28);
		this.addCollider(new BoxCollider(start, end));
		
		start = new Vector3f(-9.6F, 0, -19.6F);
		end = new Vector3f(9.6F, 1.3F, -8);
		this.addCollider(new BoxCollider(start, end));
		
		Vector3f post1 = new Vector3f(-7.9F, 0, -15);
		Vector3f post2 = new Vector3f(7.9F, 0, -15);

		this.addCollider(new CylinderCollider(post1, 01f, 22, 0));
		this.addCollider(new CylinderCollider(post2, 01f, 22, 0));
		this.getModel().getTexture().setReflectivity(0.8f);

	}
	
}
