package org.golde.java.game.objects.debug;

import org.golde.java.game.models.TexturedModel;
import org.golde.java.game.objects.base.colliders.BoxCollider;
import org.golde.java.game.objects.base.entities.Entity;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.renderEngine.OBJLoader;
import org.golde.java.game.textures.model.ModelTexture;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class EntityColliderBox extends Entity 
{
	Vector3f start, end;
	
	static TexturedModel texturedModel;
	
	private EntityColliderBox(TexturedModel model, BoxCollider boxCollider)
	{
		super(model);

		start = boxCollider.getStartPoint();
		end = boxCollider.getEndPoint();
	}
	
	public static EntityColliderBox create(Loader loader, BoxCollider boxCollider)
	{
		if (texturedModel == null) {
			texturedModel = new TexturedModel(OBJLoader.loadObjectModel("models/collider/box", loader), new ModelTexture(loader.loadTexture("models/collider/texture")));
		}
		return new EntityColliderBox(texturedModel, boxCollider);
	}

	
	@Override
	public Matrix4f getTransformationMatrix() {
		Vector3f center = new Vector3f((start.getX() + end.getX()) / 2.0F, (start.getY() + end.getY()) / 2.0F, (start.getZ() + end.getZ()) / 2.0F);
		float scaleX = (end.getX() - start.getX()) / 2.0F;
		float scaleY = (end.getY() - start.getY()) / 2.0F;
		float scaleZ = (end.getZ() - start.getZ()) / 2.0F;
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(center, matrix, matrix);
		Matrix4f.scale(new Vector3f(scaleX, scaleY, scaleZ), matrix, matrix);
		return matrix;
	}
}
