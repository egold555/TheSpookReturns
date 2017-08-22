package org.golde.java.game.objects.base.colliders;

public abstract class Collider {

	public abstract boolean collidesWith(Collider other);
	
	public abstract float getTop();
	
	public abstract void scaleAndTranslate(float scale, float x, float y, float z);
	
	public boolean blocks = true;
	
}
