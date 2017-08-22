package org.golde.java.game.renderEngine.renderers;

import java.util.List;

import org.golde.java.game.gui.base.Gui;
import org.golde.java.game.helpers.Maths;
import org.golde.java.game.models.RawModel;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.VaoList;
import org.golde.java.game.shaders.GuiShader;
import org.golde.java.game.textures.gui.GuiStaticTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

public class GuiRenderer {

	private final RawModel quad;
	private GuiShader shader;

	public GuiRenderer(Loader loader) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}

	public void render(List<Gui> guis) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(VaoList.POSITIONS);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(Gui gui:guis) {
			gui.onTick();
			if(!gui.isVisible()) {continue;}
			gui.onRender();
			shader.loadBrightness(gui.getBrightness());
			for(GuiStaticTexture guiTexture:gui.getStaticGuiTextures()) {
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, guiTexture.getTexture());
				Matrix4f matrix = Maths.createTransformationMatrix(guiTexture.getPosition(), guiTexture.getScale());
				shader.loadTransformation(matrix);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
				guiTexture.onRender();
			}

		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(VaoList.POSITIONS);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}
