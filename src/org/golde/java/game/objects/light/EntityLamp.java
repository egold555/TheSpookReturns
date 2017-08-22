package org.golde.java.game.objects.light;

import org.golde.java.game.Main;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityLamp extends Entity{

	final float UNSCALED_RADIUS = 0.5f;
	final float UNSCALED_HEIGHT = 14;
	
	private static final float LIGHT_PLUS_POS = 12.8f;
	private Light light;
	
	
	public EntityLamp(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(new TexturedModel(OBJLoader.loadObjectModel("models/lamp", loader), new ModelTexture(loader.loadTexture("models/lamp"))), new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, scale);
		this.getModel().getTexture().setUseFakeLightning(true);
		this.getModel().getTexture().setHasTransparency(true);
		float y = terrain.getHeightOfTerrain(x, z);
		light = new Light(new Vector3f(x, y +  LIGHT_PLUS_POS, z), new Vector3f(2, 2, 0), new Vector3f(0.5f, 0.01f, 0.001f));
		
		this.addCollider(new CylinderCollider(new Vector3f(0, 0, 0), UNSCALED_RADIUS, UNSCALED_HEIGHT, 0));
		Main.getLights().add(light);
	}
	
	@Override
	public Entity setPosition(Vector3f position) {
		light.setPosition(new Vector3f(position.x, position.y + LIGHT_PLUS_POS, position.z));
		return super.setPosition(position);
	}
	
	public EntityLamp setColor(Vector3f color) {
		light.setColor(color);
		return this;
	}
	
	public EntityLamp setAttenuation(float att1, float att2, float att3) 
	{
		light.setAttenuation(att1, att2, att3);
		return this;
	}
	
	public EntityLamp setSpotLight(Vector3f dir, float innerWidth, float outerWidth)
	{
		light.setSpotLight(dir, innerWidth, outerWidth);
		return this;
	}
	
	@Override
	public void remove() {
		Main.getLights().remove(light);
		super.remove();
	}
	
}
