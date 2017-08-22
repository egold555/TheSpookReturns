package org.golde.java.game.objects.player.items.guns;

import org.golde.java.game.models.RawModel;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.item.Item;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.lwjgl.util.vector.Vector3f;

public class ItemAmmoBox extends Item{

	public enum EnumAmmoBoxType{
		
		_BLANK("null", 0, "blank"),
		ORIG("Orig", 10, "orig")
		//PISTOL("Pistol", 10, "pistol")
		
		;
		
		public final String name;
		public final int maxAmmo;
		public final String texture;
		EnumAmmoBoxType(String name, int maxAmmo, String texture){
			this.name = name;
			this.maxAmmo = maxAmmo;
			this.texture = texture;
		}
	}
	
	EnumAmmoBoxType boxType;
	private final int maxAmmo;
	private int ammoLeft;
	public ItemAmmoBox(Loader loader, EnumAmmoBoxType type) {
		super(loader, getModel(loader), "guns/AmmoBox/" + type.texture, 7, "ab" + type.ordinal());
		addCollider(new CylinderCollider(new Vector3f(), 3.0F/7F, 5f/7f, 0));
		this.boxType = type;
		this.maxAmmo = boxType.maxAmmo;
		this.ammoLeft = this.maxAmmo;
	}
	
	static RawModel model;
	
	static RawModel getModel(Loader loader)
	{
		if (model == null) {
			model = OBJLoader.loadObjectModel("models/items/guns/AmmoBox/box", loader);
		}
		return model;
	}
	
	public void removeAmmo(int amountToRemove) {
		ammoLeft -= amountToRemove;
		if(ammoLeft < 0) {
			ammoLeft = 0;
		}
	}
	
	public int getAmmoLeft() {
		return ammoLeft;
	}

	@Override
	public String getName() {
		return ammoLeft == 0 ? "Empty " + boxType.name + " Ammo Box" : boxType.name + " Ammo Box";
	}

	@Override
	public String[] getPickupSounds() {
		return null;
	}

}
