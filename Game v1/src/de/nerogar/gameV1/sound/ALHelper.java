package de.nerogar.gameV1.sound;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.OpenALException;

import de.nerogar.gameV1.Vector3d;

public class ALHelper {

	public static void play(ALSource source) throws OpenALException {
		alSourcePlay(source.getSourceID());
		checkAndThrowALError();
	}

	public static void stop(ALSource source) throws OpenALException {
		alSourceStop(source.getSourceID());
		checkAndThrowALError();
	}

	public static void pause(ALSource source) throws OpenALException {
		alSourcePause(source.getSourceID());
		checkAndThrowALError();
	}
	
	public static int genSources() throws OpenALException {
		IntBuffer ib = BufferUtils.createIntBuffer(1);
		alGenSources(ib);
		checkAndThrowALError();
		return ib.get(0);
	}
	
	public static int genBuffers() throws OpenALException {
		IntBuffer ib = BufferUtils.createIntBuffer(1);
		alGenBuffers(ib);
		checkAndThrowALError();
		return ib.get(0);
	}
	
	public static void setBuffer(int bufferID, int format, ByteBuffer data, int samplerate) throws OpenALException {
		alBufferData(bufferID, format, data, samplerate);
		checkAndThrowALError();
	}
	
	public static void bindBufferToSource(ALBuffer buffer, ALSource source) throws OpenALException {
		alSourcei(source.getSourceID(), AL_BUFFER, buffer.getBufferID());
		checkAndThrowALError();
	}
	
	public static int getBufferSize(int id) {
		int result = AL10.alGetBufferi(id, AL_SIZE);
		checkAndThrowALError();
		return result;
	}
	
	public static void setPosition(ALSource source, Vector3d position) throws OpenALException {
		alSource3f(source.getSourceID(), AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		checkAndThrowALError();
	}
	
	public static void setVelocity(ALSource source, Vector3d velocity) throws OpenALException {
		alSource3f(source.getSourceID(), AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());
		checkAndThrowALError();
	}

	public static void setLooping(ALSource source, boolean looping) throws OpenALException {
		if (looping) alSourcei(source.getSourceID(), AL_LOOPING, AL_TRUE);
		else alSourcei(source.getSourceID(), AL_LOOPING, AL_FALSE);
		checkAndThrowALError();
	}
	
	public static void setPitch(ALSource source, float pitch) throws OpenALException {
		alSourcef(source.getSourceID(), AL_PITCH, pitch);
		checkAndThrowALError();
	}
	
	public static void setGain(ALSource source, float gain) throws OpenALException {
		alSourcef(source.getSourceID(), AL_GAIN, gain);
		checkAndThrowALError();
	}
	
	public static void setOffset(ALSource source, float offset) throws OpenALException {
		alSourcei(source.getSourceID(), AL_BYTE_OFFSET, (int) (offset*source.getAlBuffer().getSize()));
		checkAndThrowALError();
	}
	
	public static int getByteOffset(ALSource source) throws OpenALException {
		int result = alGetSourcei(source.getSourceID(), AL_BYTE_OFFSET);
		checkAndThrowALError();
		return result;
	}
	
	public static int getSourceState(ALSource source) throws OpenALException {
		int result = alGetSourcei(source.getSourceID(), AL_SOURCE_STATE);
		checkAndThrowALError();
		return result;
	}
	
	public static void setListener(Vector3d position, Vector3d velocity, Vector3d orientationAt, Vector3d orientationUp) {
		alListener3f(AL_POSITION, position.getXf(), position.getYf(), position.getZf());
		alListener3f(AL_VELOCITY, velocity.getXf(), velocity.getYf(), velocity.getZf());
		FloatBuffer ori = BufferUtils.createFloatBuffer(6);
		ori.put(0, orientationAt.getXf());
		ori.put(1, orientationAt.getYf());
		ori.put(2, orientationAt.getZf());
		ori.put(3, orientationUp.getXf());
		ori.put(4, orientationUp.getYf());
		ori.put(5, orientationUp.getZf());
		alListener(AL_ORIENTATION, ori);
		//alListenerf(AL_REFERENCE_DISTANCE, 0f);
		checkAndThrowALError();
	}
	
	public static void destroySource(ALSource source) throws OpenALException {
		IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
		intBuffer.put(0, source.getSourceID());
		alDeleteSources(intBuffer);
		checkAndThrowALError();
	}
	
	public static void destroyBuffer(ALBuffer buffer) throws OpenALException {
		IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
		intBuffer.put(0, buffer.getBufferID());
		alDeleteBuffers(intBuffer);
		checkAndThrowALError();
	}

	public static String getALErrorString(int err) {
		switch (err) {
		case AL_NO_ERROR:
			return "AL_NO_ERROR";
		case AL_INVALID_NAME:
			return "AL_INVALID_NAME";
		case AL_INVALID_ENUM:
			return "AL_INVALID_ENUM";
		case AL_INVALID_VALUE:
			return "AL_INVALID_VALUE";
		case AL_INVALID_OPERATION:
			return "AL_INVALID_OPERATION";
		case AL_OUT_OF_MEMORY:
			return "AL_OUT_OF_MEMORY";
		default:
			return "No such error code";
		}
	}

	/*public static String getALCErrorString(int err) {
		switch (err) {
		case ALC_NO_ERROR:
			return "AL_NO_ERROR";
		case ALC_INVALID_DEVICE:
			return "ALC_INVALID_DEVICE";
		case ALC_INVALID_CONTEXT:
			return "ALC_INVALID_CONTEXT";
		case ALC_INVALID_ENUM:
			return "ALC_INVALID_ENUM";
		case ALC_INVALID_VALUE:
			return "ALC_INVALID_VALUE";
		case ALC_OUT_OF_MEMORY:
			return "ALC_OUT_OF_MEMORY";
		default:
			return "no such error code";
		}
	}*/

	public static void checkAndThrowALError() throws OpenALException {
		int error = alGetError();
		if (error != AL_NO_ERROR) {
			throw new OpenALException(getALErrorString(error));
		}
	}
}
