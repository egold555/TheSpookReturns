package org.golde.java.game.objects.base.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.golde.java.game.Main;
import org.golde.java.game.helpers.Maths;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.behavior.Behavior;
import org.golde.java.game.objects.base.colliders.Collider;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private TexturedModel model;
	protected Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	public Random rand = new Random();
	private List<Collider> colliders = new ArrayList<Collider>();
	private float ambientLighting = 0.5f;
	private List<Behavior> behaviors = new ArrayList<Behavior>();
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Entity(TexturedModel model) {
		this.model = model;
		this.position = new Vector3f();
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
		this.scale = 1;
	}
	
	public Entity increasePosition(float dx, float dy, float dz) {
		if (position != null) {
			this.setPosition(new Vector3f(position.x + dx, position.y + dy, position.z + dz));
		}
		return this;
	}
	
	public Entity increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
		
		if(this.rotX > 360) {
			this.rotX -= 360;
		}
		
		if(this.rotX < 0) {
			this.rotX += 360;
		}
		return this;
	}
	
	public void addBehavior(Behavior behavior) {
		behaviors.add(behavior);
	}
	
	public List<Collider> getColliders() {
		return colliders;
	}
	
	public void addCollider(Collider collider) {
		if (position != null) {
			collider.scaleAndTranslate(this.getScale(), this.position.x, this.position.y, this.position.z);
		}
		else {
			collider.scaleAndTranslate(this.getScale(), 0, 0, 0);
		}

		colliders.add(collider);
	}
	
	public boolean removeCollider(Collider collider) {
		return colliders.remove(collider);
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public Entity setModel(TexturedModel model) {
		this.model = model;
		return this;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return new Vector3f(rotX, rotY, rotZ);
	}
	
	public Entity setPosition(float x, float y, float z)
	{
		return setPosition(new Vector3f(x, y, z));
	}
	
	public Entity setPosition(Vector3f newPos) {
		Vector3f oldPosition = (position == null) ? new Vector3f() : position;
		Vector3f newPosition = (newPos == null) ? new Vector3f() : newPos;
		float dx = newPosition.x - oldPosition.x;
		float dy = newPosition.y - oldPosition.y;
		float dz = newPosition.z - oldPosition.z;
		this.position = newPos;
		
		for (Collider collider: this.colliders) {
			collider.scaleAndTranslate(1, dx,  dy, dz);
		}
		return this;
	}
	
	public float getRotX() {
		return rotX;
	}
	
	public Entity setRotX(float rotX) {
		this.rotX = rotX;
		return this;
	}
	
	public float getRotY() {
		return rotY;
	}
	
	public Entity setRotY(float rotY) {
		this.rotY = rotY;
		return this;
	}
	
	public float getRotZ() {
		return rotZ;
	}
	
	public Entity setRotZ(float rotZ) {
		this.rotZ = rotZ;
		return this;
	}
	
	public float getScale() {
		return scale;
	}
	
	public Entity setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	public void onCollision(Entity collidedWith) {
		for(Behavior behavior:behaviors) {
			behavior.onCollision(collidedWith);
		}
	}
	
	public void onRender() {
		if(model == null || model.getTexture() == null) {return;}
		model.getTexture().onRender();
		for(Behavior behavior:behaviors) {
			behavior.onRender(this);
		}
	};
	
	public void remove() {
		Main.getEntities().remove(this);
	}

	public float getAmbientLighting() {
		return ambientLighting;
	}
	
	public void setAmbientLighting(float ambientLighting) {
		this.ambientLighting = ambientLighting;
	}
	
	public Matrix4f getTransformationMatrix()
	{
		return Maths.createTransformationMatrix(getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
	}
}
