package org.golde.java.game.shaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
 
public class FontShader extends ShaderProgram{
 
    private static final String VERTEX_FILE = PACKAGE + "fontVertex.txt";
    private static final String FRAGMENT_FILE = PACKAGE + "fontFragment.txt";
     
    private int location_color;
    private int location_translation;
    private int location_alpha;
     
    public FontShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_color = super.getUniformLocation("color");
        location_translation = super.getUniformLocation("translation");
        location_alpha = super.getUniformLocation("alpha");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
    
    public void loadAlpha(float alpha) {
    	super.loadFloat(location_alpha, alpha);
    }
    
    public void loadColor(Vector3f color){
        super.loadVector(location_color, color);
    }
     
    public void loadTranslation(Vector2f translation){
        super.load2DVector(location_translation, translation);
    }
 
 
}
