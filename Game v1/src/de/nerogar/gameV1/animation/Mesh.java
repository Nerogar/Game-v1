package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.Vector3d;

public class Mesh {

	public float[] vectors;
	public int[] boneIDs;
	public byte[] weights;
	public final int vertexCount;

	public Mesh(float[] vectors, int[] boneIDs, byte[] weights) {
		this.vectors = vectors;
		this.boneIDs = boneIDs;
		this.weights = weights;
		vertexCount = vectors.length / 3;
	}

	public float[] getTranslatedVectors(Armature skeleton) {
		float[] translated = new float[vertexCount * 3];
		MeshVertex helperVertex = new MeshVertex(null, new int[4], new float[4]);
		Vector3d helperVector = new Vector3d(0, 0, 0);
		for (int i = 0; i < vertexCount; i++) {
			helperVector.setX(vectors[i * 3]);
			helperVector.setY(vectors[i * 3 + 1]);
			helperVector.setZ(vectors[i * 3 + 2]);
			helperVertex.vector = helperVector;
			helperVertex.boneIDs[0] = boneIDs[i * 4];
			helperVertex.boneIDs[1] = boneIDs[i * 4 + 1];
			helperVertex.boneIDs[2] = boneIDs[i * 4 + 2];
			helperVertex.boneIDs[3] = boneIDs[i * 4 + 3];
			helperVertex.weights[0] = (float) weights[i * 4] / 255;
			helperVertex.weights[1] = (float) weights[i * 4 + 1] / 255;
			helperVertex.weights[2] = (float) weights[i * 4 + 2] / 255;
			helperVertex.weights[3] = (float) weights[i * 4 + 3] / 255;
			Vector3d translatedVector = skeleton.translateMeshVertex(helperVertex);
			translated[i * 3] = translatedVector.getXf();
			translated[i * 3 + 1] = translatedVector.getYf();
			translated[i * 3 + 2] = translatedVector.getZf();
		}
		return translated;
	}

}
