package org.golde.java.game.gui.mainMenu;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.GameState;
import org.golde.java.game.Main;
import org.golde.java.game.gui.base.Gui;
import org.golde.java.game.gui.base.button.Button;
import org.golde.java.game.gui.base.button.ButtonEnabledDisabled;
import org.golde.java.game.renderEngine.Loader;
import org.lwjgl.util.vector.Vector2f;

public class GuiOptions extends Gui{

	private List<Button> buttons = new ArrayList<Button>();
	public GuiOptions(Loader loader) {
		
		buttons.add(new ButtonEnabledDisabled(loader, new Vector2f(0.00f, 0f), new Vector2f(0.4f, 0.3f), false, this) {

			@Override
			public void onClick() {
				super.onClick();
			}

			@Override
			public void whileHover() {super.whileHover();}

			@Override
			public void startHover() {super.startHover();}

			@Override
			public void stopHover() {super.stopHover();}

		});
		
		buttons.add(new Button(loader, "options/back", new Vector2f(0.00f, -0.8f), new Vector2f(0.4f, 0.3f), this) {

			@Override
			public void onClick() {
				Main.setGameState(GameState.TITLE_SCREEN);
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
