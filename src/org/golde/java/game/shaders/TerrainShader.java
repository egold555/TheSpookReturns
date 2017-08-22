package org.golde.java.game.shaders;

import java.util.List;

import org.golde.java.game.helpers.Maths;
import org.golde.java.game.objects.light.Light;
import org.golde.java.game.objects.player.Camera;
import org.golde.java.game.renderEngine.VaoList;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class TerrainShader extends ShaderProgram{
	private static final int MAX_LIGHTS = 8;

	private static final String VERTEX_FILE = "src/org/golde/java/game/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/org/golde/java/game/shaders/terrainFragmentShader.txt";
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPos[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_lightSpotDir[];
	private int location_lightInnerConeCos[];
	private int location_lightOuterConeCos[];
	private int location_shineDamer;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_fogDensity;
	private int location_fogGradient;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(VaoList.POSITIONS, "position");
		super.bindAttribute(VaoList.TEXTURES, "textureCoords");
		super.bindAttribute(VaoList.NORMALS, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamer = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColor = super.getUniformLocation("skyColor");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_fogDensity = super.getUniformLocation("density");
		location_fogGradient = super.getUniformLocation("gradient");
		
		
		location_lightPos = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		location_lightSpotDir = new int[MAX_LIGHTS];
		location_lightInnerConeCos = new int[MAX_LIGHTS];
		location_lightOuterConeCos = new int[MAX_LIGHTS];

		for (int i = 0; i < MAX_LIGHTS; ++i) {
			location_lightPos[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");			
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
			location_lightSpotDir[i] = super.getUniformLocation("lightSpotDir[" + i + "]");
			location_lightInnerConeCos[i] = super.getUniformLocation("lightInnerConeCos[" + i + "]");
			location_lightOuterConeCos[i] = super.getUniformLocation("lightOuterConeCos[" + i + "]");
		}
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(location_skyColor, new Vector3f(r, g, b));
	}
	
	public void loadFog(float density, float gradient) {
		super.loadFloat(location_fogDensity, density);
		super.loadFloat(location_fogGradient, gradient);
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamer, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; ++i) {
			if (i < lights.size()) {
				super.loadVector(location_lightPos[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
				super.loadVector(location_lightSpotDir[i], lights.get(i).getSpotDirection());
				super.loadFloat(location_lightInnerConeCos[i], lights.get(i).getSpotInnerConeCos());
				super.loadFloat(location_lightOuterConeCos[i], lights.get(i).getSpotOuterConeCos());
			}
			else {
				super.loadVector(location_lightPos[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
				super.loadVector(location_lightSpotDir[i], new Vector3f(0, 1, 0));
				super.loadFloat(location_lightInnerConeCos[i], -1.0F);
				super.loadFloat(location_lightOuterConeCos[i], -2.0F);
			}
		}
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}

