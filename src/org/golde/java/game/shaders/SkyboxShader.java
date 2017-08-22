package org.golde.java.game.shaders;

import org.golde.java.game.helpers.Maths;
import org.golde.java.game.objects.player.Camera;
import org.lwjgl.util.vector.Matrix4f;

public class SkyboxShader extends ShaderProgram{
	 
    private static final String VERTEX_FILE = PACKAGE + "skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = PACKAGE + "skyboxFragmentShader.txt";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_brightness;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_brightness = super.getUniformLocation("brightness");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
    
    public void loadBrightness(float brightness) {
    	super.loadFloat(location_brightness, brightness);
    }
 
}