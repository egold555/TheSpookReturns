package org.golde.java.game.objects.player.items;

import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.item.Item;
import org.golde.java.game.renderEngine.Loader;
import org.lwjgl.util.vector.Vector3f;

public class FlashLight extends Item{

	public FlashLight(Loader loader, float scale) {
		super(loader, "flashlight", scale, "flashlight");
		addCollider(new CylinderCollider(new Vector3f(), 6, 5, 0));
	}

	@Override
	public String getName() {
		return "Police Flashlight";
	}

	@Override
	public String[] getPickupSounds() {
		return null;
	}

}
