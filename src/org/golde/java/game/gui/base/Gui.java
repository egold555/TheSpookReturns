package org.golde.java.game.gui.base;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.gui.base.button.Button;
import org.golde.java.game.textures.gui.GuiStaticTexture;

public class Gui {

	private boolean visible = true;
	public List<GuiStaticTexture> guiStaticTextures = new ArrayList<GuiStaticTexture>();
	private List<GuiText> texts = new ArrayList<GuiText>();
	private List<Button> buttons = new ArrayList<Button>();
	
	public boolean isVisible() {
		return visible;
	}
	
	public float getBrightness() {
		return 1;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void onRender() {}
	public void onTick() {}

	public List<GuiStaticTexture> getStaticGuiTextures() {
		return guiStaticTextures;
	}

	public void addGuiTexture(GuiStaticTexture guiTexture) {
		this.guiStaticTextures.add(guiTexture);
	}
	
	public boolean removeTexture(GuiStaticTexture guiTexture) {
		return this.guiStaticTextures.remove(guiTexture);
	}
	
	public List<GuiText> getTextsToBeRendered() {
		return texts;
	}

	public void addText(GuiText text) {
		this.texts.add(text);
	}
	
	public boolean removeText(GuiText text) {
		return this.texts.remove(text);
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public void addButton(Button button) {
		this.buttons.add(button);
	}
	
}
