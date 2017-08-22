package org.golde.java.game.renderEngine.renderers;

import java.util.List;
import java.util.Map;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.VaoList;
import org.golde.java.game.shaders.StaticShader;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				shader.loadAmbientLighting(entity);
				prepareInstance(entity);
				entity.onRender();
				if(entity.getModel() == null) {continue;}
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		if(model == null || model.getRawModel() == null || model.getTexture() == null) {return;}
		GL30.glBindVertexArray(model.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(VaoList.POSITIONS);
		GL20.glEnableVertexAttribArray(VaoList.TEXTURES);
		GL20.glEnableVertexAttribArray(VaoList.NORMALS);
		ModelTexture texture = model.getTexture();
		if(texture.hasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightningValue(texture.hasFakeLightning());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(VaoList.POSITIONS);
		GL20.glDisableVertexAttribArray(VaoList.TEXTURES);
		GL20.glDisableVertexAttribArray(VaoList.NORMALS);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = entity.getTransformationMatrix();
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
}
