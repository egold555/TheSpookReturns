package org.golde.java.game.objects.light;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	private Vector3f spotDirection = new Vector3f(0, 1, 0);
	private float innerConeCos = -1;
	private float outerConeCos = -2;
	
	private boolean permanent = false;
	
	public Light(Vector3f position) {
		this(position, new Vector3f(1, 1, 1));
	}
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
		this.permanent = true;
	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
		this.permanent = false;
	}
	
	public boolean isPermanent() {
		return permanent;
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
	}
	
	public void setAttenuation(float att1, float att2, float att3) {
		attenuation = new Vector3f(att1, att2, att3);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public Vector3f getSpotDirection() {
		return spotDirection;
	}

	
	public float getSpotInnerConeCos() {
		return innerConeCos;
	}
	
	public float getSpotOuterConeCos() {
		return outerConeCos;
	}
	
	public void setSpotLight(Vector3f dir, float innerWidth, float outerWidth)
	{
		dir.normalise(spotDirection);
		innerConeCos = (float) Math.cos(Math.toRadians(innerWidth));
		outerConeCos = (float) Math.cos(Math.toRadians(outerWidth));
	}
}
