package org.golde.java.game.objects.base.item;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.audio.AudioMaster;
import org.golde.java.game.helpers.JavaHelper;
import org.golde.java.game.objects.player.items.guns.ItemAmmoBox.EnumAmmoBoxType;
import org.golde.java.game.renderEngine.Loader;

public abstract class ItemGun extends Item{

	public abstract String[] getShootSound();
	public abstract float getReloadCooldownMS();
	public abstract float getFireBulletCooldownMS();
	public abstract float getFireBulletAmount();
	public abstract int getMaxAmmo();
	
	private List<Integer> shootSounds = new ArrayList<Integer>();
	protected int currentAmmo = 0;
	
	public ItemGun(Loader loader, String modelAndTexture, float scale, EnumAmmoBoxType ammoBoxType) {
		super(loader, "guns/" + modelAndTexture, scale);
		
		if(getPickupSounds() != null) {
			for(String s:getPickupSounds()) {
				if(JavaHelper.isStringEmpty(s)) {
					continue;
				}
				shootSounds.add(AudioMaster.loadSound("player/items/guns/" + s));
			}
		}
	}
	
	@Override
	public void onRender() {
		
		super.onRender();
	}

}
