package de.nerogar.gameV1.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import org.newdawn.slick.openal.*;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;

public class Testsound {

	int buffer;
	int source;
	Vector3d position = new Vector3d();
	Vector3d velocity = new Vector3d();
	private boolean looping = true;
	private int state;
	private float offset;
	private float size;
	public float crash = 2f;

	public Testsound(File file) throws LWJGLException, IOException {
		this(file, new Vector3d(0, 0, 0), false);
	}

	public Testsound(File file, Vector3d pos, boolean looping) throws LWJGLException, IOException {
		// buffer und sources an OpenAL weiterleiten
		buffer = alGenBuffers();
		source = alGenSources();

		RenderHelper.renderLoadingScreen("Lade "+file.getPath());
		// Datei in den Buffer laden
		String format = getExtension(file);
		switch (format) {
		case "wav":
			setWaveFile(file);
			break;
		case "ogg":
			setVorbisFile(file);
			break;
		default:
			setVorbisFile(file);
		}
		
		// Source festlegen
		setSource(pos, new Vector3d(0, 0, 0));

		// Listener positionieren
		setListener(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0));
		
		// OpenAL mitteilen, ob der Sound wiederholt werden soll
		this.looping = looping;
		updateLooping();
		setGain(1);
		setPitch(1);
		//alDopplerFactor(100000);
		//alDopplerVelocity(1000);
	}

	private void setWaveFile(File file) throws FileNotFoundException {
		WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
		setBuffer(waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
	}

	private void setVorbisFile(File file) throws IOException, LWJGLException {
		FileInputStream fileStream = new FileInputStream(file);
		OggData ogg = new OggDecoder().getData(fileStream);
		int format = (ogg.channels == 2) ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
		setBuffer(format, ogg.data, ogg.rate);
		fileStream.close();
	}

	private void setBuffer(int format, ByteBuffer data, int samplerate) {
		alBufferData(buffer, format, data, samplerate);
		this.size = AL10.alGetBufferi(buffer, AL_SIZE);
	}

	private void setSource(Vector3d position, Vector3d velocity) {
		//if (AL10.alGetError() != AL10.AL_NO_ERROR) return AL10.AL_FALSE;
		alSourcei(source, AL_BUFFER, buffer);
		setPitch(1.5f);
		setGain(1.0f);
		alSource3f(source, AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		alSource3f(source, AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());

	}
	
	public void setPitch(float pitch) {
		alSourcef(source, AL_PITCH, pitch);
	}
	
	public void setGain(float gain) {
		alSourcef(source, AL_GAIN, gain);
	}

	public static void setListener(Vector3d position, Vector3d velocity, Vector3d orientation) {
		alListener3f(AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		alListener3f(AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());
		alListener3f(AL_ORIENTATION, orientation.getXf(), orientation.getYf(), orientation.getZf());
	}

	public void play() {
		alSourcePlay(source);
	}

	public void stop() {
		alSourceStop(source);
	}

	public void pause() {
		alSourcePause(source);
	}

	public void destroy() {
		alDeleteSources(source);
		alDeleteBuffers(buffer);
	}

	public boolean isPaused() {
		return (this.state == AL_PAUSED);
	}

	public boolean isPlaying() {
		return (this.state == AL_PLAYING);
	}

	public boolean isLooping() {
		return looping;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
		updateLooping();
	}

	public void updateLooping() {
		if (looping) {
			alSourcei(source, AL_LOOPING, AL_TRUE);
		} else {
			alSourcei(source, AL_LOOPING, AL_FALSE);
		}
	}
	
	public void update() {
		this.state = alGetSourcei(source, AL_SOURCE_STATE);
		this.offset = (float) alGetSourcei(source, AL_BYTE_OFFSET) / size;
		if (crash < 1) {
			setOffset(crash);
		}
		//System.out.println(offset);
	}
	
	public boolean isStopped() {
		return (this.state == AL_STOPPED);
	}

	public int getState() {
		return this.state;
	}

	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		alSourcei(source, AL_BYTE_OFFSET, (int) (offset*size));
	}
	
	public void crash() {
		crash = getOffset();
	}
	
	public void uncrash() {
		crash = 2;
	}
	
	public String getExtension(File file) {
		String[] parts = file.getName().split("\\.");
		if (parts.length <= 1) return null;
		return parts[parts.length-1].toLowerCase();
	}
	
}
