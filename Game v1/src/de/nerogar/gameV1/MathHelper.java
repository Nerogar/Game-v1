package de.nerogar.gameV1;

public final class MathHelper {

	private static float[] sin;
	private static float[] cos;
	private static float[] tan;

	private static final int LOOKUPLENGTH = 1000;

	private static final int SINLENGTH = LOOKUPLENGTH;
	private static final int COSLENGTH = LOOKUPLENGTH;
	private static final int TANLENGTH = LOOKUPLENGTH;

	private static final double TAU = 2 * Math.PI;
	private static final double degToRad = (double) TAU / 360;
	private static final double radToDeg = (double) 360 / TAU;

	static {

		sin = new float[(int) (SINLENGTH * TAU) + 1];
		for (int i = 0; i < sin.length; i++) {
			sin[i] = (float) Math.sin((float) i / SINLENGTH);
		}

		cos = new float[(int) (COSLENGTH * TAU) + 1];
		for (int i = 0; i < cos.length; i++) {
			cos[i] = (float) Math.cos((float) i / COSLENGTH);
		}

		tan = new float[(int) (TANLENGTH * TAU) + 1];
		for (int i = 0; i < tan.length; i++) {
			tan[i] = (float) Math.tan((float) i / TANLENGTH);
		}

	}

	// x in Rad
	public static float sin(float x) {

		double x2 = x;
		x2 = x2 % TAU;
		if (x2 < 0) x2 += TAU;
		x2 = x2 * SINLENGTH;

		return sin[(int) x2];

	}

	// x in Rad
	public static float cos(float x) {

		double x2 = x;
		x2 = x2 % TAU;
		if (x2 < 0) x2 += TAU;
		x2 = x2 * COSLENGTH;

		return cos[(int) x2];

	}

	// x in Rad
	public static float tan(float x) {

		double x2 = x;
		x2 = x2 % TAU;
		if (x2 < 0) x2 += TAU;
		x2 = x2 * TANLENGTH;

		return tan[(int) x2];

	}

	public static double sqrt(double x) {

		return Math.sqrt(x);

	}

	public static double abs(double x) {

		if (x < 0) return -1 * x;
		return x;

	}

	public static int roundTo0(double d) {
		return (int) d;
	}

	public static int divDownToInt(double d, int n) {
		int ret = (int) (d / n);
		if (((int) (d / n)) != (d / n)) if (d < 0) ret--;

		return ret;
	}

	public static int divUpToInt(double d, int n) {
		int ret = (int) ((d + n) / n);
		if (((int) (d / n)) != (d / n)) if (d < 0) ret--;

		return ret;
	}

	public static int roundDownToInt(double d, int n) {
		double ret = d / n;
		ret = Math.floor(ret);
		ret *= n;
		return (int) ret;
	}

	public static int roundUpToInt(double d, int n) {

		double ret = d / n;
		ret = Math.ceil(ret);
		ret *= n;
		return (int) ret;
	}

	public static double modToInt(double d, int n) {
		double temp = d - roundDownToInt(d, n);
		if (temp == n) {
			temp -= n;
		}
		return temp;
		/*int temp = ((int) d) % n;
		if (d < 0 && temp < 0) temp += n;
		double decimals = (d - Math.floor(d));
		return temp + decimals;*/

	}

	public static float getHightest(float... d) {
		float dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] > dH) dH = d[i];
		}
		return dH;
	}
	
	public static double getHightest(double... d) {
		double dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] > dH) dH = d[i];
		}
		return dH;
	}

	public static int getHightest(int... d) {
		int dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] > dH) dH = d[i];
		}
		return dH;
	}

	public static float getLowest(float... d) {
		float dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] < dH) dH = d[i];
		}
		return dH;
	}
	
	public static double getLowest(double... d) {
		double dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] < dH) dH = d[i];
		}
		return dH;
	}

	public static int getLowest(int... d) {
		int dH = d[0];
		for (int i = 0; i < d.length; i++) {
			if (d[i] < dH) dH = d[i];
		}
		return dH;
	}

	public static double DegToRad(double deg) {
		return degToRad * deg;
	}

	public static double RadToDeg(double rad) {
		return radToDeg * rad;
	}

}
