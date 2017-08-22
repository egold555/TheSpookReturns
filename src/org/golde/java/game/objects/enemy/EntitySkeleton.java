package org.golde.java.game.objects.enemy;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.behavior.BehaviorLookAtPlayer;
import org.golde.java.game.objects.base.behavior.BehaviorWalkToPlayer;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.objects.base.entities.EntityMoveable;
import org.golde.java.game.objects.player.EntityBullet;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.renderEngine.particles.ParticleSystem;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.model.ModelTexture;
import org.golde.java.game.textures.particles.ParticleTexture;
import org.lwjgl.util.vector.Vector3f;

public class EntitySkeleton extends EntityMoveable {

	ParticleSystem psystem = null;

	public EntitySkeleton(Loader loader, float x, float z, Terrain terrain, float scale) {
		super(getModel(loader), new Vector3f(x, terrain.getHeightOfTerrain(x, z) + 7, z), 0, 0, 0, scale);
		//this.getModel().getTexture().setUseFakeLightning(true);

		addCollider(new CylinderCollider(new Vector3f(), 8, 80, 0));

		this.addBehavior(new BehaviorLookAtPlayer(10));
		this.addBehavior(new BehaviorWalkToPlayer(10, true));
		psystem = new ParticleSystem(new ParticleTexture(loader.loadTexture("particles/explosion"), 5), 500, 25, 0.3f, 4, 100);
		psystem.randomizeRotation();
		psystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
		psystem.setLifeError(0.1f);
		psystem.setScaleError(0.4f);
		psystem.setScaleError(0.8f);

	}
	
	static TexturedModel model;
	
	static TexturedModel getModel(Loader loader)
	{
		if (model == null) {
			model = new TexturedModel(OBJLoader.loadObjectModel("models/bakedSkeleton", loader), new ModelTexture(loader.loadTexture("models/bakedSkeleton")));
		}
		return model;
	}

	@Override
	public void onCollision(Entity collidedWith) {
		if(collidedWith instanceof EntityBullet) {
			damage(1);
		}
		super.onCollision(collidedWith);
	} 

	@Override
	public int getMaxHealth() {
		return 20;
	}

	@Override
	public void onDeath() {
		psystem.generateParticles(this.getPosition());
		remove();
	}

}
