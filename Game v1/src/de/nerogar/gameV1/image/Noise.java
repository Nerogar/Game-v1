package de.nerogar.gameV1.image;

import java.util.Random;

public class Noise {

	long seed;
	int freq;
	int size;
	int contrast;
	public NoiseTable[] tables;
	final boolean COSINTERPOLATION = true;
	boolean seemLess = true;
	int repeatOffset;
	private Noise lNoise, uNoise, luNoise;

	int[] pow2; //lookup table
	double[] cos; //lookup table
	final int COSLENGTH = 1000;

	public Noise(int size, int contrast, int depth, int freq, long seed, boolean seemLess) {
		this(size, contrast, depth, freq, seed, seemLess, 0);
	}

	public Noise(int size, int contrast, int depth, int freq, long seed, boolean seemLess, int repeatOffset) {
		this.size = size;
		this.seed = seed;
		this.freq = freq;
		this.contrast = contrast;
		this.seemLess = seemLess;
		this.repeatOffset = repeatOffset;

		if (seemLess) {
			lNoise = new Noise(size, contrast, depth, freq, seed - repeatOffset, false, repeatOffset);
			uNoise = new Noise(size, contrast, depth, freq, seed - 1, false, repeatOffset);
			luNoise = new Noise(size, contrast, depth, freq, seed - repeatOffset - 1, false, repeatOffset);
		}

		//creating pow x^2 lookup table
		pow2 = new int[depth];
		for (int i = 0; i < pow2.length; i++) {
			pow2[i] = (int) Math.pow(2, i);
		}

		//creating cos lookup table

		cos = new double[(int) (COSLENGTH * 3.1415927) + 1];
		for (int i = 0; i < cos.length; i++) {
			cos[i] = Math.cos((double) i / COSLENGTH);
		}

		//generating noise values

		tables = new NoiseTable[depth];
		Random rd = new Random(seed);

		for (int i = 0; i < depth; i++) {
			tables[i] = new NoiseTable((int) (size / getFreq(i)) + 2, contrast);
		}

		for (int i = 0; i < tables.length; i++) {
			for (int j = 0; j < contrast; j++) {
				tables[i].values[j] = (1.0F / (double) contrast) * j;
			}
			for (int j = 0; j < contrast; j++) {
				int randomIndex = rd.nextInt(contrast);
				double buffer = tables[i].values[j];
				tables[i].values[j] = tables[i].values[randomIndex];
				tables[i].values[randomIndex] = buffer;
			}
			for (int j = 0; j < tables[i].size; j++) {
				tables[i].valuesX[j] = rd.nextInt(contrast);
			}
			for (int j = 0; j < tables[i].size; j++) {
				tables[i].valuesY[j] = rd.nextInt(contrast);
			}
		}
	}

	private double getFreq(int layer) {
		return (double) freq / (pow2[layer]);
	}

	/*private double interpolate_linear(double v1, double v2, double x) {
		return (v1 * x) + (v2 * (1 - x));
	}*/

	private double interpolate_cosinus(double b, double a, double x) {
		double ft = x * 3.1415927;
		double f = (1 - cos[(int) (ft * COSLENGTH)]) * 0.5;
		return a * (1 - f) + b * f;
	}

