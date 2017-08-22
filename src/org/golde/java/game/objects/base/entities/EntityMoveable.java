package org.golde.java.game.objects.base.entities;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.GLog;
import org.golde.java.game.Main;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.Collider;
import org.golde.java.game.renderEngine.DisplayManager;
import org.golde.java.game.terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;

public abstract class EntityMoveable extends Entity{
	public static final float GRAVITY = -50;

	private boolean hasGravity = true;

	protected boolean isInAir = false;
    private Vector3f velocity = new Vector3f();
	private Terrain terrainCurrentlyStandingOn;
	private float deep = 0;
	
	private int health = getMaxHealth();
	
	public EntityMoveable(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	
	public abstract int getMaxHealth();
	public int getHealth() {return health;}
	public abstract void onDeath();
	public void damage(int damage) {
		if(getMaxHealth() < 0) {return;}
		health -= damage;
		GLog.info("[" + this.getClass().getName() + "] Took " + damage + " damage | Health: " + health);
		if(health <= 0) {
			onDeath();
		}
	}
	
	public void setHeightOffGround(float deep) {
		this.deep = deep;
	}
	
	public Vector3f getVelocity()
	{
		return velocity;
	}
	
	public void setVelocity(Vector3f vel) {
		velocity = new Vector3f(vel);
	}
	
	public void setVelocity(float x, float y, float z)
	{
		velocity.x = x;
		velocity.y = y;
		velocity.z = z;
	}
	
	public void addVelocity(float dx, float dy, float dz)
	{
		velocity.x += dx;
		velocity.y += dy;
		velocity.z += dz;
	}
	
	public boolean hasGravity() {
		return hasGravity;
	}
	
	public void setHasGravity(boolean hasGravity) {
		this.hasGravity = hasGravity;
	}
	
	public boolean isInAir() {
		return isInAir;
	}

	@Override
	public void onRender() {
		super.onRender();
	}
	
	public void move(Terrain terrain) {
		if(getPosition() == null) {return;} //Items

		terrainCurrentlyStandingOn = terrain;
		float frameTime= DisplayManager.getFrameTimeSeconds();
        Vector3f curPos = getPosition();
        
		float dx = velocity.x * frameTime;
		float dz = velocity.z * frameTime;

		List<Entity> collidedWith = getIntersectingEntities(curPos.x + dx, curPos.y, curPos.z + dz);

		if (collidedWith == null || !isBlockingEntity(collidedWith)) {

			super.increasePosition(dx, 0, dz);
			curPos = getPosition();
		}

		
		if(collidedWith != null) {
			notifyEntitiesOfCollision(collidedWith, this);
		}

		if(getPosition() == null) {return;} //Items
		
		if (hasGravity) {
			velocity.y += GRAVITY * frameTime;
		}
		
		float dy = velocity.y * frameTime;
		
		if (dy > 0) {
			isInAir = true;
		}
		
		List<Collider> floor = getIntersectingColliders(curPos.x, curPos.y + dy, curPos.z);

		if (floor == null || !isBlockingCollider(floor)) {
			super.increasePosition(0, dy, 0);
			curPos = getPosition();
			float terrainHeight = terrain.getHeightOfTerrain(curPos.x, curPos.z);
			if(curPos.y < terrainHeight + deep) {
				velocity.y = 0;
				super.setPosition(position.x, terrainHeight + deep, position.z);
				isInAir = false;
			}
		}
		else {
			velocity.y = 0;
			super.setPosition(position.x, floor.get(0).getTop() + deep + 0.001f, position.z);
			isInAir = false;			
		}


	}
	
	public boolean isMoving() {
		return velocity.x != 0 || velocity.y != 0 || velocity.z != 0;
	}
	
	/*
	public Collider getCollider(float x, float y, float z)
	{
		Collider collider = new CylinderCollider(new Vector3f(x, y - deep, z), radius, high + deep);
		collider.blocks = this.blocksMovement;
		return collider;
	}
	
	@Override
	public List<Collider> getColliders() {
		Vector3f pos = getPosition();
		
		List<Collider> list = new ArrayList<Collider>();
		list.add(getCollider(pos.x, pos.y, pos.z));
		return list;
	}

*/
	private void notifyEntitiesOfCollision(List<Entity> entities, Entity collidedWith)
	{
		for (Entity e: entities) {
			e.onCollision(collidedWith);
			this.onCollision(e);
		}
	}

	private List<Entity> getIntersectingEntities(float x, float y, float z)
	{
		float dx = x - position.x, dy = y - position.y, dz = z - position.z;
		List<Entity> collideWith = null;
		
		for (Collider myCollider: getColliders()) {
			myCollider.scaleAndTranslate(1, dx, dy, dz);
			for (Entity entity: Main.getEntities())
			{
				if (entity != this && entity.getPosition() != null) {
					for (Collider other: entity.getColliders()) {
						if (myCollider.collidesWith(other)) {
							if (collideWith == null)
								collideWith = new ArrayList<Entity>();
							collideWith.add(entity);
						}
					}
				}
			}
			myCollider.scaleAndTranslate(1, -dx, -dy, -dz);
		}

		return collideWith;
	}
	
	private List<Collider> getIntersectingColliders(float x, float y, float z)
	{
		float dx = x - position.x, dy = y - position.y, dz = z - position.z;
		
		List<Collider> collideWith = null;

		for (Collider myCollider: getColliders()) {
			myCollider.scaleAndTranslate(1, dx, dy, dz);
			for (Entity entity: Main.getEntities())
			{
				if (entity != this && entity.getPosition() != null) {
					for (Collider other: entity.getColliders()) {
						if (myCollider.collidesWith(other)) {
							if (collideWith == null)
								collideWith = new ArrayList<Collider>();
							collideWith.add(other);
						}
					}
				}
			}
			myCollider.scaleAndTranslate(1, -dx, -dy, -dz);
		}
		return collideWith;
	}

	public Terrain getTerrainCurrentlyStandingOn() {
		return terrainCurrentlyStandingOn;
	}
	
	boolean isBlockingEntity(List<Entity> entities) {
		for(Entity e:entities) {
			if(isBlockingCollider(e.getColliders())) {
				return true;
			}
		}
		return false;
	}
	
	boolean isBlockingCollider(List<Collider> colliders) {
			for(Collider c:colliders) {
				if(c.blocks) {
					return true;
				}
			}
		return false;
	}

}
