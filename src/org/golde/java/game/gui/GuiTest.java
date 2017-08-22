package org.golde.java.game.gui;

import org.golde.java.game.gui.base.Gui;
import org.golde.java.game.gui.base.GuiText;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.textures.gui.GuiAnimatedTexture;
import org.golde.java.game.textures.gui.GuiStaticTexture;
import org.lwjgl.util.vector.Vector2f;

public class GuiTest extends Gui{

	public GuiTest(Loader loader) {
		GuiText testText = new GuiText("Hello World", 5, new Vector2f(0, 0), 1, true);
		testText.setColor(1, 0, 0);
		addText(testText);
		
		GuiStaticTexture staticTexture = new GuiStaticTexture(loader, "test/frame1", new Vector2f(0.5f, 0.5f), new Vector2f(0.1f, 0.1f));
		addGuiTexture(staticTexture);
		
		GuiAnimatedTexture animatedTexture =  new GuiAnimatedTexture(loader, "test", new Vector2f(0.5f, 0.15f), new Vector2f(0.1f, 0.1f), 20, false);
		addGuiTexture(animatedTexture);
	}
	
}
