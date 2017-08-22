package org.golde.java.game.objects.base.behavior;

import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.DisplayManager;

public class BehaviorLookAtPlayer extends Behavior{

	private float degreesPerSecond;
	private float offset = 0;
	
	public BehaviorLookAtPlayer(float degreesPerSecond) {
		
		if(degreesPerSecond < 0) {
			degreesPerSecond = 360 * 100;
		}
		this.degreesPerSecond = degreesPerSecond;
	}
	
	public BehaviorLookAtPlayer addOffset(float offset) {
		this.offset = offset;
		return this;
	}
	
	@Override
	public void onRender(final Entity entity) {
		float angleToPlayer = (float) Math.toDegrees(Math.atan2(player.getPosition().x- entity.getPosition().x, player.getPosition().z - entity.getPosition().z));
		float currentAngle = entity.getRotY();
		
		while (Math.abs(angleToPlayer - currentAngle) > 180)
		{
			if (angleToPlayer < currentAngle)
				angleToPlayer += 360;
			else if (angleToPlayer > currentAngle)
				angleToPlayer -= 360;
		}
		
		float newAngle = currentAngle;
		float maxChange = Math.abs(angleToPlayer - currentAngle);
		if (angleToPlayer > currentAngle) {
			newAngle = currentAngle + Math.min(maxChange, degreesPerSecond * DisplayManager.getFrameTimeSeconds());
		}
		else {
			newAngle = currentAngle - Math.min(maxChange, degreesPerSecond * DisplayManager.getFrameTimeSeconds());
		}
		
		entity.setRotY(newAngle + offset);
	}

}
