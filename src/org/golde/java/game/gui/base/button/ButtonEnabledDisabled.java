package org.golde.java.game.gui.base.button;


import org.golde.java.game.GLog;
import org.golde.java.game.gui.base.Gui;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.textures.gui.GuiStaticTexture;
import org.lwjgl.util.vector.Vector2f;

public class ButtonEnabledDisabled extends Button{

	private boolean state;
	private GuiStaticTexture TEXTURE_ENABLED;
	private GuiStaticTexture TEXTURE_DISABLED;
	
	public ButtonEnabledDisabled(Loader loader, Vector2f position, Vector2f scale, boolean startState, Gui gui) {
		super(loader, startState ? "enabled" : "disabled", position, scale, gui);
		this.state = startState;
		this.TEXTURE_DISABLED = new GuiStaticTexture(loader, "disabled", position, scale);
		this.TEXTURE_ENABLED = new GuiStaticTexture(loader, "enabled", position, scale);
	}
	
	private GuiStaticTexture getBooleanTexture() {
		if(state) {
			return TEXTURE_ENABLED;
		}
		return TEXTURE_DISABLED;
	}
	
	public boolean getState() {
		return state;
	}

	@Override
	public void onClick() {
		GLog.info("BUTTON CLICK:  " + state);
		hide();
		state = !state;
		setGuiTexture(getBooleanTexture());
		show();
	}

	@Override
	public void whileHover() {}

	@Override
	public void startHover() {}

	@Override
	public void stopHover() {}

}
