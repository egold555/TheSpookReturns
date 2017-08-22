package org.golde.java.game.font;

import java.io.File;

import org.golde.java.game.gui.base.GuiText;
import org.golde.java.game.renderEngine.Loader;

/**
 * Holds info about a font, and creates all the nesserery files about the font
 * @author Eric
 *
 */
public class FontType {
	 
    private int textureAtlas;
    private TextMeshCreator loader;
 
    /**
     * Creates a new font and loads up the data about each character from the
     * font file.
     * 
     * @param textureAtlas
     *            - the ID of the font atlas texture.
     * @param fontFile
     *            - the font file containing information about each character in
     *            the texture atlas.
     */
    public FontType(Loader loader, String name) {
        this.textureAtlas = loader.loadTexture("font/" + name);
        this.loader = new TextMeshCreator(new File("res/font/" + name + ".fnt"));
    }
 
    /**
     * @return The font texture atlas.
     */
    public int getTextureAtlas() {
        return textureAtlas;
    }
 
    /**
     * Takes in an unloaded text and calculate all of the vertices for the quads
     * on which this text will be rendered. The vertex positions and texture
     * coords and calculated based on the information from the font file.
     * 
     * @param text
     *            - the unloaded text.
     * @return Information about the vertices of all the quads.
     */
    public TextMeshData loadText(GuiText text) {
        return loader.createTextMesh(text);
    }
 
}
