package org.golde.java.game.objects.base.item;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.GLog;
import org.golde.java.game.Main;
import org.golde.java.game.audio.AudioMaster;
import org.golde.java.game.audio.Source;
import org.golde.java.game.helpers.JavaHelper;
import org.golde.java.game.models.RawModel;
import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.Collider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.objects.base.entities.EntityMoveable;
import org.golde.java.game.objects.player.EntityPlayer;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.terrains.Terrain;
import org.golde.java.game.textures.gui.GuiStaticTexture;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Item extends EntityMoveable{

	public abstract String getName();
	public abstract String[] getPickupSounds();

	protected Source sfx = new Source();
	private List<Integer> pickupSounds = new ArrayList<Integer>();
	protected float scale;
	public static List<Item> ALL_ITEMS = new ArrayList<Item>();
	private GuiStaticTexture guiIcon;
	private static final String NO_ICON = "noIcon";
	
	public Item(Loader loader, String modelAndTexture, float scale) {
		this(loader, modelAndTexture, modelAndTexture, scale, null, NO_ICON);
	}
	
	public Item(Loader loader, String model, String texture, float scale) {
		this(loader, model, texture, scale, null, NO_ICON);
	}
	
	public Item(Loader loader, String modelAndTexture, float scale, Vector3f pos) {
		this(loader, modelAndTexture, modelAndTexture, scale, pos, NO_ICON);
	}
	
	//
	
	public Item(Loader loader, String modelAndTexture, float scale, String icon) {
		this(loader, modelAndTexture, modelAndTexture, scale, null, icon);
	}
	
	public Item(Loader loader, String model, String texture, float scale, String icon) {
		this(loader, model, texture, scale, null, icon);
	}
	
	public Item(Loader loader, String modelAndTexture, float scale, Vector3f pos, String icon) {
		this(loader, modelAndTexture, modelAndTexture, scale, pos, icon);
	}
	
	//
	
	public Item(Loader loader, RawModel model, String texture, float scale, String icon) {
		this(loader, model, texture, scale, null, icon);
	}
	
	public Item(Loader loader, String model, String texture, float scale, Vector3f pos, String icon) {
		this(loader, OBJLoader.loadObjectModel("models/items/" + model, loader), texture, scale, pos, icon);
	}
	
	//
	
	public Item(Loader loader, RawModel model, String texture, float scale, Vector3f pos, String icon) {
		super(new TexturedModel(model, new ModelTexture(loader.loadTexture("models/items/" + texture))), pos, 0, 0, 0, scale);
		this.position = pos;
		sfx.setLooping(false);

		if(pos != null) {
			drop(pos, new Vector3f(0, 0, 0));
		}

		if(getPickupSounds() != null) {
			for(String s:getPickupSounds()) {
				if(JavaHelper.isStringEmpty(s)) {
					continue;
				}
				pickupSounds.add(AudioMaster.loadSound("player/items/" + s));
			}
		}
		ALL_ITEMS.add(this);
		if(icon.equals(NO_ICON)) {
			GLog.error(getName() + " Does not have an icon!");
		}
		guiIcon = new GuiStaticTexture(loader, "player/overlay/items/" + icon, new Vector2f(0,0), new Vector2f(0.04f, 0.06f));
	}
	
	public GuiStaticTexture getIcon() {
		return guiIcon;
	}

	public void drop(Vector3f loc, Vector3f velocity) {
		this.setPosition(new Vector3f(loc));
		this.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
		this.isInAir = true;

		Main.getEntities().add(this);
	}
	
	@Override
	public void move(Terrain terrain)
	{
		super.move(terrain);
		
		// When we hit the ground, stop moving.
		if (!isInAir()) {
			setVelocity(0, 0, 0);
		}
	}
	
	@Override
	public int getMaxHealth() {
		return -1;
	}
	
	@Override
	public void onDeath() {

	}

	private int getPickupSound() {
		return pickupSounds.get(rand.nextInt(pickupSounds.size()));
	}
	
	public void onPickup(EntityPlayer player) {};

	private void pickup(EntityPlayer player) {
		if(position == null) {
			return;
		}
		
		setPosition(null);
		if(getPickupSounds() != null) {
			sfx.play(getPickupSound());
		}
		boolean pickedup = false;
		for(int i = 0; i < player.getInventory().size(); i++) {
			if(player.getInventory().get(i) == null) {
				player.getInventory().set(i, this);
				pickedup = true;
				break;
			}
		}
		if(pickedup) {
			player.sendChat(this.getName());
			remove();
			onPickup(player);
		}
		
	}

	public boolean onGround() {
		return position != null;
	}

	@Override
	public void onCollision(Entity collidedWith) {
		if(collidedWith instanceof EntityPlayer) {
			if (!isInAir()) {
				pickup((EntityPlayer)collidedWith);
			}
		}
		super.onCollision(collidedWith);
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public void addCollider(Collider collider) {
		collider.blocks = false;
		super.addCollider(collider);
	}

}
