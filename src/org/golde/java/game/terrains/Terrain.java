package org.golde.java.game.terrains;

import org.golde.java.game.helpers.Maths;
import org.golde.java.game.models.RawModel;
import org.golde.java.game.renderEngine.Loader;
import org.golde.java.game.textures.terrain.TerrainTexture;
import org.golde.java.game.textures.terrain.TerrainTexturePack;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Abstract class for every type of terrain to enharent from.
 * @author Eric
 *
 */
public abstract class Terrain {

	final float SIZE = 800;

	protected float px;
	protected float pz;
	protected RawModel model;
	protected TerrainTexturePack texturePack;
	protected TerrainTexture blendMap;
	protected float[][] heights;

	public float getX() {
		return px;
	}

	public float getZ() {
		return pz;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}
	
	public float getSize() {
		return SIZE;
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.px;
		float terrainZ = worldZ - this.pz;
		float gridSquareSize = SIZE / (float)heights.length;
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
									heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
									heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	protected abstract RawModel generateTerrain(Loader loader, String heightMap);
}
