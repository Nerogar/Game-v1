package de.nerogar.gameV1.sound;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.OpenALException;

import org.lwjgl.util.WaveData;
import org.newdawn.slick.openal.OggData;
import org.newdawn.slick.openal.OggDecoder;

public class ALBufferBank {

	public static ALBufferBank instance = new ALBufferBank();
	public HashMap<String, ALBuffer> buffers;

	public ALBufferBank() {
		buffers = new HashMap<String, ALBuffer>();
	}

	public void addSound(String filename) throws IOException, LWJGLException, OpenALException {
		File file = new File("res/sound/" + filename);

		int id = ALHelper.genBuffers();

		// Datei in den Buffer laden
		String format = getExtension(file);
		switch (format) {
		case "wav":
			setWaveFile(id, file);
			break;
		case "ogg":
			setVorbisFile(id, file);
			break;
		default:
			System.out.println("did not recognize extension for: " + file.getName());
			throw new IOException();
		}

		int size = ALHelper.getBufferSize(id);
		buffers.put(file.getName(), new ALBuffer(id, size));
	}

	public ALBuffer getSound(String filename) throws OpenALException, IOException, LWJGLException {
		if (!buffers.containsKey(filename)) addSound(filename);
		return buffers.get(filename);
	}

	private void setWaveFile(int bufferID, File file) throws FileNotFoundException {
		WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
		ALHelper.setBuffer(bufferID, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
	}

	private void setVorbisFile(int bufferID, File file) throws IOException, LWJGLException {
		FileInputStream fileStream = new FileInputStream(file);
		OggData ogg = new OggDecoder().getData(fileStream);
		int format = (ogg.channels == 2) ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
		//System.out.println(format == AL_FORMAT_MONO16);
		ALHelper.setBuffer(bufferID, format, ogg.data, ogg.rate);
		fileStream.close();
	}

	public String getExtension(File file) {
		String[] parts = file.getName().split("\\.");
		if (parts.length <= 1) return null;
		return parts[parts.length - 1].toLowerCase();
	}

	public void clear() {
		for (int i = 0; i < buffers.size(); i++) {
			if (buffers.get(i) != null) buffers.get(i).destroy();
		}
	}

}
