package org.golde.java.game.objects.base.behavior;

import org.golde.java.game.Main;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.objects.player.EntityPlayer;

public abstract class Behavior {
	
	protected EntityPlayer player = Main.getPlayer();
	
	public abstract void onRender(final Entity entity);
	
	public void onCollision(Entity collidedWith) {};
	
}
