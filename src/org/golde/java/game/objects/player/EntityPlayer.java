package org.golde.java.game.objects.player;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.GameState;
import org.golde.java.game.Main;
import org.golde.java.game.audio.AudioMaster;
import org.golde.java.game.audio.Source;
import org.golde.java.game.gui.base.GuiText;
import org.golde.java.game.gui.player.GuiPlayerOverlay;
import org.golde.java.game.helpers.BetterKeyboard;
import org.golde.java.game.objects.base.colliders.Collider;
import org.golde.java.game.objects.base.colliders.CylinderCollider;
import org.golde.java.game.objects.base.entities.EntityMoveable;
import org.golde.java.game.objects.base.item.Item;
import org.golde.java.game.objects.light.Light;
import org.golde.java.game.objects.player.items.FlashLight;
import org.golde.java.game.objects.player.items.guns.ItemAmmoBox;
import org.golde.java.game.objects.player.items.guns.ItemAmmoBox.EnumAmmoBoxType;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class EntityPlayer extends EntityMoveable{

	private static final float RUN_SPEED = 30;
	private static final float SIDE_SPEED = 30;
	public static final float GRAVITY = -50;
	private static final float JUMP_POWER = 20;

	private List<Item> inventory = new ArrayList<Item>();

	private Source sfx = new Source();
	private int sound;
	private GuiPlayerOverlay guiOverlay;
	private Loader loader;
	
	private Light flashLight;
	private boolean flashlightEnabled;

	public EntityPlayer(Loader loader, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(null, position, rotX, rotY, rotZ, scale);
		this.loader = loader;
		sfx.setLooping(true);
		sfx.setVolume(200);
		sound = AudioMaster.loadSound("player/footsteps_wood");
		guiOverlay = new GuiPlayerOverlay(this, loader);
		guiOverlay.setVisible(true);
		
		Collider collider = new CylinderCollider(new Vector3f(), 2, 11, 0);
		collider.blocks = false;
		addCollider(collider);

		for(int i = 0; i < 9; i++) {
			inventory.add(null);
		}
		int temp = 0;
		for(EnumAmmoBoxType ammoBoxType : EnumAmmoBoxType.values()) {
			inventory.set(temp, new ItemAmmoBox(loader, ammoBoxType));
			temp++;
		}
		
		inventory.set(5, new FlashLight(loader, 0.5f));
		
		flashLight = new Light(position, new Vector3f(0, 0, 0), new Vector3f(0.5f, 0.001f, 0.0001f));
		Main.getLights().add(flashLight);
	}

	boolean shouldUpdatePos = false;

	@Override
	public void move(Terrain terrain) {
		checkInputs();
		
		super.move(terrain);
		
		sfx.setPosition(this.getPosition());

		if(isMoving() && !sfx.isPlaying()) {
			sfx.play(sound);
		}
		if(!isMoving()) {
			sfx.stop();
		}
		
		if(flashlightEnabled) {
			flashLight.setPosition(Main.getCamera().getPosition());
			flashLight.setSpotLight(calculateFlashlightRotation(), 5, 20);
		}
	}
	
	Vector3f calculateFlashlightRotation() {
		float y = (float) -Math.sin(Math.toRadians(getRotY()));
		float x = (float) (Math.cos(Math.toRadians(getRotY())) * Math.sin(Math.toRadians(getRotX())));
	    float z = (float) -(Math.cos(Math.toRadians(getRotY())) * Math.cos(Math.toRadians(getRotX())));
		
		return new Vector3f(x, y, z);
	}
	
	

	private void jump() {
		if(isInAir) {return;}
		addVelocity(0, JUMP_POWER, 0);
	}

	private void onLeftClick() {

	}

	private void onRightClick() {

	}
	
	public void onScrollWheel(int scroll) {
		if (scroll < 0) {
	        guiOverlay.setSlotPosition(guiOverlay.getSelectedSlot() + 1);
	    } else if (scroll > 0){
	    	guiOverlay.setSlotPosition(guiOverlay.getSelectedSlot() - 1);
	   }
	}


	private void checkInputs() {
		if(Main.getGameState() != GameState.PLAYING) {return;}
		float currentSpeed = 0;
		float currentSideSpeed = 0;

		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			currentSpeed = RUN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			currentSpeed = -RUN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			currentSideSpeed = -SIDE_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			currentSideSpeed = SIDE_SPEED;
		}

		float rotX = (float) Math.toRadians(super.getRotX());

		float velX = (float) (currentSpeed * Math.sin(rotX) + currentSideSpeed * Math.cos(rotX));
		float velZ = (float) (-currentSpeed * Math.cos(rotX) + currentSideSpeed * Math.sin(rotX));

		super.setVelocity(velX, getVelocity().y, velZ);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}

		if(Mouse.isButtonDown(0)) {
			onLeftClick();
		}

		if(Mouse.isButtonDown(1)) {
			onRightClick();
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
			fireBullet();
		}

		if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_Q)) {
			int selected = this.getGuiOverlay().getSelectedSlot();
			if(inventory.size() != 0 && selected < inventory.size()) {
				Item itemToDrop = inventory.get(selected);
				if(itemToDrop != null) {
					itemToDrop.drop(getThrowPosition(itemToDrop), getThrowVelocity(itemToDrop));
					inventory.set(selected, null);
				}
			}
		}
	}
	
	

	Vector3f getThrowPosition(Item itemToDrop) {
		// Throw from a position 4 units above feet.
		float throwHeight = 4;

		Vector3f pos = getPosition();
		return new Vector3f(pos.getX(), pos.getY() + throwHeight, pos.getZ()); 
	}

	Vector3f getThrowVelocity(Item itemToDrop)
	{
		float speed = 20;
		float vertSpeed = 5;
		double angle = Math.toRadians(getRotX());

		float x = (float)(speed * Math.sin(angle));
		float z = -(float)(speed * Math.cos(angle));
		float y = vertSpeed;

		return new Vector3f(x, y, z);
	}

	@Override
	public void onRender() {
		if(!canFireBullet) {
			bulletTime++;
			if(bulletTime >=10) {
				bulletTime = 0;
				canFireBullet = true;
			}
		}
		
		
		if(getSelectedItem() != null && getSelectedItem().getClass().equals(FlashLight.class)) {
			flashLight.setColor(new Vector3f(1, 1, 1));
			flashlightEnabled = true;
		}else {
			flashLight.setColor(new Vector3f(0, 0, 0));
			flashlightEnabled = false;
		}
		
		

		super.onRender();
	}

	private boolean canFireBullet = false;
	private int bulletTime = 0;
	private void fireBullet() {
		if(canFireBullet) {
			new EntityBullet(loader, this, 0.1f);
			canFireBullet = false;
		}
	}

	public void sendChat(String msg) {
		sendChat(msg, 0.003f);
	}

	public void sendChat(String msg, float fadeTime) {
		sendChat(msg, 1, 1, 1, fadeTime);
	}

	public void sendChat(String msg, float r, float g, float b) {
		sendChat(msg, r, g, b, 0.003f);
	}

	public void sendChat(String msg, float r, float g, float b, float fadeTime) {
		GuiText chat = guiOverlay.chat;
		chat.setColor(r, g, b);
		chat.setTextString(msg);
		chat.setAlpha(1);
		chat.fadeOut(fadeTime);
	}

	public GuiPlayerOverlay getGuiOverlay() {
		return guiOverlay;
	}

	@Override
	public int getMaxHealth() {
		return 20;
	}

	@Override
	public void onDeath() {
		//TODO: Player death?
	}

	public List<Item> getInventory() {
		return inventory;
	}
	
	public boolean hasItemInInventory(Item i) {
		for(Item it:getInventory()) {
			if( it.equals(i)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasItemInInventory(Class<Item> itemClazz) {
		for(Item it:getInventory()) {
			if(it.getClass().equals(itemClazz)) {
				return true;
			}
		}
		return false;
	}
	
	Item getSelectedItem() {
		return getItemAt(getGuiOverlay().getSelectedSlot());
	}
	
	Item getItemAt(int index) {
		return getInventory().get(index);
	}

}
