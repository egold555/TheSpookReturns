package org.golde.java.game.gui.player;

import org.golde.java.game.Main;
import org.golde.java.game.gui.base.Gui;
import org.golde.java.game.gui.base.GuiText;
import org.golde.java.game.helpers.BetterKeyboard;
import org.golde.java.game.objects.base.item.Item;
import org.golde.java.game.objects.player.EntityPlayer;
import org.golde.java.game.renderEngine.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

public class GuiDebug extends Gui{

	EntityPlayer player;
	private int renderTicks = 0;
	
	boolean isDebugEnabled = false;
	
	public GuiDebug(EntityPlayer player) {
		this.player = player;
		addText(new GuiText("", 1,  new Vector2f(0, 0), 1, false));
		addText(new GuiText("", 1, new Vector2f(0, 0.03f), 1, false));
		addText(new GuiText("", 1, new Vector2f(0, 0.06f), 1, false));
		addText(new GuiText("", 1, new Vector2f(0, 0.09f), 1, false));
		addText(new GuiText("FPS: Working on it...", 1, new Vector2f(0, 0.12f), 1, false));
		addText(new GuiText("", 1, new Vector2f(0, 0.09f), 1, false));
		
		setVisible(isDebugEnabled);
		Main.getRenderer().setRenderColliders(isDebugEnabled);
		
	}
	
	
	@Override
	public void onRender() {
		
		getTextsToBeRendered().get(0).setTextString("X: " + player.getPosition().x + " Y:" + player.getPosition().y + " Z: " + player.getPosition().z);
		getTextsToBeRendered().get(1).setTextString("RX: " + player.getRotX() + " RY: " + player.getRotY() + " RZ: " + player.getRotZ());
		getTextsToBeRendered().get(2).setTextString("Is Moving: " + player.isMoving());
		getTextsToBeRendered().get(3).setTextString("Terrain: " + player.getTerrainCurrentlyStandingOn().toString().replace("org.golde.java.game.terrains.", ""));
		getTextsToBeRendered().get(3).setTextString("Inventory: " + toStringInventory());
		
	}
	
	@Override
	public void onTick() {
		
		if(BetterKeyboard.wasKeyPressed(Keyboard.KEY_F3)) {
			isDebugEnabled = !isDebugEnabled;
			setVisible(isDebugEnabled);
			Main.getRenderer().setRenderColliders(isDebugEnabled);
		}
		
		if(renderTicks > 30) {
			everySoOften();
			renderTicks = 0;
		}
		renderTicks++;
		
	}
	
	private void everySoOften() {
		getTextsToBeRendered().get(4).setTextString("FPS: " + Math.round(1 / DisplayManager.getFrameTimeSeconds()));
	}
	
	private String toStringInventory() {
		String s = "{";
		for(Item i: player.getInventory()) {
			if(i == null) {
				s = s + "null, ";
			}else {
				s = s + i.toString() + ", ";
			}
			
		}
		return s + "}";
	}
	
}
