package org.golde.java.game.gui.base.button;

import org.golde.java.game.gui.base.Gui;
import org.golde.java.game.renderEngine.DisplayManager;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.textures.gui.GuiStaticTexture;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public abstract class Button implements IButton {
	//private Loader loader;
	private Vector2f position, scale, origScale;
	//private Vector4f color = new Vector4f(1, 1, 1, 1);
	private GuiStaticTexture guiTexture;
	private boolean isHidden = false;
	private boolean isHovering = false;
	private Gui gui;

	public Button(Loader loader, String texture, Vector2f position, Vector2f scale, Gui gui) {
		// this.loader = loader;
		this.position = position;
		this.scale = scale;
		this.origScale = scale;
		this.gui = gui;
		this.guiTexture = new GuiStaticTexture(loader, texture, position, scale);
	}

	/* public Button(Loader loader, String texture, Vector2f position, Vector2f scale, Vector4f color) {
        this(loader, texture, position, scale);
        this.color = color;
        this.guiTexture.setColor(color);
    }*/

	public void checkHover() {
		if (!isHidden) {
			Vector2f location = guiTexture.getPosition();
			Vector2f scale = guiTexture.getScale();
			Vector2f mouseCoordinates = DisplayManager.getNormailzedMouseCoordinates();
			if (location.y + scale.y > -mouseCoordinates.y && location.y - scale.y < -mouseCoordinates.y && location.x + scale.x > mouseCoordinates.x && location.x - scale.x < mouseCoordinates.x) {
				whileHover();
				if (!isHovering) {
					isHovering = true;
					startHover();
				}
				while (Mouse.next())
					if (Mouse.isButtonDown(0)) onClick();
			} else {
				if (isHovering) {
					isHovering = false;
					stopHover();
				}
				guiTexture.setScale(this.scale);
			}
		}
	}
	
	public void setGuiTexture(GuiStaticTexture guiTexture) {
		if (!isHidden) {
			stopRender();
		}
		this.guiTexture = guiTexture;
		if (!isHidden) {
			startRender();
		}
	}

	public void playHoverAnimation(float scaleFactor) {
		guiTexture.setScale(new Vector2f(scale.x + scaleFactor, scale.y + scaleFactor));
	}

	public void playerClickAnimation(float scaleFactor) {
		guiTexture.setScale(new Vector2f(scale.x - (scaleFactor * 2), scale.y - (scaleFactor * 2)));
	}

	private void startRender() {
		gui.addGuiTexture(guiTexture);
	}

	private void stopRender() {
		gui.removeTexture(guiTexture);
	}


	public void hide() {
		stopRender();
		isHidden = true;
	}

	public void show() {
		startRender();
		isHidden = false;
	}

	public void reopen() {
		hide();
		show();
	}

	public boolean isHovering() {
		return isHovering;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public GuiStaticTexture getGuiTexture() {
		return guiTexture;
	}

	/* public Vector4f getColor() {
        return color;
    }*/

	public Vector2f getScale() {
		return scale;
	}

	public Vector2f getPosition() {
		return position;
	}

	/*public void setColor(Vector4f color) {
        guiTexture.setColor(color);
    }*/

	public void setScale(Vector2f scale) {
		guiTexture.setScale(scale);
	}

	public void setPosition(Vector2f position) {
		guiTexture.setPosition(position);
	}

	public void resetScale() {
		guiTexture.setScale(origScale);
	}

	/*public Button clone() {
		try {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeObject(this);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		ObjectInputStream in = new ObjectInputStream(inputStream);
		Button copied = (Button) in.readObject();
		return copied;
		}catch(Exception e) {
			GLog.error("Count not clone button class!");
			GLog.error(e);
			return null;
		}
	}*/
}
