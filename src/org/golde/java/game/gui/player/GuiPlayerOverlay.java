package org.golde.java.game.gui.player;

import org.golde.java.game.gui.base.Gui;
import org.golde.java.game.gui.base.GuiText;
import org.golde.java.game.helpers.BetterKeyboard;
import org.golde.java.game.objects.base.item.Item;
import org.golde.java.game.objects.player.EntityPlayer;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.textures.gui.GuiStaticTexture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

public class GuiPlayerOverlay extends Gui{

	public GuiText chat;
	private EntityPlayer player;

	private GuiStaticTexture selectedTexture;
	private final float firstSlotPos = 0.206f;
	private final float incrementAmount = 0.14f;
	private final float hotbarYPos  = -2.1f;
	private int selectedSlot = 0;


	public GuiPlayerOverlay(EntityPlayer player, Loader loader) {
		this.player = player;
		chat = new GuiText("", 1.5f, new Vector2f(0, 0.8f), 1, true);
		addText(chat);
		addGuiTexture(new GuiStaticTexture(loader, "player/overlay/hotbar", new Vector2f(0.2f, hotbarYPos), new Vector2f(0.9f, 1.4f)));
		selectedTexture = new GuiStaticTexture(loader, "player/overlay/selected", new Vector2f(0.206f, hotbarYPos), new Vector2f(0.9f, 1.4f));
		addGuiTexture(selectedTexture);
	}

	public void setSlotPosition(int slot) {
		selectedSlot = slot;
		
		if(selectedSlot > 8) {
			selectedSlot = 0;
		}
		
		if(selectedSlot < 0) {
			selectedSlot = 8;
		}
		
		selectedTexture.setPosition(new Vector2f(getSlotPos(selectedSlot), -2.1f));
		if(player.getInventory().get(selectedSlot) != null) {
			player.sendChat(player.getInventory().get(selectedSlot).getName());
		}
	}

	public int getSelectedSlot() {
		return selectedSlot;
	}

	private float getSlotPos(int slot) {
		return (incrementAmount * slot) + firstSlotPos;
	}

	private float getIconPos(int slot) {
		return getSlotPos(slot) - 0.83f;
	}
	
	@Override
	public void onRender() {
		
		if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_1)) {
			setSlotPosition(0);
		}
		else if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_2)) {
			setSlotPosition(1);
		}
		else if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_3)) {
			setSlotPosition(2);
		}
		else if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_4)) {
			setSlotPosition(3);
		}
		else if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_5)) {
			setSlotPosition(4);
		}
		else if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_6)) {
			setSlotPosition(5);
		}
		else if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_7)) {
			setSlotPosition(6);
		}
		else if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_8)) {
			setSlotPosition(7);
		}
		else if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_9)) {
			setSlotPosition(8);
		}

		for(Item i:Item.ALL_ITEMS) {
			if(player.getInventory().contains(i) && !guiStaticTextures.contains(i.getIcon())) {
				GuiStaticTexture texture = i.getIcon();
				texture.setPosition(new Vector2f(getIconPos(player.getInventory().indexOf(i)), -0.82f));
				addGuiTexture(texture);
			} 
			else if (!player.getInventory().contains(i) && getStaticGuiTextures().contains(i.getIcon())) {
				removeTexture(i.getIcon());
			}
		}

	}
	
	

}
