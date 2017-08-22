package org.golde.java.game.renderEngine.particles;

import java.util.Random;

import org.golde.java.game.renderEngine.DisplayManager;
import org.golde.java.game.textures.particles.ParticleTexture;
import org.lwjgl.util.vector.Vector3f;

public class RainMaker {
    private float pps, speed, startHeight, endHeight;
    private float minDistance, maxDistance;
    
    private ParticleTexture texture;
    private float scale;
 
    private Random random = new Random();
 
    public RainMaker(ParticleTexture texture, float scale, float pps, float speed, float minDistance, float maxDistance, float startHeight, float endHeight) {
        this.texture = texture;
    	this.pps = pps;
        this.scale = scale;
        this.speed = speed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
    }
    
    public void generateParticles(float x, float z)
    {
        float delta = DisplayManager.getFrameTimeSeconds();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for (int i = 0; i < count; i++) {
            emitParticle(x, z);
        }
        if (Math.random() < partialParticle) {
            emitParticle(x, z);
        }

    }
    
    private void emitParticle(float x, float z)
    {
    	Vector3f pos = initialPosition(x, z);
    	float lifeLength = (startHeight - endHeight) / speed;
        new Particle(texture, pos, new Vector3f(0, -speed, 0), 0, lifeLength, random.nextFloat() * 360f, scale);

    }
    
    private Vector3f initialPosition(float playerX, float playerZ)
    {
    	float distance = minDistance + random.nextFloat() * (maxDistance - minDistance);
    	double angle = random.nextFloat() * Math.PI * 2;
    	float x = (float)(playerX + distance * Math.sin(angle));
    	float z = (float)(playerZ + distance * Math.cos(angle));
    	return new Vector3f(x, startHeight, z);
    }

}
