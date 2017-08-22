package org.golde.java.game.gui.base.button;

import java.util.HashMap;
import java.util.List;

import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.textures.gui.GuiStaticTexture;

public class ButtonHandler {
    private Loader loader;
    private List<GuiStaticTexture> guiTextureList;
 
    public ButtonHandler(Loader loader, List<GuiStaticTexture> guiList) {
        this.loader = loader;
        this.guiTextureList = guiList;
    }
 
 
    private HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
 
    public void update() {
        for (Button button : buttons.values()) button.checkHover();
    }
 
 
    @SuppressWarnings("unlikely-arg-type")
	public void registerButton(int id, Button button) {
        if (!buttons.containsKey(button)) {
            buttons.put(id, button);
        }
    }
 
    public List<GuiStaticTexture> getGuiTextureList() {
        return guiTextureList;
    }
 
    public HashMap<Integer, Button> getButtons() {
        return buttons;
    }
 
    public Button getButton(int id) {
        return buttons.get(id);
    }
 
    public Loader getLoader() {
        return loader;
    }
}
