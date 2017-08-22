package org.golde.java.game.textures.model;

public class ModelTexture {

	private int textureID;
	private float shineDamper = 3f;
	private float reflectivity = 0.4f;
	private boolean hasTransparency = false;
	private boolean useFakeLightning = false;
	
	protected ModelTexture() {}
	
	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	public int getTextureID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean hasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean hasFakeLightning() {
		return useFakeLightning;
	}

	public void setUseFakeLightning(boolean useFakeLightning) {
		this.useFakeLightning = useFakeLightning;
	}
	
	public void onRender() {}
	
}