	public double getNoise2D(int layer, double x, double y) {
		double ret;
		double v1, v2, v3, v4;
		double xMult, yMult;
		double x1, x2, y1;

		double freq1 = getFreq(layer);

		if (seemLess) {
			if (x < freq1 && y >= freq1) { //left
				v4 = lNoise.tables[layer].values[(int) ((lNoise.tables[layer].valuesX[lNoise.tables[layer].valuesX.length - 2] + lNoise.tables[layer].valuesY[(int) (y / freq1)]) / 2)]; //U L
				v3 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1) + 1] + tables[layer].valuesY[(int) (y / freq1)]) / 2)]; //U R
				v2 = lNoise.tables[layer].values[(int) ((lNoise.tables[layer].valuesX[lNoise.tables[layer].valuesX.length - 2] + lNoise.tables[layer].valuesY[(int) ((y / freq1) + 1)]) / 2)]; //D L
			} else if (x >= freq1 && y < freq1) { //up
				v4 = uNoise.tables[layer].values[(int) ((uNoise.tables[layer].valuesX[(int) (x / freq1)] + uNoise.tables[layer].valuesY[uNoise.tables[layer].valuesY.length - 2]) / 2)]; //U L
				v3 = uNoise.tables[layer].values[(int) ((uNoise.tables[layer].valuesX[(int) (x / freq1) + 1] + uNoise.tables[layer].valuesY[uNoise.tables[layer].valuesY.length - 2]) / 2)]; //U R
				v2 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1)] + tables[layer].valuesY[(int) ((y / freq1) + 1)]) / 2)]; //D L
			} else if (x < freq1 && y < freq1) { //left top (corner)
				v4 = luNoise.tables[layer].values[(int) ((luNoise.tables[layer].valuesX[luNoise.tables[layer].valuesX.length - 2] + luNoise.tables[layer].valuesY[luNoise.tables[layer].valuesY.length - 2]) / 2)]; //U L
				v3 = uNoise.tables[layer].values[(int) ((uNoise.tables[layer].valuesX[(int) (x / freq1) + 1] + uNoise.tables[layer].valuesY[uNoise.tables[layer].valuesY.length - 2]) / 2)]; //U R
				v2 = lNoise.tables[layer].values[(int) ((lNoise.tables[layer].valuesX[lNoise.tables[layer].valuesX.length - 2] + lNoise.tables[layer].valuesY[(int) ((y / freq1) + 1)]) / 2)]; //D L	
			} else { //else
				v4 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1)] + tables[layer].valuesY[(int) (y / freq1)]) / 2)]; //U L
				v3 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1) + 1] + tables[layer].valuesY[(int) (y / freq1)]) / 2)]; //U R
				v2 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1)] + tables[layer].valuesY[(int) ((y / freq1) + 1)]) / 2)]; //D L
			}

			v1 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1) + 1] + tables[layer].valuesY[(int) ((y / freq1) + 1)]) / 2)]; //D R

		} else {
			v4 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1)] + tables[layer].valuesY[(int) (y / freq1)]) / 2)]; //U L
			v3 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1) + 1] + tables[layer].valuesY[(int) (y / freq1)]) / 2)]; //U R
			v2 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1)] + tables[layer].valuesY[(int) ((y / freq1) + 1)]) / 2)]; //D L
			v1 = tables[layer].values[(int) ((tables[layer].valuesX[(int) (x / freq1) + 1] + tables[layer].valuesY[(int) ((y / freq1) + 1)]) / 2)]; //D R
		}

		xMult = (x % freq1) / freq1;
		yMult = (y % freq1) / freq1;

		x1 = interpolate_cosinus(v1, v2, xMult);
		x2 = interpolate_cosinus(v3, v4, xMult);
		y1 = interpolate_cosinus(x1, x2, yMult);

		ret = y1;

		return ret;
	}

	public double getNoisePoint(double x, double y) {

		double ret = 0;
		//combining all layers
		double[] values = new double[tables.length];
		for (int i = 0; i < tables.length; i++) {
			values[i] = (getNoise2D(i, x, y) * 2) - 1;
		}
		for (int i = 0; i < tables.length; i++) {
			double temp = ret + values[i] / pow2[i];
			if (temp > 1f) ret = 1f;
			else if (temp < -1f) ret = -1f;
			else ret += values[i] / pow2[i];
		}

		ret = (ret + 1f) / 2f;

		if (ret < 0.0f || ret > 1.0f) System.out.println("error in NoiseMapGeneration:" + ret);

		return ret;

	}

	public int[] getNoiseMap() {

		return null;
	}
}