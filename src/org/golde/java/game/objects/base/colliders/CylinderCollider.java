package org.golde.java.game.objects.base.colliders;

import org.golde.java.game.helpers.Maths;
import org.lwjgl.util.vector.Vector3f;

public class CylinderCollider extends Collider{

	private Vector3f bottomCenter;
	private float radius;
	private float height;
	private float depth;
	
	public CylinderCollider(Vector3f bottomCenter, float radius, float height, float depth) {
		this.bottomCenter = bottomCenter;
		this.radius = radius;
		this.height = height;
		this.depth = depth;
	}
	
	@Override
	public void scaleAndTranslate(float scale, float x, float y, float z) {
		bottomCenter.scale(scale);
		bottomCenter.translate(x,  y,  z);
		radius *= scale;
		height *= scale;
		depth *= scale;
	}
	
	@Override
	public boolean collidesWith(Collider other) {
		if (other instanceof CylinderCollider) {
			CylinderCollider otherCyl = (CylinderCollider) other;
			
			float distBetween = Maths.distance(bottomCenter, otherCyl.bottomCenter);
			if (distBetween > radius + otherCyl.radius) {
				return false;
			}
			
			// Now we know that the circles overlap. We need to figure out if the heights overlap.
			if (getBottom() > otherCyl.getTop())
				return false;
			if (otherCyl.getBottom() > getTop())
				return false;
			
			return true;
		}
		else {
			return other.collidesWith(this);
		}
	}
	
	public Vector3f getBottomCenter() {
		return bottomCenter;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getDepth() {
		return depth;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public float getBottom() {
		return bottomCenter.y - depth;
	}
	
	public float getTop() {
		return bottomCenter.y + height;
	}

}
