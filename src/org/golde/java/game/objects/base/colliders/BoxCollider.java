package org.golde.java.game.objects.base.colliders;

import org.golde.java.game.helpers.Maths;
import org.lwjgl.util.vector.Vector3f;

public class BoxCollider extends Collider{

	private Vector3f startPoint;
	private Vector3f endPoint;
	
	public BoxCollider(Vector3f startPoint, Vector3f endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	@Override
	public void scaleAndTranslate(float scale, float x, float y, float z) {
		this.startPoint.scale(scale);
	    this.startPoint.translate(x, y, z);
		this.endPoint.scale(scale);
	    this.endPoint.translate(x, y, z);	
	}
	
	@Override
	public boolean collidesWith(Collider other) {
		if (other instanceof CylinderCollider) {
			CylinderCollider otherCyl = (CylinderCollider) other;
			
			float startX = startPoint.x, startZ = startPoint.z, endX = endPoint.x, endZ = endPoint.z;
			float otherX = otherCyl.getBottomCenter().x, otherZ = otherCyl.getBottomCenter().z;
			float otherRad = otherCyl.getRadius();
			
			if (otherX > startX && otherX < endX) {
				if (otherZ + otherRad < startZ) {
					return false;
				}
				else if (otherZ - otherRad > endZ) {
					return false;
				}
			}
			else if (otherZ > startZ && otherZ < endZ) {
				if (otherX + otherRad < startX) {
					return false;
				}
				else if (otherX - otherRad > endX) {
					return false;
				}
			}
			else if (otherX > endX && otherZ > endZ) {
				if (Maths.distance(otherX, otherZ, endX, endZ) > otherRad) {
					return false;
				}
			}
			else if (otherX < startX && otherZ > endZ) {
				if (Maths.distance(otherX, otherZ, startX, endZ) > otherRad) {
					return false;
				}
			}
			else if (otherX > endX && otherZ < startZ) {
				if (Maths.distance(otherX, otherZ, endX, startZ) > otherRad) {
					return false;
				}
			}
			else if (otherX < startX && otherZ < startX) {
				if (Maths.distance(otherX, otherZ, startX, startZ) > otherRad) {
					return false;
				}
			}
			else {
				return false;
			}
			
			//  We need to figure out if the heights overlap.
			if (startPoint.y > otherCyl.getTop())
				return false;
			if (otherCyl.getBottom() > endPoint.y)
				return false;
			
			return true;
		}
		else if (other instanceof BoxCollider) {
			BoxCollider otherBox = (BoxCollider) other;
			
			if (otherBox.startPoint.x > this.endPoint.x)
				return false;
			if (otherBox.endPoint.x < this.startPoint.x)
				return false;
			if (otherBox.startPoint.y > this.endPoint.y)
				return false;
			if (otherBox.endPoint.y < this.startPoint.y)
				return false;
			if (otherBox.startPoint.z > this.endPoint.z)
				return false;
			if (otherBox.endPoint.z < this.startPoint.z)
				return false;
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public Vector3f getEndPoint() {
		return endPoint;
	}
	
	public Vector3f getStartPoint() {
		return startPoint;
	}
	
	public float getTop() {
		return endPoint.y;
	}

}
