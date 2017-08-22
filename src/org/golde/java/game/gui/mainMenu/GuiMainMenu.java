package org.golde.java.game.gui.mainMenu;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.GameState;
import org.golde.java.game.Main;
import org.golde.java.game.gui.base.Gui;
import org.golde.java.game.gui.base.button.Button;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.textures.gui.GuiAnimatedTexture;
import org.golde.java.game.textures.gui.GuiStaticTexture;
import org.lwjgl.util.vector.Vector2f;

public class GuiMainMenu extends Gui{

	public GuiMainMenu(Loader loader) {

		addGuiTexture(new GuiAnimatedTexture(loader, "mainMenu/flash", new Vector2f(0,0), new Vector2f(1, 1), 15, true));
		addGuiTexture(new GuiStaticTexture(loader, "mainMenu/logo", new Vector2f(0.0f, 0.6f), new Vector2f(0.4f, 0.3f)));
		List<Button> buttons = new ArrayList<Button>();

		buttons.add(new Button(loader, "mainMenu/play", new Vector2f(-0.04f, 0.1f), new Vector2f(0.3f, 0.2f), this) {

			@Override
			public void onClick() {
				Main.setGameState(GameState.PLAYING);
			}

			@Override
			public void whileHover() {}

			@Override
			public void startHover() {}

			@Override
			public void stopHover() {}

		});

		/*buttons.add(new Button(loader, "mainMenu/options", new Vector2f(0.04f, -0.4f), new Vector2f(0.4f, 0.3f), this) {

			@Override
			public void onClick() {
				Main.gameState = GameState.OPTIONS;
			}

			@Override
			public void whileHover() {}

			@Override
			public void startHover() {}

			@Override
			public void stopHover() {}

		});*/
		
		buttons.add(new Button(loader, "mainMenu/quit", new Vector2f(0.00f, -0.8f), new Vector2f(0.4f, 0.3f), this) {

			@Override
			public void onClick() {
				Main.exit();
			}

			@Override
			public void whileHover() {}

			@Override
			public void startHover() {}

			@Override
			public void stopHover() {}

		});

		for(Button button:buttons) {
			button.show();
			addButton(button);
		}
	}

	@Override
	public void onRender() {
		for(Button button:getButtons()) {
			button.checkHover();
		}
	}

}
