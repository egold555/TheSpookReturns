package org.golde.java.game.textures.gui;

import org.golde.java.game.renderEngine.Loader;
import org.lwjgl.util.vector.Vector2f;

public class GuiStaticTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	protected GuiStaticTexture(Vector2f position, Vector2f scale) {
		this.position = position;
		this.scale = scale;
	}
	
	public GuiStaticTexture(Loader loader, String texture, Vector2f position, Vector2f scale) {
		this.texture = loader.loadTexture("gui/" + texture);
		this.position = position;
		this.scale = scale;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
	public void onRender() {}
	
}
