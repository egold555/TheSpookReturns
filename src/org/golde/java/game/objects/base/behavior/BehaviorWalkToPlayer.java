package org.golde.java.game.objects.base.behavior;

import org.golde.java.game.helpers.Maths;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.objects.base.entities.EntityMoveable;

public class BehaviorWalkToPlayer extends Behavior{

	private final float walkSpeed;
	private final boolean mustBeLookingAt;
	
	public BehaviorWalkToPlayer(final float walkSpeed, boolean mustBeLookingAt) {
		this.walkSpeed = walkSpeed;
		this.mustBeLookingAt = mustBeLookingAt;
	}
	
	@Override
	public void onRender(final Entity entity) {
		if (! (entity instanceof EntityMoveable))
			return;
		
		if (mustBeLookingAt) {
			float angleToPlayer = (float) Math.toDegrees(Math.atan2(player.getPosition().x- entity.getPosition().x, player.getPosition().z - entity.getPosition().z));
			float currentAngle = entity.getRotY();
			
			while (Math.abs(angleToPlayer - currentAngle) > 180)
			{
				if (angleToPlayer < currentAngle)
					angleToPlayer += 360;
				else if (angleToPlayer > currentAngle)
					angleToPlayer -= 360;
			}
			
			if (Math.abs(angleToPlayer - currentAngle) > 15) {
				return;
			}
		}
		
		float deltaX = player.getPosition().x - entity.getPosition().x;
		float deltaZ = player.getPosition().z - entity.getPosition().z;
		float distance = Maths.distance(player.getPosition().x, player.getPosition().z, entity.getPosition().x, entity.getPosition().z);
		
		float velX, velZ;
		if (distance < 0.1) {
			velX = 0;
			velZ = 0;
		}
		else {
			velX = walkSpeed * (deltaX / distance);
			velZ = walkSpeed * (deltaZ / distance);
		}
		
		
		EntityMoveable moveable = ((EntityMoveable)entity);
		moveable.setVelocity(velX, 0, velZ);
	}

}
