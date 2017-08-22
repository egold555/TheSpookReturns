package org.golde.java.game.textures.particles;

public class ParticleTexture {

	private int textureId;
	private int numberOfRows;
	private boolean additive = false;
	public ParticleTexture(int textureId, int numberOfRows) {
		this.textureId = textureId;
		this.numberOfRows = numberOfRows;
	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public boolean isAdditive() {
		return additive;
	}

	public void setAdditive(boolean additive) {
		this.additive = additive;
	}
	
	
	
}
