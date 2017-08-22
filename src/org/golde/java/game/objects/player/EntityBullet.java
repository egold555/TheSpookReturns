package org.golde.java.game.objects.player;

import org.golde.java.game.Main;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.Collider;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.objects.base.entities.EntityMoveable;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.renderEngine.renderers.SkyboxRenderer;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntityBullet extends EntityMoveable{
	private static final float BULLET_SPEED = 50;
	private static final float SCALE = 0.1f;
	
	Entity firedFrom;
	
	public EntityBullet(Loader loader, Entity p, float scale) {
		super(getModel(loader), new Vector3f(p.getPosition().x, p.getPosition().y + 4, p.getPosition().z), 0, 0, 0, SCALE);
		
		firedFrom = p;
		
		Main.getEntities().add(this);
		setHasGravity(false);
		Collider collider = new CylinderCollider(new Vector3f(), 0.01F, 0.01F, 0);
		collider.blocks = false;
		addCollider(collider);
		
		float rotX = (float) Math.toRadians(p.getRotX());
		float rotY = (float) Math.toRadians(p.getRotY());
		
		float velX = (float) (BULLET_SPEED * Math.sin(rotX) * Math.cos(rotY));
		float velZ = (float) (-BULLET_SPEED * Math.cos(rotX) * Math.cos(rotY));
		float velY = (float) (-BULLET_SPEED * Math.sin(rotY));
		
		setVelocity(velX, velY, velZ);
		
		setPosition(new Vector3f(p.getPosition().x + velX / 10, p.getPosition().y + 4, p.getPosition().z + velZ / 10));
		super.setRotY(- p.getRotX() + 90);
	}
	
	static TexturedModel model;

	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/firedBullet", loader), new ModelTexture(loader.loadTexture("models/firedBullet")));
		}
		return model;
	}
	
	@Override
	public void onRender() {
		//GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
		super.onRender();
	}

	@Override
	public void onCollision(Entity collidedWith) {
		if (collidedWith != firedFrom) {
			remove();
		}
	}
	
	@Override
	public void move(Terrain terrain) {
		super.move(terrain);
		if(Math.abs(getPosition().x) > SkyboxRenderer.SIZE || Math.abs(getPosition().y) > SkyboxRenderer.SIZE || Math.abs(getPosition().z) > SkyboxRenderer.SIZE) {
			remove();
		}
	}

	@Override
	public int getMaxHealth() {
		return -1;
	}

	@Override
	public void onDeath() {}

}
