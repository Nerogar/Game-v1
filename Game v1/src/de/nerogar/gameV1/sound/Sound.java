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
	private boolean paused;
	private boolean playing;

	public Sound(File file, String format) throws LWJGLException, IOException {
		this(file, format, new Vector3d(0, 0, 0));
	}

	public Sound(File file, String format, Vector3d pos) throws LWJGLException, IOException {
		// buffer und sources an OpenAL weiterleiten
		buffer = AL10.alGenBuffers();
		source = AL10.alGenSources();

		// Datei in den Buffer laden
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
		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
		AL10.alSource3f(source, AL10.AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		AL10.alSource3f(source, AL10.AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());

	}

	public static void setListener(Vector3d position, Vector3d velocity, Vector3d orientation) {
		AL10.alListener3f(AL10.AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		AL10.alListener3f(AL10.AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());
		AL10.alListener3f(AL10.AL_ORIENTATION, orientation.getXf(), orientation.getYf(), orientation.getZf());
	}

	public void play() {
		AL10.alSourcePlay(source);
		paused = false;
		playing = true;
	}

	public void stop() {
		AL10.alSourceStop(source);
		paused = false;
		playing = false;
	}
	
	public void pause() {
		AL10.alSourcePause(source);
		paused = true;
	}

	public void destroy() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isPlaying() {
		return playing;
	}


}
