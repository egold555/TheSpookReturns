package org.golde.java.game.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.golde.java.game.GLog;
import org.golde.java.game.models.RawModel;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.textures.terrain.TerrainTexture;
import org.golde.java.game.textures.terrain.TerrainTexturePack;
import org.lwjgl.util.vector.Vector3f;

/**
 * Generates terrain based on a hightmap image
 * @author Eric
 *
 */
public class HeightMapTerrain extends Terrain{

	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 256*256*256;
	
	public HeightMapTerrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.px = gridX * SIZE;
		this.pz = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}

	
	
	protected RawModel generateTerrain(Loader loader, String heightMap){
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/terrain/heightmaps/" + heightMap + ".png"));
		} catch (IOException e) {
			GLog.error(e, "Cound not find HeightMap '" + heightMap + "'.");
		}
		
		int VERTEX_COUNT = image.getHeight();
		heights =new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormal(int x, int y, BufferedImage image) {
		float heightL = getHeight(x-1, y, image);
		float heightR = getHeight(x+1, y, image);
		float heightD = getHeight(x, y-1, image);
		float heightU = getHeight(x, y+1, image);
		Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	private float getHeight(int x, int y, BufferedImage image) {
		if(x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight()) {return 0;}
		
		float height = image.getRGB(x, y);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return height;
		
	}
	
}
