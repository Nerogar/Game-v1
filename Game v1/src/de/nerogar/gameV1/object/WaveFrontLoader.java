package de.nerogar.gameV1.object;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WaveFrontLoader {

	private ArrayList<float[]> vertices = new ArrayList<float[]>();
	private ArrayList<float[]> normals = new ArrayList<float[]>();
	private ArrayList<float[]> texCoords = new ArrayList<float[]>();

	private ArrayList<int[]> faceVertsQuads = new ArrayList<int[]>();
	private ArrayList<int[]> faceVertsTriangles = new ArrayList<int[]>();
	private ArrayList<int[]> faceTexQuads = new ArrayList<int[]>();
	private ArrayList<int[]> faceTexTriangles = new ArrayList<int[]>();
	private ArrayList<int[]> faceNormalsQuads = new ArrayList<int[]>();
	private ArrayList<int[]> faceNormalsTriangles = new ArrayList<int[]>();

	public float[] verticesQuads, normalsQuads, texCoordsQuads, verticesTriangles, normalsTriangles, texCoordsTriangles;
	Object3D object = new Object3D();

	public WaveFrontLoader() {

	}

	public Object3D loadObject(String filename) {
		BufferedReader reader;

		try {

			reader = new BufferedReader(new FileReader(filename));

			String line;
			while ((line = reader.readLine()) != null) {

				String[] lineSplit = line.split(" ");

				switch (lineSplit[0]) {
				case "v":
					if (lineSplit.length == 4) {
						float f1 = Float.valueOf(lineSplit[1]);
						float f2 = Float.valueOf(lineSplit[2]);
						float f3 = Float.valueOf(lineSplit[3]);

						addVertex(f1, f2, f3);
					}
					break;
				case "vt":
					if (lineSplit.length == 3) {
						float f1 = Float.valueOf(lineSplit[1]);
						float f2 = 1 - Float.valueOf(lineSplit[2]);

						addTexCoord(f1, f2);
					}

					break;
				case "vn":
					if (lineSplit.length == 4) {
						float f1 = Float.valueOf(lineSplit[1]);
						float f2 = Float.valueOf(lineSplit[2]);
						float f3 = Float.valueOf(lineSplit[3]);

						addNormal(f1, f2, f3);
					}
					break;
				case "f":
					String lineData = line.substring(2);
					lineSplit = lineData.split(" ");

					if (lineSplit.length == 3) {
						int f1 = Integer.valueOf(lineSplit[0].split("/")[0]);
						int f2 = Integer.valueOf(lineSplit[1].split("/")[0]);
						int f3 = Integer.valueOf(lineSplit[2].split("/")[0]);
						int t1 = 0, t2 = 0, t3 = 0;
						int n1 = 0, n2 = 0, n3 = 0;

						if (lineSplit[0].split("/").length > 1) {
							t1 = Integer.valueOf(lineSplit[0].split("/")[1]);
							t2 = Integer.valueOf(lineSplit[1].split("/")[1]);
							t3 = Integer.valueOf(lineSplit[2].split("/")[1]);

							if (lineSplit[0].split("/").length > 2) {
								n1 = Integer.valueOf(lineSplit[0].split("/")[2]);
								n2 = Integer.valueOf(lineSplit[1].split("/")[2]);
								n3 = Integer.valueOf(lineSplit[2].split("/")[2]);

							}
						}
						addFaceVerts(new int[] { f1, f2, f3 });
						addFaceTex(new int[] { t1, t2, t3 });
						addFaceNormal(new int[] { n1, n2, n3 });

					} else if (lineSplit.length == 4) {
						int f1 = Integer.valueOf(lineSplit[0].split("/")[0]);
						int f2 = Integer.valueOf(lineSplit[1].split("/")[0]);
						int f3 = Integer.valueOf(lineSplit[2].split("/")[0]);
						int f4 = Integer.valueOf(lineSplit[3].split("/")[0]);
						int t1 = 0, t2 = 0, t3 = 0, t4 = 0;
						int n1 = 0, n2 = 0, n3 = 0, n4 = 0;

						if (lineSplit[0].split("/").length > 1) {
							t1 = Integer.valueOf(lineSplit[0].split("/")[1]);
							t2 = Integer.valueOf(lineSplit[1].split("/")[1]);
							t3 = Integer.valueOf(lineSplit[2].split("/")[1]);
							t4 = Integer.valueOf(lineSplit[3].split("/")[1]);

							if (lineSplit[0].split("/").length > 2) {
								n1 = Integer.valueOf(lineSplit[0].split("/")[2]);
								n2 = Integer.valueOf(lineSplit[1].split("/")[2]);
								n3 = Integer.valueOf(lineSplit[2].split("/")[2]);
								n4 = Integer.valueOf(lineSplit[3].split("/")[2]);

							}
						}
						addFaceVerts(new int[] { f1, f2, f3, f4 });
						addFaceTex(new int[] { t1, t2, t3, t4 });
						addFaceNormal(new int[] { n1, n2, n3, n4 });
					}

					break;
				default:
					break;
				}

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		initObject();

		return object;
	}

	private void initObject() {
		verticesQuads = new float[faceVertsQuads.size() * 4 * 3];
		normalsQuads = new float[faceVertsQuads.size() * 4 * 3];
		texCoordsQuads = new float[faceVertsQuads.size() * 4 * 2];
		verticesTriangles = new float[faceVertsTriangles.size() * 3 * 3];
		normalsTriangles = new float[faceVertsTriangles.size() * 3 * 3];
		texCoordsTriangles = new float[faceVertsTriangles.size() * 3 * 2];

		for (int i = 0; i < faceVertsQuads.size(); i++) {
			int[] vert = faceVertsQuads.get(i);
			int[] tex = faceTexQuads.get(i);
			int[] norm = faceNormalsQuads.get(i);

			verticesQuads[i * 12 + 0] = vertices.get(vert[0] - 1)[0];
			verticesQuads[i * 12 + 1] = vertices.get(vert[0] - 1)[1];
			verticesQuads[i * 12 + 2] = vertices.get(vert[0] - 1)[2];
			texCoordsQuads[i * 8 + 0] = texCoords.get(tex[0] - 1)[0];
			texCoordsQuads[i * 8 + 1] = texCoords.get(tex[0] - 1)[1];
			normalsQuads[i * 12 + 0] = normals.get(norm[0] - 1)[0];
			normalsQuads[i * 12 + 0] = normals.get(norm[0] - 1)[1];
			normalsQuads[i * 12 + 0] = normals.get(norm[0] - 1)[2];

			verticesQuads[i * 12 + 3] = vertices.get(vert[1] - 1)[0];
			verticesQuads[i * 12 + 4] = vertices.get(vert[1] - 1)[1];
			verticesQuads[i * 12 + 5] = vertices.get(vert[1] - 1)[2];
			texCoordsQuads[i * 8 + 2] = texCoords.get(tex[1] - 1)[0];
			texCoordsQuads[i * 8 + 3] = texCoords.get(tex[1] - 1)[1];
			normalsQuads[i * 12 + 3] = normals.get(norm[1] - 1)[0];
			normalsQuads[i * 12 + 4] = normals.get(norm[1] - 1)[1];
			normalsQuads[i * 12 + 5] = normals.get(norm[1] - 1)[2];

			verticesQuads[i * 12 + 6] = vertices.get(vert[2] - 1)[0];
			verticesQuads[i * 12 + 7] = vertices.get(vert[2] - 1)[1];
			verticesQuads[i * 12 + 8] = vertices.get(vert[2] - 1)[2];
			texCoordsQuads[i * 8 + 4] = texCoords.get(tex[2] - 1)[0];
			texCoordsQuads[i * 8 + 5] = texCoords.get(tex[2] - 1)[1];
			normalsQuads[i * 12 + 6] = normals.get(norm[2] - 1)[0];
			normalsQuads[i * 12 + 7] = normals.get(norm[2] - 1)[1];
			normalsQuads[i * 12 + 8] = normals.get(norm[2] - 1)[2];

			verticesQuads[i * 12 + 9] = vertices.get(vert[3] - 1)[0];
			verticesQuads[i * 12 + 10] = vertices.get(vert[3] - 1)[1];
			verticesQuads[i * 12 + 11] = vertices.get(vert[3] - 1)[2];
			texCoordsQuads[i * 8 + 6] = texCoords.get(tex[3] - 1)[0];
			texCoordsQuads[i * 8 + 7] = texCoords.get(tex[3] - 1)[1];
			normalsQuads[i * 12 + 9] = normals.get(norm[3] - 1)[0];
			normalsQuads[i * 12 + 10] = normals.get(norm[3] - 1)[1];
			normalsQuads[i * 12 + 11] = normals.get(norm[3] - 1)[2];
		}

		for (int i = 0; i < faceVertsTriangles.size(); i++) {
			int[] vert = faceVertsTriangles.get(i);
			int[] tex = faceTexTriangles.get(i);
			int[] norm = faceNormalsTriangles.get(i);

			verticesTriangles[i * 9 + 0] = vertices.get(vert[0] - 1)[0];
			verticesTriangles[i * 9 + 1] = vertices.get(vert[0] - 1)[1];
			verticesTriangles[i * 9 + 2] = vertices.get(vert[0] - 1)[2];
			texCoordsTriangles[i * 6 + 0] = texCoords.get(tex[0] - 1)[0];
			texCoordsTriangles[i * 6 + 1] = texCoords.get(tex[0] - 1)[1];
			normalsTriangles[i * 9 + 0] = normals.get(norm[0] - 1)[0];
			normalsTriangles[i * 9 + 1] = normals.get(norm[0] - 1)[1];
			normalsTriangles[i * 9 + 2] = normals.get(norm[0] - 1)[2];

			verticesTriangles[i * 9 + 3] = vertices.get(vert[1] - 1)[0];
			verticesTriangles[i * 9 + 4] = vertices.get(vert[1] - 1)[1];
			verticesTriangles[i * 9 + 5] = vertices.get(vert[1] - 1)[2];
			texCoordsTriangles[i * 6 + 2] = texCoords.get(tex[1] - 1)[0];
			texCoordsTriangles[i * 6 + 3] = texCoords.get(tex[1] - 1)[1];
			normalsTriangles[i * 9 + 3] = normals.get(norm[1] - 1)[0];
			normalsTriangles[i * 9 + 4] = normals.get(norm[1] - 1)[1];
			normalsTriangles[i * 9 + 5] = normals.get(norm[1] - 1)[2];

			verticesTriangles[i * 9 + 6] = vertices.get(vert[2] - 1)[0];
			verticesTriangles[i * 9 + 7] = vertices.get(vert[2] - 1)[1];
			verticesTriangles[i * 9 + 8] = vertices.get(vert[2] - 1)[2];
			texCoordsTriangles[i * 6 + 4] = texCoords.get(tex[2] - 1)[0];
			texCoordsTriangles[i * 6 + 5] = texCoords.get(tex[2] - 1)[1];
			normalsTriangles[i * 9 + 6] = normals.get(norm[2] - 1)[0];
			normalsTriangles[i * 9 + 7] = normals.get(norm[2] - 1)[1];
			normalsTriangles[i * 9 + 8] = normals.get(norm[2] - 1)[2];
		}

		object.verticesQuads = verticesQuads;
		object.normalsQuads = normalsQuads;
		object.texCoordsQuads = texCoordsQuads;
		object.verticesTriangles = verticesTriangles;
		object.normalsTriangles = normalsTriangles;
		object.texCoordsTriangles = texCoordsTriangles;

		object.updateVbo();
	}

	private void addVertex(float f1, float f2, float f3) {
		vertices.add(new float[] { f1, f2, f3 });
	}

	private void addFaceVerts(int[] indexes) {

		if (indexes.length == 3) {
			faceVertsTriangles.add(new int[] { indexes[0], indexes[1], indexes[2] });
		} else if (indexes.length == 4) {
			faceVertsQuads.add(new int[] { indexes[0], indexes[1], indexes[2], indexes[3] });
		}
	}

	private void addTexCoord(float f1, float f2) {
		texCoords.add(new float[] { f1, f2 });
	}

	private void addFaceTex(int[] indexes) {

		if (indexes.length == 3) {
			faceTexTriangles.add(new int[] { indexes[0], indexes[1], indexes[2] });
		} else if (indexes.length == 4) {
			faceTexQuads.add(new int[] { indexes[0], indexes[1], indexes[2], indexes[3] });
		}
	}

	private void addNormal(float f1, float f2, float f3) {
		normals.add(new float[] { f1, f2, f3 });
	}

	private void addFaceNormal(int[] indexes) {

		if (indexes.length == 3) {
			faceNormalsTriangles.add(new int[] { indexes[0], indexes[1], indexes[2] });
		} else if (indexes.length == 4) {
			faceNormalsQuads.add(new int[] { indexes[0], indexes[1], indexes[2], indexes[3] });
		}
	}
}
