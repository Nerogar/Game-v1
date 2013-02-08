package de.nerogar.gameV1.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import de.nerogar.gameV1.Vector3d;

public class Sound {

	int buffer;
	int source;
	Vector3d position = new Vector3d();
	Vector3d velocity = new Vector3d();
	private boolean looping = true;
	private int state;

	public Sound(File file) throws LWJGLException, IOException {
		this(file, new Vector3d(0, 0, 0), false);
	}

	public Sound(File file, Vector3d pos, boolean looping) throws LWJGLException, IOException {
		// buffer und sources an OpenAL weiterleiten
		buffer = AL10.alGenBuffers();
		source = AL10.alGenSources();

		// Datei in den Buffer laden
		String format = "wav";
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
	}

	private void setWaveFile(File file) throws FileNotFoundException {
		WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
		setBuffer(waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
	}

	private void setVorbisFile(File file) throws IOException, LWJGLException {
		if (AL10.alIsExtensionPresent("AL_EXT_vorbis")) {
			System.out.println("präsent");
		} else {
			System.out.println("nicht präsent");
		}
		FileInputStream fileStream = new FileInputStream(file);
		ByteBuffer fileBuffer = BufferUtils.createByteBuffer((int) fileStream.getChannel().size());
		fileStream.getChannel().read(fileBuffer);
		fileBuffer.rewind();
		setBuffer(AL10.AL_FORMAT_VORBIS_EXT, fileBuffer, fileBuffer.capacity());
		fileStream.close();
	}

	private void setBuffer(int format, ByteBuffer data, int samplerate) {
		AL10.alBufferData(buffer, format, data, samplerate);
	}

	private void setSource(Vector3d position, Vector3d velocity) {
		//if (AL10.alGetError() != AL10.AL_NO_ERROR) return AL10.AL_FALSE;
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		setPitch(1.5f);
		setGain(1.0f);
		AL10.alSource3f(source, AL10.AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		AL10.alSource3f(source, AL10.AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());

	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(source, AL10.AL_PITCH, pitch);
	}
	
	public void setGain(float gain) {
		AL10.alSourcef(source, AL10.AL_GAIN, gain);
	}

	public static void setListener(Vector3d position, Vector3d velocity, Vector3d orientation) {
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

	public void pause() {
		AL10.alSourcePause(source);
	}

	public void destroy() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}

	public boolean isPaused() {
		return (this.state == AL10.AL_PAUSED);
	}

	public boolean isPlaying() {
		return (this.state == AL10.AL_PLAYING);
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
			AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
		} else {
			AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
		}
	}
	
	public void update() {
		this.state = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
	}
	
	public boolean isStopped() {
		return (this.state == AL10.AL_STOPPED);
	}

	public int getState() {
		return this.state;
	}

}
