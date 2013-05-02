package de.nerogar.gameV1.debug;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.graphics.Shader;
import de.nerogar.gameV1.graphics.ShaderBank;
import de.nerogar.gameV1.level.EntityTestparticle;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class DebugNerogar {

	private Game game;
	private EntityTestparticle[] testParticles;
	private boolean spawned = false;
	public double time = 0;

	// war private, gar aber eine Warning geworfen

	public DebugNerogar(Game game) {
		this.game = game;

	}

	public void startup() {
		ShaderBank.instance.createShaderProgramm("test");
		Shader testShader = ShaderBank.instance.getShader("test");

		testShader.setVertexShader("res/shaders/testShader.vert");
		testShader.setFragmentShader("res/shaders/testShader.frag");
		testShader.compile();
		
		
		/*
		 * noch kleine Probleme beim erstellen von Shadern
		 * vSync geht nicht (shader compile ist falsch)
		 * schwarz gerendert ohne fragment shader
		 * 
		 */
	}

	public void run() {
		//shader tests

		Shader testShader = ShaderBank.instance.getShader("test");
		testShader.reloadFiles();
		testShader.compile();

		//particle tests

		time += game.timer.delta;

		if (game.world.isLoaded && !spawned) {
			testParticles = new EntityTestparticle[5000];
			for (int i = 0; i < testParticles.length; i++) {
				float x = 0;
				float z = 0;
				float y = 5;

				ObjectMatrix particleMatrix = new ObjectMatrix(new Vector3d(x, y, z));

				testParticles[i] = new EntityTestparticle(game, particleMatrix);
				//testParticles[i].standardAcceleration = new Vector3d(0, 0, 0);
				testParticles[i].liveTime = 10000;
				testParticles[i].matrix.scaling = new Vector3d(0.4, 0.4, 0.4);
				game.world.spawnEntity(testParticles[i]);
			}

			spawned = true;
		} else if (spawned) {
			float force = 7;
			int iteration = (int) (game.timer.getFramecount() % testParticles.length);

			testParticles[iteration].matrix.position = new Vector3d(0, 5, 0);
			float x = (float) (Math.random() * force) - force / 2;
			float z = (float) (Math.random() * force) - force / 2;
			float y = (float) (Math.random() * 12);
			testParticles[iteration].addForce(new Vector3d(x, y, z));

		}

		/*tornado setup
		if (game.world.isLoaded && !spawned) {
			testParticles = new EntityTestparticle[3000];
			for (int i = 0; i < testParticles.length; i++) {
				float x = (float) (0 + (Math.random() * 5f));
				float z = (float) (0 + (Math.random() * 5f));
				float y = 5;

				ObjectMatrix particleMatrix = new ObjectMatrix(new Vector3d(x, y, z));

				testParticles[i] = new EntityTestparticle(game, particleMatrix);
				testParticles[i].standardAcceleration = new Vector3d(0, 0, 0);
				testParticles[i].liveTime = 10000;
				testParticles[i].matrix.scaling = new Vector3d(0.4, 0.4, 0.4);
				game.world.spawnEntity(testParticles[i]);
			}

			spawned = true;
		} else if (spawned) {

			float rotations = 32;

			for (int i = 0; i < testParticles.length; i++) {
				float angle = (float) (((float) i / testParticles.length) * Math.PI * 2 * rotations - (time / 50f));
				float radius = ((float) i / testParticles.length) * 2 + 2;
				float x = (float) Math.cos(angle) * radius;
				float z = (float) Math.sin(angle) * radius;
				float y = 2 + (i / 360f);
				Vector3d position = new Vector3d(x, y, z);

				testParticles[i].matrix.setPosition(position);

			}
		}*/
	}

	public void end() {

	}

	public void additionalRender() {
		
	}

}
