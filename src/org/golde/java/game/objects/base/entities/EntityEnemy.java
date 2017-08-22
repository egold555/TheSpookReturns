package org.golde.java.game.objects.base.entities;

import org.golde.java.game.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;


public abstract class EntityEnemy extends EntityMoveable{

	public EntityEnemy(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.setAmbientLighting(0.7f);
	}

}
