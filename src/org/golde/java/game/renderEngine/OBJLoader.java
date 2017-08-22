package org.golde.java.game.renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.golde.java.game.GLog;
import org.golde.java.game.models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class OBJLoader {

	public static RawModel loadObjectModel(String fileName, Loader loader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			GLog.error("Model 'res/" + fileName + ".obj' does not exist!");
			return loadObjectModel("models/default/noModel", loader);
			
		}
		BufferedReader reader = new BufferedReader(fr);

		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();

		float[] verticiesArray = null;
		float[] normalArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;

		try {
			List<String> fLines = new ArrayList<String>();
			
			while(true) {
				line = reader.readLine();
				if (line == null)
					break;
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}
				else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}
				else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}
				else if(line.startsWith("f ")) {
					fLines.add(line);
				}
			}

			
			int nFaceVertices = fLines.size() * 3;
			textureArray = new float[nFaceVertices * 2];
			normalArray = new float[nFaceVertices * 3];
			verticiesArray = new float[nFaceVertices * 3];
			indicesArray = new int[nFaceVertices];

			int index = 0;
			for (String fLine: fLines) {
				String[] currentLine = fLine.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				prossessVertex(index++, vertex1, vertices, textures, normals, verticiesArray, textureArray, normalArray);
				prossessVertex(index++, vertex2, vertices, textures, normals, verticiesArray, textureArray, normalArray);
				prossessVertex(index++, vertex3, vertices, textures, normals, verticiesArray, textureArray, normalArray);
			}
			reader.close();

		}
		catch(Exception e) {
			GLog.error(e, "Failed to loadObjectModel: " + fileName);
		}

		for(int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = i;
		}
		
		return loader.loadToVAO(verticiesArray, textureArray, normalArray, indicesArray);
		
	}
	
	private static void prossessVertex(int index, String[] vertexData, List<Vector3f> vertices, List<Vector2f> textures, List<Vector3f> normals, float[] vertexArray, float[] textureArray, float[] normalsArray) {
		if (vertexData[1].length() == 0)
			return;
		
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		int currentTexturePointer = Integer.parseInt(vertexData[1]) - 1;
		int currentNormalPointer = Integer.parseInt(vertexData[2]) - 1;
		
		Vector3f currentVert = vertices.get(currentVertexPointer);
		vertexArray[index * 3] = currentVert.x;
		vertexArray[index * 3 + 1] = currentVert.y;
		vertexArray[index * 3 + 2] = currentVert.z;
		
		Vector2f currentTexture = textures.get(currentTexturePointer);
		textureArray[index * 2] = currentTexture.x;
		textureArray[index * 2+1] = 1 - currentTexture.y;
		
		Vector3f currentNorm = normals.get(currentNormalPointer);
		normalsArray[index * 3] = currentNorm.x;
		normalsArray[index * 3 + 1] = currentNorm.y;
		normalsArray[index * 3 + 2] = currentNorm.z;
	}

}
