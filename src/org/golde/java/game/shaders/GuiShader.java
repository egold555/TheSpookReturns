package org.golde.java.game.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends ShaderProgram{
    
    private static final String VERTEX_FILE = "src/org/golde/java/game/shaders/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/org/golde/java/game/shaders/guiFragmentShader.txt";
     
    private int location_transformationMatrix;
    private int location_brightness;
 
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
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
