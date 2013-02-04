package de.nerogar.gameV1.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import de.nerogar.gameV1.Vector3d;

public class Sound {

	int buffer;
	int source;
	Vector3d position = new Vector3d();
	Vector3d velocity = new Vector3d();

	public Sound(File file) throws FileNotFoundException, LWJGLException {
		this(file, new Vector3d(0,0,0));
	}
	
	public Sound(File file, Vector3d pos) throws FileNotFoundException, LWJGLException {
		// OpenAL intialisieren
		AL.create();

		// buffer und sources an OpenAL weiterleiten
		buffer = AL10.alGenBuffers();
		source = AL10.alGenSources();

		// Wave-Datei in den Buffer laden
		setWaveFile(file);

		// Source festlegen
		setSource(pos, new Vector3d(0, 0, 0));

		// Listener positionieren
		setListener(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0));
	}

	private void setWaveFile(File file) throws FileNotFoundException {
		WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
		setBuffer(waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
	}

	private void setBuffer(int format, ByteBuffer data, int samplerate) {
		AL10.alBufferData(buffer, format, data, samplerate);
	}

	private void setSource(Vector3d position, Vector3d velocity) {
		//if (AL10.alGetError() != AL10.AL_NO_ERROR) return AL10.AL_FALSE;
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
		AL10.alSource3f(source, AL10.AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		AL10.alSource3f(source, AL10.AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());

	}

	private void setListener(Vector3d position, Vector3d velocity, Vector3d orientation) {
		AL10.alListener3f(AL10.AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		AL10.alListener3f(AL10.AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());
		AL10.alListener3f(AL10.AL_ORIENTATION, orientation.getXf(), orientation.getYf(), orientation.getZf());
	}
	
	public void play() {
		AL10.alSourcePlay(source);
	}
	
	public void stop() {
		AL10.alSourceStop(source);
	}

	public void kill() {
		killData();
		AL.destroy();
	}
	
	public void killData() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}

}
